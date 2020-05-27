package cn.cquptCommunity.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.cquptCommunity.article.pojo.Channel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 频道模块的数据层访问接口
 */
public interface ChannelDao extends JpaRepository<Channel,String>,JpaSpecificationExecutor<Channel>{

    /**
     * 频道审核
     */
    @Modifying
    @Query(value = "UPDATE a_channel SET state='1' WHERE id =? ",nativeQuery = true)
    public void updateState(String channelId);
}
