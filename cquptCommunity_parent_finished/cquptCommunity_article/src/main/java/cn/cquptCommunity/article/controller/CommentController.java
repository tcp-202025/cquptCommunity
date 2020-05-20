package cn.cquptCommunity.article.controller;

import cn.cquptCommunity.article.pojo.Comment;
import cn.cquptCommunity.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 文章评论模块的控制器层
 */

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;//生成token和解析token的工具类

    /**
     * 添加评论
     */
    @PostMapping
    public Result save(@RequestBody Comment comment){
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        //否则token不为空，表示有权限
        commentService.add(comment);
        return new Result(true, StatusCode.OK, "提交成功");
    }

    /**
     * 根据文章ID查询评论列表
     */
    @GetMapping("/article/{articleid}")
    public Result findByArticleid(@PathVariable("articleid") String articleid){
        return new Result(true,StatusCode.OK,"查询成功",commentService.findByArticleid(articleid));
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/article/{articleid}")
    public Result delete(@PathVariable("articleid") String articleid){
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        //否则token不为空，表示有权限
        commentService.delete(articleid);
        return new Result(true,StatusCode.OK,"删除成功");
    }
}
