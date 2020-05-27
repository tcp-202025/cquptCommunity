package cn.cquptCommunity.article.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import cn.cquptCommunity.article.pojo.Channel;
import cn.cquptCommunity.article.service.ChannelService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 频道模块控制器层
 */
@RestController
@CrossOrigin
@RequestMapping("/channel")
public class ChannelController {

	@Autowired
	private ChannelService channelService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;//生成token和解析token的工具类

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",channelService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping(value="/{id}")
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",channelService.findById(id));
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
		Page<Channel> pageList = channelService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Channel>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping(value="/search")
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",channelService.findSearch(searchMap));
    }

	/**
	 * 增加
	 * @param channel
	 */
	@PostMapping
	public Result add(@RequestBody Channel channel){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		channelService.add(channel);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param channel
	 */
	@PutMapping(value="/{id}")
	public Result update(@RequestBody Channel channel, @PathVariable String id ){
		//需要判断用户是否登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_user");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		//否则token不为空，表示有权限
		channel.setId(id);
		channelService.update(channel);		
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
		channelService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 用户添加频道之后需要向管理员申请审核
	 */
	@PutMapping("/examine/{channelId}")
	public Result updateState(@PathVariable String channelId){
		//需要判断是否是admin登录
		//上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
		String token=(String) request.getAttribute("claims_admin");
		if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//否则token不为空，表示有权限
		channelService.updateState(channelId);
		return new Result(true,StatusCode.OK,"审核成功");
	}
	
}
