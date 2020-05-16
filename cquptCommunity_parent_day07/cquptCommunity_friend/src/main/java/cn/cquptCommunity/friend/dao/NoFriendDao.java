package cn.cquptCommunity.friend.dao;

import cn.cquptCommunity.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 交友的dao（添加不喜欢的人）:使用的是springDataJPA,所以需要继承JpaRepository<NoFriend（实体类）,String（主键id）>接口
 */
public interface NoFriendDao extends JpaRepository<NoFriend,String> {
    //联合主键查询
    public NoFriend findByUseridAndFriendid(String userid, String friendid);

    //根据userid和friendid删除非好友表中的记录
    @Modifying
    @Query(value = "delete from f_nofriend where userid=? and friendid=?",nativeQuery = true)
    public void deleteNoFriend(String userid, String friendid);
}
