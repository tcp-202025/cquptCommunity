package cn.cquptCommunity.friend.client;

import cn.cquptCommunity.friend.client.impl.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * 声明一个feign接口，用于远程调用微服务模块
 */
@FeignClient(value = "cquptCommunity-user",fallback = UserClientFallback.class)//当用户点击关注之后，就远程调用user模块的更新粉丝数和更新关注数的方法
public interface UserClient {
    @PutMapping("/user/inc/{userid}/{friendid}/{x}")
    public void updateFansCountAndFollowCount(@PathVariable("userid") String userid, @PathVariable("friendid") String friendid, @PathVariable("x") int x);
}
