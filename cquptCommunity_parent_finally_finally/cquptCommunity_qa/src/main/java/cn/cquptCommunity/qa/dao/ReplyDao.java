package cn.cquptCommunity.qa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.cquptCommunity.qa.pojo.Reply;

import java.util.List;

/**
 * 回答的数据层访问接口
 */
public interface ReplyDao extends JpaRepository<Reply,String>,JpaSpecificationExecutor<Reply>{


    /**
     * 查询我（当前登录用户）回答的问题
     */
    public List<Reply> findByUserid(String userid);

    /**
     * 根据用户的昵称查出他回答了哪些问题
     */
    public List<Reply> findByNickname(String nickname);
}
