package cn.cquptCommunity.gathering.service;

import cn.cquptCommunity.gathering.dao.UsergathDao;
import cn.cquptCommunity.gathering.pojo.Gathering;
import cn.cquptCommunity.gathering.pojo.Usergath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户参加活动的业务层
 */

@Service
@Transactional
public class UsergathService {

    @Autowired
    private UsergathDao usergathDao;

    /**
     * 查询当前登录的用户参加了哪些活动
     */
    public List<Usergath> findGathering(String userid) {
        return usergathDao.findByUserid(userid);
    }

    /**
     * 新参加活动
     */
    public void joinGathering(String userid, String gathid) {
        Usergath usergath=new Usergath();
        usergath.setUserid(userid);
        usergath.setGathid(gathid);
        usergath.setExetime(new Date());
        usergathDao.save(usergath);
    }

    /**
     * 取消/删除 参加的活动
     */
    public void delete(String userid, String gathid) {
        usergathDao.deleteGathering(userid,gathid);
    }
}
