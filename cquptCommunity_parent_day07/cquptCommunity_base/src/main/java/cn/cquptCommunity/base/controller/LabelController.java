package cn.cquptCommunity.base.controller;

import cn.cquptCommunity.base.pojo.Label;
import cn.cquptCommunity.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin //表示跨域访问
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

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
        labelService.save(label);
        return new Result(true, StatusCode.OK,"添加成功");
    }
    /**根据id修改数据*/
    @PutMapping("/{labelId}")
    public Result update(@PathVariable("labelId") String labelId, @RequestBody Label label){
        label.setId(labelId);
        labelService.update(label);
        return new Result(true, StatusCode.OK,"修改成功");
    }
    /**根据id删除*/
    @DeleteMapping("/{labelId}")
    public Result deleteById(@PathVariable("labelId") String labelId){
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
