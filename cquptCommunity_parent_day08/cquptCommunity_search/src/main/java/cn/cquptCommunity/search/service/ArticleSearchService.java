package cn.cquptCommunity.search.service;

import cn.cquptCommunity.search.dao.ArticleSearchDao;
import cn.cquptCommunity.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArticleSearchService {

    @Autowired
    private ArticleSearchDao articleSearchDao;

    /**
     * 向索引库中添加文章
     */
    public void save(Article article){
        articleSearchDao.save(article);
    }

    /**
     * 根据关键字搜索文章，并分页展示
     */
    public Page<Article> findByKey(String key,int page,int size){
        Pageable pageable= PageRequest.of(page-1, size);//封装分页参数，因为spring框架提供的该方法中的页码索引是从0开始的，所以需要减一
        return articleSearchDao.findByTitleOrContentLike(key,key,pageable);//将key作为关键字去文章标题以及文章内容中去查找
    }
}
