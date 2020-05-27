package cn.cquptCommunity.gathering.dao;


import cn.cquptCommunity.gathering.pojo.Usergath;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户参加活动的数据层访问接口
 */
public interface UsergathDao extends JpaRepository<Usergath,String>,JpaSpecificationExecutor<Usergath>{

    /**
     * 根据userid查询活动列表
     */
    public List<Usergath> findByUserid(String userid);

    /**
     * 根据userid和gathid来删除参加的活动
     */
    @Modifying
    @Query(value = "delete from g_usergath where userid=? and gathid=?",nativeQuery = true)
    public void deleteGathering(String userid,String gathid);

    /**
     * 根据gathid查询活动列表
     */
    public List<Usergath> findByGathid(String gathid);

    /**
     * 根据当前登录用户的id和活动id查询用户是否查询
     */
    public Usergath findByUseridAndGathid(String userid,String gathid);

}
