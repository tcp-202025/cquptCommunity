package cn.cquptCommunity.qa.controller;

import java.util.List;
import java.util.Map;

import cn.cquptCommunity.qa.client.LabelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import cn.cquptCommunity.qa.pojo.Problem;
import cn.cquptCommunity.qa.service.ProblemService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
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
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
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
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",problemService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param problem
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Problem problem  ){
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
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Problem problem, @PathVariable String id ){
		problem.setId(id);
		problemService.update(problem);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
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
	
}
