package cn.cquptCommunity.article.dao;

import cn.cquptCommunity.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 文章评论的dao:操纵的是mongoDB数据库。需要继承MongoRepository接口
 * MongoRepository<Spit,String>:第一个泛型是实体类的类型，第二个泛型是主键id的类型
 */
public interface CommentDao extends MongoRepository<Comment, String> {

    /**
     * 根据文章ID查询评论列表
     */
    public List<Comment> findByArticleid(String articleid); //使用的是springData中的方法命名规则来查询：相当于是select * from where articleid=?
}
