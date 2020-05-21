package cn.cquptCommunity.user.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cquptCommunity.user.client.FriendClient;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import cn.cquptCommunity.user.pojo.User;
import cn.cquptCommunity.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * user的控制器层
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;//生成token和解析token的工具类

	@Autowired
	private FriendClient friendClient;//远程调用friend微服务


	/**
	 * 发送短信验证码
	 */
	@PostMapping("/sendsms/{mobile}")
	public Result sendSms(@PathVariable("mobile") String mobile){
		userService.sendSms(mobile);
		return new Result(true,StatusCode.OK,"发送成功");
	}


	/**
	 * 注册
	 */
	@PostMapping("/register/{code}")
	public Result register(@PathVariable("code") String code,@RequestBody User user){
		//获取缓存中的验证码
		String checkCode_redis=(String) redisTemplate.opsForValue().get("checkCode_"+user.getMobile());
		if(checkCode_redis.isEmpty()){
			return new Result(false,StatusCode.ERROR,"请先获取手机验证码");
		}
		if(!checkCode_redis.equals(code)){
			return new Result(false,StatusCode.ERROR,"验证码错误");
		}
		userService.add(user);//调用add方法注册
		return new Result(true,StatusCode.OK,"注册成功");
	}

	/**
	 * 用户登录
	 */
	@PostMapping("/login")
	public Result login(@RequestBody User user){
		User resultUser=userService.login(user);
		if(resultUser==null){
			return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
		}
		//如果登陆成功：则需要编写一个使得前后端能够进行通话的操作（以前是使用往session中存入这个登录对象的方式，今天使用AWT来完成）
		//生成令牌
		String token = jwtUtil.createJWT(resultUser.getId(), resultUser.getMobile(), "user");
		Map<String,Object> map=new HashMap<>();
		map.put("token",token);
		map.put("roles","user");
		return new Result(true,StatusCode.OK,"登录成功",map);
	}

	/**
	 * 更新用户的关注数和好友的粉丝数,点击关注后，该用户的关注数加一，好友的粉丝数加一
	 * 使用x来表示是关注还是取消关注。如果点的是关注，则x为1，如果是取消关注，x为-1
	 */
	@PutMapping("/inc/{userid}/{friendid}/{x}")
	public void updateFansCountAndFollowCount(@PathVariable String userid,@PathVariable String friendid,@PathVariable int x){
		userService.updateFans_countAndFollow_count(x,userid,friendid);
	}


	/**
	 * 查询登录用户信息
	 */
	@GetMapping("/info")
	public Result findLoginUser(){
		//需要对用户权限进行一个判定：判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");//拿到用户随身携带的令牌
		if(token==null|| "".equals(token)){ //token为空，表示用户未登录
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);//解析token
		String userid = claims.getId();//拿到登录的用户的id
		User user = userService.findById(userid);
		return new Result(true,StatusCode.OK,"查询成功",user);
	}

	/**
	 * 修改当前登录用户信息
	 */
	@PutMapping("/saveinfo")
	public Result updateLoginUser(@RequestBody User user){
		//需要对用户权限进行一个判定：判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");//拿到用户随身携带的令牌
		if(token==null|| "".equals(token)){ //token为空，表示用户未登录
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);//解析token
		String userid = claims.getId();//拿到登录的用户的id
		user.setId(userid);
		userService.update(user);
		return new Result(true,StatusCode.OK,"修改成功");
	}


	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping(value="/{id}")
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
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
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping(value="/search")
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }

	/**
	 * 查询我的粉丝:先拿到当前登录用户的userid，然后根据userid远程调用friend模块查询我的粉丝的id:然后根据这个id再来查询user表拿到我的粉丝的详细数据
	 */
	@GetMapping("/follow/myfans")
	public Result findMyFans(){
		//需要对用户权限进行一个判定：判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");//拿到用户随身携带的令牌
		if(token==null|| "".equals(token)){ //token为空，表示用户未登录
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);//解析token
		String userid = claims.getId();//拿到登录的用户的id
		//远程调用friend模块查询
		List<String> ids = friendClient.findFans(userid);
		List<User> fansList=new ArrayList<>();
		for (String id : ids) {
			User fans = userService.findById(id);
			fansList.add(fans);
		}
		return new Result(true,StatusCode.OK,"查询成功",fansList);
	}

	/**
	 * 查询我的关注:先拿到当前登录用户的userid，然后根据userid远程调用friend模块查询我关注的人的id
	 */
	@GetMapping("/follow/myfollow")
	public Result findMyFollow(){
		//需要对用户权限进行一个判定：判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");//拿到用户随身携带的令牌
		if(token==null|| "".equals(token)){ //token为空，表示用户未登录
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);//解析token
		String userid = claims.getId();//拿到登录的用户的id
		//远程调用friend模块查询
		List<String> ids = friendClient.findMyFollow(userid);
		List<User> myFollowList=new ArrayList<>();
		for (String id : ids) {
			User myFollow = userService.findById(id);
			myFollowList.add(myFollow);
		}
		return new Result(true,StatusCode.OK,"查询成功",myFollowList);
	}



	/**
	 * 查询我的关注ID列表
	 */
	@GetMapping("/follow/myfollowid")
	public Result findMyFollowId(){
		//需要对用户权限进行一个判定：判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");//拿到用户随身携带的令牌
		if(token==null|| "".equals(token)){ //token为空，表示用户未登录
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		Claims claims = jwtUtil.parseJWT(token);//解析token
		String userid = claims.getId();//拿到登录的用户的id
		//远程调用friend模块查询
		List<String> ids = friendClient.findMyFollow(userid);//远程调用friend微服务查询我的关注的ID列表
		return new Result(true,StatusCode.OK,"查询成功",ids);
	}




	/**
	 * 增加
	 * @param user
	 */
	@PostMapping
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@PutMapping(value="/{id}")
	public Result update(@RequestBody User user, @PathVariable String id ){
		//普通用户只能修改自己的账号信息，修改user信息应该是admin用户才有的操作，所以需要判断是否是admin登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_admin");
		if(token==null|| "".equals(token)){ //token为空，表示不是admin管理员访问的，拦截器未将token存入域中
			return new Result(false,StatusCode.ACCESSERROR,"权限不足，无法修改");
		}
		//否则token不为空，表示有权限
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除用户，必须拥有管理员权限，否则不能删除。
	 * 前后端约定：前端请求微服务时需要添加头信息Authorization ,内容为Bearer+空格+token
	 * 如果没有传入的头信息不符合约定规范，返回false
	 * 如果传入的头信息中的令牌不是所给定的令牌，返回false
	 */
	@DeleteMapping(value="/{id}")
	public Result delete(@PathVariable String id ){
		/*String header = request.getHeader("Authorization");//拿到请求头信息
		if(header==null||"".equals(header)){
			return new Result(true,StatusCode.ACCESSERROR,"权限不足，无法删除");
		}
		if(!header.startsWith("Bearer ")){
			return new Result(true,StatusCode.ACCESSERROR,"权限不足，无法删除");
		}
		//如果传入的是符合约定的头信息
		String token = header.substring(7);//从第7位截取，拿到Token
		try {
			//解析token
			Claims claims = jwtUtil.parseJWT(token);
			String roles=(String) claims.get("roles");
			if(roles==null||!roles.equals("admin")){//如果该访问的用户所携带的令牌中的roles不是admin，也表示权限不足
				return new Result(true,StatusCode.ACCESSERROR,"权限不足，无法删除");
			}
			userService.deleteById(id);
		} catch (Exception e) {
			return new Result(true,StatusCode.ACCESSERROR,"权限不足，无法删除");
		}*/

		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_admin");
		if(token==null|| "".equals(token)){ //token为空，表示不是admin管理员访问的，拦截器未将token存入域中
			return new Result(false,StatusCode.ACCESSERROR,"权限不足,无法删除");
		}
		//否则token不为空，表示有权限
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
