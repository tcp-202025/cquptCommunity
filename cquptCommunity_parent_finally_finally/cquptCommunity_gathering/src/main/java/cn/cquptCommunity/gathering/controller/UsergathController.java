package cn.cquptCommunity.gathering.controller;

import cn.cquptCommunity.gathering.client.UserClient;
import cn.cquptCommunity.gathering.pojo.Gathering;
import cn.cquptCommunity.gathering.pojo.Usergath;
import cn.cquptCommunity.gathering.service.GatheringService;
import cn.cquptCommunity.gathering.service.UsergathService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;
import util.MailUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户参加活动的控制器
 */

@RestController
@CrossOrigin
@RequestMapping("/usergath")
public class UsergathController {

    @Autowired
    private UsergathService usergathService;

    @Autowired
    private GatheringService gatheringService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;//生成token和解析token的工具类

    @Autowired
    private MailUtils mailUtils;//发送邮件的工具类

    @Autowired
    private UserClient userClient;//远程调用user微服务

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 根据当前登录用户的id和活动id查询用户是否查询
     */
    @GetMapping("isjoin/{gathid}")
    public Result isJoin(@PathVariable String gathid){
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        //否则token不为空，表示有权限
        Claims claims = jwtUtil.parseJWT(token);//解析token
        String userid = claims.getId();//拿到userid
        Usergath usergath= usergathService.findByUseridAndGathid(userid,gathid);
        if(usergath!=null&&!"".equals(usergath)){
            return new Result(false,StatusCode.REPERROR,"您已经参加该活动");
        }
        return new Result(true,StatusCode.OK,"未参加");
    }

    /**
     * 根据当前用户的id查询用户参加的活动
     */
    @GetMapping()
    public Result findGathering(){
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        //否则token不为空，表示有权限
        Claims claims = jwtUtil.parseJWT(token);//解析token
        String userid = claims.getId();//拿到userid
        List<Usergath> usergaths=usergathService.findGathering(userid);//拿到用户参加的活动列表
        List<Gathering> gatherings=new ArrayList<>();
        for (Usergath usergath : usergaths) {
            Gathering gathering = gatheringService.findById(usergath.getGathid());//拿到活动id，然后去gathering表中查询详细信息
            gatherings.add(gathering);
        }
        return new Result(true,StatusCode.OK,"查询成功",gatherings);
    }

    @GetMapping("/{nickname}")
    public Result findGatheringByNickname(@PathVariable String nickname){
        return new Result(true,StatusCode.OK,"查询成功");
    }

    /**
     * 参加活动时发送验证码
     */
    @PostMapping("/joingathering/sendsms/{mobile}")
    public Result sendSms_joinGathering(@PathVariable String mobile){
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        //否则token不为空，表示有权限
        Claims claims = jwtUtil.parseJWT(token);//解析token
        String userid = claims.getId();
        String phoneNumber = userClient.findPhoneNumber(userid);
        usergathService.sendSms_joinGathering(phoneNumber);
        return new Result(true,StatusCode.OK,"发送成功");
    }

    /**
     * 参加活动
     */
    @PostMapping("/{gathid}/{mobile}/{checkcode}")
    public Result joinGathering(@PathVariable String gathid,@PathVariable String mobile,@PathVariable String checkcode){
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        //否则token不为空，表示有权限
        //获取缓存中的验证码
        String checkCode_redis=(String) redisTemplate.opsForValue().get("joinCheckCode_"+mobile);
        if(checkCode_redis==null){
            return new Result(false,StatusCode.ERROR,"请先获取手机验证码");
        }
        if(!checkCode_redis.equals(checkcode)){
            return new Result(false,StatusCode.ERROR,"验证码错误");
        }
        //第一步：像用户的活动表中添加数据
        Claims claims = jwtUtil.parseJWT(token);//解析token
        String userid = claims.getId();//拿到userid
        usergathService.joinGathering(userid,gathid);
        //第二步：像报名成功的用户账号中发送邮件
        Gathering gathering = gatheringService.findById(gathid);//拿到活动详情
        String email = userClient.findEmail(userid);//拿到用户的邮箱
        mailUtils.sendMail(email,"尊敬的用户，您已经成功报名参加"+"“"+gathering.getName()+"”"+"！活动时间:"+gathering.getStarttime()+"——"+gathering.getEndtime()+"，举办地点："+gathering.getAddress()+"，活动详情："+gathering.getSummary()+"。","报名邮件");
        //第三步：更新活动表中的参与人数字段
        gatheringService.updateJoinCount(1,gathid);
        //第四步：发短信通知用户报名成功
        Map<String,String> map=new HashMap<>();
        String phoneNumber = userClient.findPhoneNumber(userid);
        map.put("mobile",phoneNumber);
        map.put("gatheringId",gathid);
        rabbitTemplate.convertAndSend("sms_join",map);//发送到消息队列中
        return new Result(true,StatusCode.OK,"参加成功");
    }


    /**
     * 取消参加活动时发送的验证码
     */
    @PostMapping("/deletegathering/sendsms/{mobile}")
    public Result sendSms_deleteGathering(@PathVariable String mobile){
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        Claims claims = jwtUtil.parseJWT(token);//解析token
        String userid = claims.getId();
        String phoneNumber = userClient.findPhoneNumber(userid);
        usergathService.sendSms_deleteGathering(phoneNumber);
        return new Result(true,StatusCode.OK,"发送成功");
    }

    /**
     * 取消参加活动
     */
    @DeleteMapping("/{gathid}/{mobile}/{checkcode}")
    public Result delete(@PathVariable String gathid,@PathVariable String checkcode,@PathVariable String mobile){
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        //否则token不为空，表示有权限
        //第一步：验证用户输入的验证码
        //获取缓存中的验证码
        String checkCode_redis=(String) redisTemplate.opsForValue().get("deleteCheckCode_"+mobile);
        if(checkCode_redis==null){
            return new Result(false,StatusCode.ERROR,"请先获取手机验证码");
        }
        if(!checkCode_redis.equals(checkcode)){
            return new Result(false,StatusCode.ERROR,"验证码错误");
        }
        Claims claims = jwtUtil.parseJWT(token);//解析token
        String userid = claims.getId();//拿到userid
        //第二步：删除活动表中的相关数据
        usergathService.delete(userid,gathid);
        //第三步：更新活动表中的参与人数字段
        gatheringService.updateJoinCount(-1,gathid);
        //第四步：发送短信告知用户：已取消参加活动
        Map<String,String> map=new HashMap<>();
        String phoneNumber = userClient.findPhoneNumber(userid);
        map.put("mobile",phoneNumber);
        map.put("gatheringId",gathid);
        rabbitTemplate.convertAndSend("sms_cancel",map);//发送到消息队列中
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 后台管理功能：根据当前活动id查询参加者的具体信息
     */
    @GetMapping("/joinuser/{gathid}")
    public Result findUserByGathid(@PathVariable String gathid){
        //需要判断是否是admin登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_admin");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //否则token不为空，表示有权限
        List<Usergath> usergaths= usergathService.findJoinUsers(gathid);
        List<Result> results=new ArrayList<>();
        for (Usergath usergath : usergaths) {
            String userid = usergath.getUserid();//拿到参与用户的id
            Result user = userClient.findById(userid);
            System.out.println(user);
            results.add(user);
        }
        return new Result(true,StatusCode.OK,"查询成功",results);
    }
}
