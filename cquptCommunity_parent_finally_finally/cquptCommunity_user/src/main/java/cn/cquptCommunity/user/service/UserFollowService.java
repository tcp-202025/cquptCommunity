package cn.cquptCommunity.user.service;

import cn.cquptCommunity.user.dao.UserFollowDao;
import cn.cquptCommunity.user.pojo.UserFollow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class UserFollowService {

    @Autowired
    private UserFollowDao userFollowDao;

    /**
     * 用户点击关注后，添加当前用户以及被关注的用户
     */
    public void add(String userid,String targetuser){
        UserFollow userFollow=new UserFollow();
        userFollow.setUserid(userid);
        userFollow.setTargetuser(targetuser);
        userFollowDao.save(userFollow);
    }

    /**
     * 用户取消关注后，删除当前用户以及被关注的用户
     */
    public void delete(String userid,String targetuser){
        userFollowDao.deleteByUseridAndTargetuser(userid,targetuser);
    }

    /**
     * 根据userid查询我的粉丝
     */
    public List<UserFollow> findMyFans(String userid){
        return userFollowDao.findByTargetuser(userid);
    }

    /**
     * 根据userid查询我的粉丝
     */
    public List<UserFollow> findMyFollows(String userid){
        return userFollowDao.findByUserid(userid);
    }

    /**
     * 根据userid和targetuser去查找是否已经关注过
     */
    public UserFollow findByUseridAndTargetuser(String userid, String targetuser){
        return userFollowDao.findByUseridAndTargetuser(userid,targetuser);
    }
}
