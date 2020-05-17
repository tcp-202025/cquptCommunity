package cn.cquptCommunity.spit.controller;

import cn.cquptCommunity.spit.pojo.Comment;
import cn.cquptCommunity.spit.service.CommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Result save(@RequestBody Comment comment){
        commentService.add(comment);
        return new Result(true, StatusCode.OK, "提交成功 ");
    }

    /**
     * 根据文章ID查询评论列表
     */
    @GetMapping("/article/{articleid}")
    public Result findByArticleid(@PathVariable("articleid") String articleid){
        return new Result(true,StatusCode.OK,"查询成功",commentService.findByArticleid(articleid));
    }
}
