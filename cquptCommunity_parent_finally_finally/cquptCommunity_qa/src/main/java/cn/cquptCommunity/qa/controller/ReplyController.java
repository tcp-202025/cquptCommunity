package cn.cquptCommunity.qa.controller;
import java.util.List;
import java.util.Map;

import cn.cquptCommunity.qa.pojo.Problem;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import cn.cquptCommunity.qa.pojo.Reply;
import cn.cquptCommunity.qa.service.ReplyService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 回答的控制器层
 */
@RestController
@CrossOrigin
@RequestMapping("/reply")
public class ReplyController {

	@Autowired
	private ReplyService replyService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;//生成token和解析token的工具类

	/**
	 * 查询全部数据
	 *
	 * @return
	 */
	@GetMapping
	public Result findAll() {
		return new Result(true, StatusCode.OK, "查询成功", replyService.findAll());
	}

	/**
	 * 根据ID查询
	 *
	 * @param id ID
	 * @return
	 */
	@GetMapping(value = "/{id}")
	public Result findById(@PathVariable String id) {
		return new Result(true, StatusCode.OK, "查询成功", replyService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 *
	 * @param searchMap 查询条件封装
	 * @param page      页码
	 * @param size      页大小
	 * @return 分页结果
	 */
	@PostMapping(value = "/search/{page}/{size}")
	public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
		Page<Reply> pageList = replyService.findSearch(searchMap, page, size);
		return new Result(true, StatusCode.OK, "查询成功", new PageResult<Reply>(pageList.getTotalElements(), pageList.getContent()));
	}

	/**
	 * 根据条件查询
	 *
	 * @param searchMap
	 * @return
	 */
	@PostMapping(value = "/search")
	public Result findSearch(@RequestBody Map searchMap) {
		return new Result(true, StatusCode.OK, "查询成功", replyService.findSearch(searchMap));
	}

	/**
	 * 增加
	 *
	 * @param reply
	 */
	@PostMapping("/addreply/{problemid}")
	public Result add(@RequestBody Reply reply,@PathVariable String problemid) {
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);//解析token
		String userid = claims.getId();
		replyService.add(reply,userid,problemid);
		return new Result(true, StatusCode.OK, "增加成功");
	}

	/**
	 * 修改
	 *
	 * @param reply
	 */
	@PutMapping(value = "/{id}")
	public Result update(@RequestBody Reply reply, @PathVariable String id) {
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		reply.setId(id);
		replyService.update(reply);
		return new Result(true, StatusCode.OK, "修改成功");
	}

	/**
	 * 删除
	 *
	 * @param id
	 */
	@DeleteMapping(value = "/{id}")
	public Result delete(@PathVariable String id) {
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		replyService.deleteById(id);
		return new Result(true, StatusCode.OK, "删除成功");
	}

	/**
	 * 查询我（当前登录用户）回答的问题
	 */
	@GetMapping("/myreply")
	public Result findMyReply(){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);//解析token
		String userid = claims.getId();//获取userid
		List<Reply> replies=replyService.findMyReply(userid);
		return new Result(true,StatusCode.OK,"查询成功",replies);
	}

	/**
	 * 根据用户的昵称查出他回答了哪些问题
	 */
	@GetMapping("/findreply/{nickname}")
	public Result findByNickName(@PathVariable String nickname){
		List<Reply> replies=replyService.findByNickName(nickname);
		return new Result(true,StatusCode.OK,"查询成功",replies);
	}

}