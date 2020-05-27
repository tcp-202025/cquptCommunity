package cn.cquptCommunity.friend.service;

import cn.cquptCommunity.friend.dao.FriendDao;
import cn.cquptCommunity.friend.dao.NoFriendDao;
import cn.cquptCommunity.friend.pojo.Friend;
import cn.cquptCommunity.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 交友模块的业务层
 */

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao; //添加喜欢的人做好友

    @Autowired
    private NoFriendDao noFriendDao;//添加不喜欢的人（非好友）

    /**
     * 添加好友
     */
    public int addFriend(String userid, String friendid) {
        //1.判断：好友表中的userid到friendid方向的type是否有数据，有就意味着重复添加（查询当前登录的用户想要添加的好友是否已经添加）
        Friend resultFriend = friendDao.findByUseridAndFriendid(userid, friendid);
        if(resultFriend!=null){
            return 0;//已经添加过了，返回0，表示重复添加
        }
        //2.如果之前没有过好友，那就添加好友：让好友表中的userid到friendid方向的type为0
        Friend friend=new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        //如果两人曾是非好友，在添加好友后，就需要删除非好友中的记录
        noFriendDao.deleteNoFriend(userid,friendid);
        //3.判断好友表中的friendid到userid方向的type是否有数据，如果有，把双方的状态都改为1（判断被添加的那个好友中是否有这个添加者)
        if(friendDao.findByUseridAndFriendid(friendid, userid)!=null){
            //如果相互喜欢，把双方的islike都更新为1
            friendDao.updateIslike("1",userid,friendid);
            friendDao.updateIslike("1",friendid,userid);
        }
        return 1;//返回1表示添加成功
    }


    /**
     * 添加非好友
     */
    public int addNoFriend(String userid, String friendid) {
        //1.先判断是否已经是非好友
        NoFriend resultNoFriend = noFriendDao.findByUseridAndFriendid(userid, friendid);
        if(resultNoFriend!=null){
            //如果已经是非好友了，就返回0，表示不能重复添加
            return 0;
        }
        NoFriend noFriend=new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
        return 1;//返回1表示添加成功
    }

    /**
     * 删除好友列表中的好友
     */
    public void deleteFriend(String userid, String friendid) {
        //删除当前用户的表中数据
        friendDao.deleteFriend(userid,friendid);
        //更新好友表中的islike为0
        friendDao.updateIslike("0",friendid,userid);
        //向非好友表中添加数据
        NoFriend noFriend=new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    /**
     * 查询粉丝数：我关注了对方，我就成为了对方的粉丝
     * 如果对方想查看它的粉丝数，那么就可以根据friendid来查询哪些人关注了它
     */
    public List<Friend> findFans(String friendid) {
        return friendDao.findByFriendid(friendid);
    }

    /**
     * 查询我的关注数：根据我的userid去查
     */
    public List<Friend> findMyFollow(String userid) {
        return friendDao.findByUserid(userid);
    }
}
