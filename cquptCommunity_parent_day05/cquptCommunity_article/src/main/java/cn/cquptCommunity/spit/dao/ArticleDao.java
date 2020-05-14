package cn.cquptCommunity.spit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.cquptCommunity.spit.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{


    /**
     * 文章审核：id为文章的序号，state表示审核状态：为1表示审核通过，为0表示审核未通过,
     */
    @Modifying //必须使用 @Modifying 进行修饰. 以通知 SpringData， 这是一个 UPDATE 或 DELETE 操作
    @Query(value = "UPDATE a_article SET state=1 WHERE id=?",nativeQuery = true)
    public void updateState(String id);


    /**
     * 文章点赞:id为文章的序号, thumbup为文章的点赞数
     */
    @Modifying //必须使用 @Modifying 进行修饰. 以通知 SpringData， 这是一个 UPDATE 或 DELETE 操作
    @Query(value = "UPDATE a_article SET thumbup=thumbup+1 WHERE id=?",nativeQuery = true)
    public void addThumbup(String id);
}
