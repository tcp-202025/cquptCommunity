package cn.cquptCommunity.article.service;

import cn.cquptCommunity.article.dao.CommentDao;
import cn.cquptCommunity.article.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * 文章评论模块的业务层
 */
@Service
@Transactional
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 添加文章评论
     */
    public void add(Comment comment){
        comment.set_id(idWorker.nextId()+"");
        comment.setPublishdate(new Date());//设置评论日期
        commentDao.save(comment);
    }

    /**
     * 根据文章ID查询评论列表
     */
    public List<Comment> findByArticleid(String articleid){
        return commentDao.findByArticleid(articleid);
    }

    /**
     * 删除文章评论
     */
    public void delete(String articleid) {
        commentDao.deleteById(articleid);
    }
}
