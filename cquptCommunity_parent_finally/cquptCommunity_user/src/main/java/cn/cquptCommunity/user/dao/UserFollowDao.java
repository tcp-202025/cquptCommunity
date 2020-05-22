package cn.cquptCommunity.user.dao;

import cn.cquptCommunity.user.pojo.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 用户的关注表
 */
public interface UserFollowDao extends JpaRepository<UserFollow,String>, JpaSpecificationExecutor<UserFollow> {

    /**
     * 删除关注
     */
    public void deleteByUseridAndTargetuser(String userid,String targetuser);

    /**
     * 根据userid查询我的粉丝
     */
    public List<UserFollow> findByTargetuser(String userid);

    /**
     * 查询我的关注
     */
    public List<UserFollow> findByUserid(String userid);
}
