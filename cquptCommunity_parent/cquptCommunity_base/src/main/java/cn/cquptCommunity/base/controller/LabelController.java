package cn.cquptCommunity.base.controller;

import cn.cquptCommunity.base.pojo.Label;
import cn.cquptCommunity.base.service.ILabelService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin //表示跨域访问
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private ILabelService labelService;

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
}
