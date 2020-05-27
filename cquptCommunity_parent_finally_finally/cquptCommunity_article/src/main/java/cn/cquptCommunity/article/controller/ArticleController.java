package cn.cquptCommunity.article.controller;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import cn.cquptCommunity.article.pojo.Article;
import cn.cquptCommunity.article.service.ArticleService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;


/**
 * 文章模块的控制器层
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;//文章模块的业务层

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;//生成token和解析token的工具类

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 *文章点赞：id为文章的序号，根据id去修改文章的点赞数
	 */
	@PutMapping(value = "/thumbup/{articleId}")
	public Result thumbup(@PathVariable("articleId") String articleId){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		//控制重复点赞：点赞一次后就不能再重复点赞
		//先判断用户是否已经点赞
		Claims claims = jwtUtil.parseJWT(token);
		String userid=claims.getId();
		if(redisTemplate.opsForValue().get("thumbup_"+userid+"_"+articleId)!=null){ //根据用户id去redis中查找，如果找到，则表示当前用户已经点过赞
			return new Result(false,StatusCode.REPERROR,"不能重复点赞");
		}
		articleService.addThumbup(articleId);
		//如果点赞成功，就将该用户标识，存入redis缓存中，防止重复点赞
		redisTemplate.opsForValue().set("thumbup_"+userid+"_"+articleId,"true");
		return new Result(true,StatusCode.OK,"点赞成功");
	}


	/**
	 * 取消点赞
	 */
	@DeleteMapping(value = "/nothumbup/{articleId}")
	public Result NoThumbup(@PathVariable("articleId") String articleId){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);
		String userid=claims.getId();//拿到登录的用户的id
		if(redisTemplate.opsForValue().get("thumbup_"+userid+"_"+articleId)!=null){ //根据用户id去redis中查找，如果找到，则表示当前用户已经点过赞
			//删除缓存中点赞
			redisTemplate.delete("thumbup_"+userid+"_"+articleId);
		}
		articleService.deleteThumbup(articleId);
		return new Result(false,StatusCode.REPERROR,"取消点赞成功");
	}


	
	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping(value="/{id}")
	public Result findById(@PathVariable String id){
		//根据id查询文章：每查询一次，表示访问数加一
		articleService.updateVisits(id);
		return new Result(true,StatusCode.OK,"查询成功",articleService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@PostMapping(value="/search/{page}/{size}")
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Article> pageList = articleService.findSearch(searchMap, page, size);
		return new Result(true,StatusCode.OK,"查询成功",  new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping(value="/search")
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",articleService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param article
	 */
	@PostMapping
	public Result add(@RequestBody Article article){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);//解析token
		String userid = claims.getId();//拿到用户id
		articleService.add(article,userid);
		return new Result(true,StatusCode.OK,"增加成功");
	}

	/**
	 * 新发表文章后，初始审核状态为0，所以需要admin管理员去审核文章：id为文章的序号，根据id去修改文章的审核状态
	 */
	@PutMapping(value = "/examine/{articleId}")
	public Result examine(@PathVariable("articleId") String id){
		//需要判断是否是admin管理员
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_admin");
		if(token==null|| "".equals(token)){ //token为空，表示不是admin管理员，即没有权限
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		Article article = articleService.findById(id);
		if(article!=null&&article.getState().equals("1")){
			return new Result(false,StatusCode.REPERROR,"已经审核，请勿重复提交审核");
		}
		articleService.updateState(id);
		return new Result(true,StatusCode.OK,"审核成功");
	}
	
	/**
	 * 修改
	 * @param article
	 */
	@PutMapping(value="/{id}")
	public Result update(@RequestBody Article article, @PathVariable String id ){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		article.setId(id);
		articleService.update(article);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping(value="/{id}")
	public Result delete(@PathVariable String id ){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		articleService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 根据频道ID获取文章列表
	 */
	@PostMapping("/channel/{channelId}/{page}/{size}")
	public Result findByChannelId(@PathVariable String channelId,@PathVariable int page,@PathVariable int size){
		Page<Article> pageList=articleService.findByChannelId(channelId,page,size);
		return new Result(true,StatusCode.OK,"查询成功",  new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()));//返回查询总数以及查询出的内容集合
	}

	/**
	 * 根据专栏ID获取文章列表
	 */
	@PostMapping("/column/{columnId}/{page}/{size}")
	public Result findByColumnId(@PathVariable String columnId,@PathVariable int page,@PathVariable int size){
		Page<Article> pageList=articleService.findByColumnId(columnId,page,size);
		return new Result(true,StatusCode.OK,"查询成功",  new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()));//返回查询总数以及查询出的内容集合
	}

	/**
	 * 查询头条文章:文章表中有一个istop字段，为1则表示该文章置顶
	 */
	@GetMapping("/top")
	public Result findTop(){
		List<Article> articles=articleService.findTop();
		return new Result(true,StatusCode.OK,"查询成功",articles);
	}

	/**
	 * 查询我的文章
	 */
	@GetMapping("/myarticle")
	public Result findMyArticle(){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);//解析token
		String userid = claims.getId();
		List<Article> articles =articleService.findMyArticle(userid);
		return new Result(true,StatusCode.OK,"查询成功",articles);
	}


	/**
	 * 通过nickname查询该作者的所有文章
	 */
	@GetMapping("/getarticle/{nickname}")
	public Result findByNickname(@PathVariable String nickname){
		List<Article> articles=articleService.findByNickname(nickname);
		return new Result(true,StatusCode.OK,"查询成功",articles);
	}

	/**
	 * 文章置顶:管理员操作，管理文章的时候可以调整文章是否置顶
	 */
	@PutMapping("/becometop/{articleid}")
	public Result becomeTop(@PathVariable String articleid){
		//需要判断是否是admin登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_admin");
		if(token==null|| "".equals(token)){ //token为空，表示没有权限
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		articleService.updateIsTop(articleid);
		return new Result(true,StatusCode.OK,"修改成功");
	}

	/**
	 * 申请文章是否公开：用户操作
	 */
	@PutMapping("/ispublic/{articleid}")
	public Result isPublic(@PathVariable String articleid){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		articleService.updateIsPublic(articleid);
		return new Result(true,StatusCode.OK,"修改成功");
	}
}
