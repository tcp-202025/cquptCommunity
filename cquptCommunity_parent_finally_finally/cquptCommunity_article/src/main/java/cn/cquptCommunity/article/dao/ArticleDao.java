package cn.cquptCommunity.article.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.cquptCommunity.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 文章模块的数据层访问接口
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{


    /**
     * 文章审核：id为文章的序号，state表示审核状态：为1表示审核通过，为0表示审核未通过,
     */
    @Modifying //必须使用 @Modifying 进行修饰. 以通知 SpringData， 这是一个 UPDATE 或 DELETE 操作
    @Query(value = "UPDATE a_article SET state=1 WHERE id=?",nativeQuery = true)
    public void updateState(String articleid);


    /**
     * 文章点赞:id为文章的序号, thumbup为文章的点赞数
     */
    @Modifying //必须使用 @Modifying 进行修饰. 以通知 SpringData， 这是一个 UPDATE 或 DELETE 操作
    @Query(value = "UPDATE a_article SET thumbup=thumbup+1 WHERE id=?",nativeQuery = true)
    public void addThumbup(String articleid);


    /**
     * 取消文章点赞:id为文章的序号, thumbup为文章的点赞数
     */
    @Modifying //必须使用 @Modifying 进行修饰. 以通知 SpringData， 这是一个 UPDATE 或 DELETE 操作
    @Query(value = "UPDATE a_article SET thumbup=thumbup-1 WHERE id=?",nativeQuery = true)
    public void deleteThumbup(String articleid);

    /**
     * 根据文章id修改文章的访问数
     */
    @Modifying
    @Query(value = "UPDATE a_article SET visits=visits+1 WHERE id=?",nativeQuery = true)
    public void addVisits(String articleid);

    /**
     * 查询头条文章:文章表中有一个istop字段，为1则表示该文章置顶
     */
    @Modifying
    @Query(value = "select * from a_article where istop=1",nativeQuery = true)
    public List<Article> findTop();

    /**
     * 根据频道id获取文章列表
     */
    public Page<Article> findByChannelid(String channelId, Pageable pageable);

    /**
     * 根据专栏id获取文章列表
     */
    public Page<Article> findByColumnid(String columnId, Pageable pageable);

    /**
     * 查询我的文章
     */
    public List<Article> findByUserid(String userid);

    /**
     * 根据nickname查询该作者的所有文章
     */
    public List<Article> findByNickname(String nickname);

    /**
     * 修改文章置顶
     */
    @Modifying
    @Query(value = "UPDATE a_article SET istop=CASE WHEN istop='0' THEN '1'  WHEN istop='1' THEN '0' ELSE istop END WHERE id=?",nativeQuery = true)
    void updateIsTop(String articleid);

    /**
     * 申请文章是否公开
     */
    @Modifying
    @Query(value = "UPDATE a_article SET ispublic=CASE WHEN ispublic='0' THEN '1'  WHEN ispublic='1' THEN '0' ELSE ispublic END WHERE id=?",nativeQuery = true)
    void updateIsPublic(String articleid);
}
