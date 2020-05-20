package cn.cquptCommunity.base.controller;

import cn.cquptCommunity.base.pojo.Label;
import cn.cquptCommunity.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * 标签模块的控制器层
 */
@RestController
@CrossOrigin //表示跨域访问
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;//生成token和解析token的工具类

    /**查询推荐标签列表*/
    @GetMapping("/toplist")
    public Result findTopList(){
        List<Label> labelList = labelService.findTopList();
        return new Result(true, StatusCode.OK,"查询成功",labelList);
    }

    /**查询有效标签列表*/
    @GetMapping("/list")
    public Result findEffective(){
        List<Label> labelList = labelService.findEffective();
        return new Result(true, StatusCode.OK,"查询成功",labelList);
    }

    /**查询全部*/
    @GetMapping
    public Result findAll(){
        List<Label> labelList = labelService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",labelList);
    }
    /**根据id查询*/
    @GetMapping("/{labelId}")
    public Result findById(@PathVariable("labelId") String labelId){ //@PathVariable注解用于接收占位符的值
        Label label = labelService.findById(labelId);
        return new Result(true, StatusCode.OK,"查询成功",label);
    }
    /**添加数据*/
    @PostMapping
    public Result save(@RequestBody Label label){ //@RequestBody接收页面传来的json数据，然后转换为Label类型
        /**
         * 添加标签应该是只有后台管理员才能进行的操作
         */
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_admin");
        if(token==null|| "".equals(token)){ //token为空，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //否则token不为空，表示有权限
        labelService.save(label);
        return new Result(true, StatusCode.OK,"添加成功");
    }
    /**根据id修改数据*/
    @PutMapping("/{labelId}")
    public Result update(@PathVariable("labelId") String labelId, @RequestBody Label label){
        /**
         * 修改标签应该是只有后台管理员才能进行的操作
         */
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_admin");
        if(token==null|| "".equals(token)){ //token为空，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //否则token不为空，表示有权限
        label.setId(labelId);
        labelService.update(label);
        return new Result(true, StatusCode.OK,"修改成功");
    }
    /**根据id删除*/
    @DeleteMapping("/{labelId}")
    public Result deleteById(@PathVariable("labelId") String labelId){
        /**
         * 删除标签应该是只有后台管理员才能进行的操作
         */
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_admin");
        if(token==null|| "".equals(token)){ //token为空，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //否则token不为空，表示有权限
        labelService.delete(labelId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /**根据条件查询*/
    @PostMapping("/search")
    public Result findSearch(@RequestBody Label label){
        List<Label> list=labelService.findSearch(label);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    /**分页查询*/
    @PostMapping("/search/{page}/{size}")
    public Result pageQuery(@RequestBody Label label,@PathVariable int page,@PathVariable int size){
        Page<Label> pageData =labelService.pageQuery(label,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Label>(pageData.getTotalElements(),pageData.getContent()));//返回总条数以及list集合
    }
}
