package cn.cquptCommunity.qa.controller;

import java.util.List;
import java.util.Map;

import cn.cquptCommunity.qa.client.LabelClient;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import cn.cquptCommunity.qa.pojo.Problem;
import cn.cquptCommunity.qa.service.ProblemService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 问题的控制器层
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

	@Autowired
	private ProblemService problemService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LabelClient labelClient;//feign组件

	@Autowired
	private JwtUtil jwtUtil;//生成token和解析token的工具类

	/**分页查询最新问答*/
	@GetMapping("/newlist/{labelid}/{page}/{size}")
	public Result newList(@PathVariable("labelid") String labelid,@PathVariable("page") int page,@PathVariable("size") int size){
		Page<Problem> problems = problemService.newList(labelid, page, size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<Problem>(problems.getTotalElements(),problems.getContent()));//返回总条数以及list集合
	}

	/**分页查询最热门问答*/
	@GetMapping("/hotlist/{labelid}/{page}/{size}")
	public Result hotList(@PathVariable("labelid") String labelid,@PathVariable("page") int page,@PathVariable("size") int size){
		Page<Problem> problems = problemService.hotList(labelid, page, size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<Problem>(problems.getTotalElements(),problems.getContent()));//返回总条数以及list集合
	}

	/**分页查询处于等待中的问答*/
	@GetMapping("/waitlist/{labelid}/{page}/{size}")
	public Result waitList(@PathVariable("labelid") String labelid,@PathVariable("page") int page,@PathVariable("size") int size){
		Page<Problem> problems = problemService.waitList(labelid, page, size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<Problem>(problems.getTotalElements(),problems.getContent()));//返回总条数以及list集合
	}
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping(value="/{id}")
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findById(id));
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
		Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping(value="/search")
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",problemService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param problem
	 */
	@PostMapping
	public Result add(@RequestBody Problem problem){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		problemService.add(problem);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param problem
	 */
	@PutMapping(value="/{id}")
	public Result update(@RequestBody Problem problem, @PathVariable String id ){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		problem.setId(id);
		problemService.update(problem);		
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
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		problemService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 在问答模块页面根据标签id查询base模块的标签信息
	 */
	@GetMapping("/label/{labelid}")
	public Result findLabelById(@PathVariable("labelid") String labelid) {
		Result result = labelClient.findById(labelid);
		return result;
	}


	/**
	 * 查询我（当前登录用户）提问了哪些问题
	 */
	@GetMapping("/myproblem")
	public Result findMyProblem(){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);//解析token
		String userid = claims.getId();//获取userid
		List<Problem> problems = problemService.findMyProblem(userid);
		return new Result(true,StatusCode.OK,"查询成功",problems);
	}

	/**
	 * 根据用户的昵称查出他提问了哪些问题
	 */
	@GetMapping("/findproblem/{nickname}")
	public Result findByNickName(@PathVariable String nickname){
		List<Problem> problems = problemService.findByNickName(nickname);
		return new Result(true,StatusCode.OK,"查询成功",problems);
	}

}
