package cn.cquptCommunity.base.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import cn.cquptCommunity.base.pojo.City;
import cn.cquptCommunity.base.service.CityService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 城市模块的控制器层
 */
@RestController
@CrossOrigin
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

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
        return new Result(true,StatusCode.OK,"查询成功",cityService.findAll());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @GetMapping(value="/{id}")
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",cityService.findById(id));
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
        Page<City> pageList = cityService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<City>(pageList.getTotalElements(), pageList.getContent()) );
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping(value="/search")
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",cityService.findSearch(searchMap));
    }

    /**
     * 增加
     * @param city
     */
    @PostMapping
    public Result add(@RequestBody City city){
        /**
         * 添加城市信息应该是只有后台管理员才能进行的操作
         */
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_admin");
        if(token==null|| "".equals(token)){ //token为空，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //否则token不为空，表示有权限
        cityService.add(city);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param city
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody City city, @PathVariable String id ){
        /**
         * 修改城市信息应该是只有后台管理员才能进行的操作
         */
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_admin");
        if(token==null|| "".equals(token)){ //token为空，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //否则token不为空，表示有权限
        city.setId(id);
        cityService.update(city);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param id
     */
    @DeleteMapping(value="/{id}")
    public Result delete(@PathVariable String id ){
        /**
         * 删除城市信息应该是只有后台管理员才能进行的操作
         */
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_admin");
        if(token==null|| "".equals(token)){ //token为空，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //否则token不为空，表示有权限
        cityService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

}