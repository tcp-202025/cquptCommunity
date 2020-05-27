package cn.cquptCommunity.search.dao;

import cn.cquptCommunity.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 文章搜索的dao：需要继承ElasticsearchRepository<>接口，其中的泛型参数为<实体类类型，主键类型>
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String> {

    /**
     * 从索引库中查询文章：根据文章标题或者文章内容来进行模糊查询(需要分页)
     */
    public Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);//使用springData中的方法命名规则，想当于select * from like title=? or content=? limit ?,?


}
