package cn.cquptCommunity.friend.dao;

import cn.cquptCommunity.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 交友的dao（添加喜欢的人）:使用的是springDataJPA,所以需要继承JpaRepository<Friend（实体类）,String（主键id）>接口
 */
public interface FriendDao extends JpaRepository<Friend,String> {

    //联合主键查询
    public Friend findByUseridAndFriendid(String userid,String friendid);

    //如果相互喜欢，把双方的islike都更新为1
    @Modifying
    @Query(value = "update f_friend f set f.islike=?1 where f.userid=?2 and f.friendid=?3",nativeQuery = true)
    public void updateIslike(String isike,String userid,String friendid);


    //根据userid和friendid删除当前用户的好友
    @Modifying
    @Query(value = "delete from f_friend where userid=? and friendid=?",nativeQuery = true)
    public void deleteFriend(String userid, String friendid);

    //根据friendid查询有哪些user关注了我
    public List<Friend> findByFriendid(String friendid);

    //根据userid查询当前登录用户关注了哪些人
    public List<Friend> findByUserid(String userid);
}
