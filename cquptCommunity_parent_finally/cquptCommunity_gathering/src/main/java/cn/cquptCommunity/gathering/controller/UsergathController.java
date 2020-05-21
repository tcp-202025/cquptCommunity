package cn.cquptCommunity.gathering.controller;

import cn.cquptCommunity.gathering.pojo.Gathering;
import cn.cquptCommunity.gathering.pojo.Usergath;
import cn.cquptCommunity.gathering.service.GatheringService;
import cn.cquptCommunity.gathering.service.UsergathService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 参加活动
     */
    @PostMapping("/{gathid}")
    public Result joinGathering(@PathVariable String gathid){
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        //否则token不为空，表示有权限
        Claims claims = jwtUtil.parseJWT(token);//解析token
        String userid = claims.getId();//拿到userid
        usergathService.joinGathering(userid,gathid);
        return new Result(true,StatusCode.OK,"参加成功");
    }

    /**
     * 取消参加活动
     */
    @DeleteMapping("/{gathid}")
    public Result delete(@PathVariable String gathid){
        //需要判断用户是否登录
        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录，没有权限
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        //否则token不为空，表示有权限
        Claims claims = jwtUtil.parseJWT(token);//解析token
        String userid = claims.getId();//拿到userid
        usergathService.delete(userid,gathid);
        return new Result(true,StatusCode.OK,"删除成功");
    }
}
