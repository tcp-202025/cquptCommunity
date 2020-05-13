package cn.cquptCommunity.spit.service;

import cn.cquptCommunity.spit.dao.CommentDao;
import cn.cquptCommunity.spit.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

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
}
