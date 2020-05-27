package cn.cquptCommunity.gathering.service;

import cn.cquptCommunity.gathering.dao.UsergathDao;
import cn.cquptCommunity.gathering.pojo.Gathering;
import cn.cquptCommunity.gathering.pojo.Usergath;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户参加活动的业务层
 */

@Service
@Transactional
public class UsergathService {

    @Autowired
    private UsergathDao usergathDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询当前登录的用户参加了哪些活动
     */
    public List<Usergath> findGathering(String userid) {
        return usergathDao.findByUserid(userid);
    }

    /**
     * 新参加活动
     */
    @Transactional
    public void joinGathering(String userid, String gathid) {
        Usergath usergath=new Usergath();
        usergath.setUserid(userid);//用户id
        usergath.setGathid(gathid);//活动id
        usergath.setExetime(new Date());//报名时间
        usergathDao.save(usergath);
    }

    /**
     * 取消/删除 参加的活动
     */
    public void delete(String userid, String gathid) {
        usergathDao.deleteGathering(userid,gathid);
    }

    /**
     * 后台管理功能：根据当前活动id查询参加者的具体信息
     */
    public List<Usergath> findJoinUsers(String gathid) {
        return usergathDao.findByGathid(gathid);
    }

    /**
     * 参加活动时发送验证码
     */
    public void sendSms_joinGathering(String mobile) {
        //1.生成六位随机数
		/*
		Random random=new Random();
		int max=999999;//最大数
		int min=100000;//最小数
		int code = random.nextInt(max);//随机生成
		if(code<min){
			code=code+min;
		}
		*/
        //使用apache提供的工具类直接生成
        String checkCode = RandomStringUtils.randomNumeric(6);//生成6位随机数
        //2.向缓存中存一份
        redisTemplate.opsForValue().set("joinCheckCode_"+mobile,checkCode,15, TimeUnit.MINUTES);//存入redis缓存中，<key,value>，key值需要唯一，设置过期时间为15分钟
        //3.给用户发一份
        Map<String,String> map=new HashMap<>();
        map.put("mobile",mobile);
        map.put("checkCode",checkCode);
        rabbitTemplate.convertAndSend("sms",map);//发送到消息队列中
    }


    /**
     * 取消参加活动时发送验证码
     */
    public void sendSms_deleteGathering(String mobile) {
        //使用apache提供的工具类直接生成验证码
        String checkCode = RandomStringUtils.randomNumeric(6);//生成6位随机数
        //2.向缓存中存一份
        redisTemplate.opsForValue().set("deleteCheckCode_"+mobile,checkCode,15, TimeUnit.MINUTES);//存入redis缓存中，<key,value>，key值需要唯一，设置过期时间为15分钟
        //3.给用户发一份
        Map<String,String> map=new HashMap<>();
        map.put("mobile",mobile);
        map.put("checkCode",checkCode);
        rabbitTemplate.convertAndSend("sms",map);//发送到消息队列中
    }

    /**
     * 根据当前登录用户的id和活动id查询用户是否查询
     */
    public Usergath findByUseridAndGathid(String userid, String gathid) {
        return usergathDao.findByUseridAndGathid(userid,gathid);
    }
}
