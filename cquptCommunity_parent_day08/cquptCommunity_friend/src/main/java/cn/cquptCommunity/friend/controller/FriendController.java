package cn.cquptCommunity.friend.controller;

import cn.cquptCommunity.friend.client.UserClient;
import cn.cquptCommunity.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FriendService friendService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserClient userClient;

    /**
     * 添加好友
     * @param friendid 对方用户ID
     * @param type 1：喜欢 0：不喜欢
     */
    @PutMapping("/like/{friendid}/{type}")
    public Result addFriend(@PathVariable String friendid,@PathVariable String type){
        //验证是否登录，并且拿到当前登录的用户的id

        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录或者不是user登录，都说明没有权限
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //否则token不为空，表示有权限
        Claims claims = jwtUtil.parseJWT(token); //解析用户访问时随身携带的token
        String userid = claims.getId();//当前登录用户的id
        //判断是添加喜欢还是不喜欢
        if(type!=null) {
            if(type.equals("1")){
                //添加的是喜欢
                int flag=friendService.addFriend(userid,friendid);
                if(flag==0){//表示重复添加
                    return new Result(false,StatusCode.ERROR,"不能重复添加好友");
                }
            }else{
                //如果是不喜欢的，则添加进非好友列表
                int flag=friendService.addNoFriend(userid,friendid);
                if(flag==0){//表示重复添加
                    return new Result(false,StatusCode.ERROR,"不能重复添加非好友");
                }
            }
        }
        //以上情况都没发生，表示添加成功
        userClient.updateFansCountAndFollowCount(userid,friendid,1);//更新用户的关注数和好友的粉丝数,点击关注后，该用户的关注数加一，好友的粉丝数加一
        return new Result(true,StatusCode.OK,"添加成功");
    }


    /**当前用户删除好友列表中的好友*/
    @DeleteMapping("/{friendid}")
    public Result deleteFriend(@PathVariable String friendid){
        //验证是否登录，并且拿到当前登录的用户的id

        //上述权限的判断交由拦截器帮我们处理,我们只需要直接拿到处理结果
        String token=(String) request.getAttribute("claims_user");
        if(token==null|| "".equals(token)){ //token为空，表示未登录或者不是user登录，都说明没有权限
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //否则token不为空，表示有权限
        Claims claims = jwtUtil.parseJWT(token); //解析用户访问时随身携带的token
        String userid = claims.getId();//当前登录用户的id
        friendService.deleteFriend(userid,friendid);
        userClient.updateFansCountAndFollowCount(userid,friendid,-1);//更新用户的关注数和好友的粉丝数,删除好友后后，该用户的关注数减一，好友的粉丝数减一
        return new Result(true,StatusCode.OK,"删除成功");
    }

}
