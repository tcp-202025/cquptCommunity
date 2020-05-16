package cn.cquptCommunity.search.controller;

import cn.cquptCommunity.search.pojo.Article;
import cn.cquptCommunity.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleSearchController {

    @Autowired
    private ArticleSearchService articleSearchService;

    /**
     * 像索引库中添加文章
     */
    @PostMapping
    public Result save(@RequestBody Article article){
        articleSearchService.save(article);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 根据关键字搜索文章，并分页展示
     */
    @GetMapping("/search/{key}/{page}/{size}")
    public Result findByKey(@PathVariable("key") String key,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Article> pageData = articleSearchService.findByKey(key, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Article>(pageData.getTotalElements(),pageData.getContent()));//返回总数和内容集合
    }
}
