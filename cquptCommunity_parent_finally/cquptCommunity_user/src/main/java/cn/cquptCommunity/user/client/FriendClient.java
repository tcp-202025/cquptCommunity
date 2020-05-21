package cn.cquptCommunity.user.client;

import cn.cquptCommunity.user.client.impl.FriendClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 远程调用friend模块
 */
@Component
@FeignClient(value ="cquptCommunity-friend",fallback = FriendClientFallback.class)//1.指定微服务的名称，表明要调用哪个微服务,2.指定一个熔断类
public interface FriendClient {
    /**
     * 查询粉丝数：我关注了对方，我就成为了对方的粉丝
     * 如果对方想查看它的粉丝数，那么就可以根据friendid来查询哪些人关注了它
     */
    @GetMapping("/friend/findFans/{friendid}")
    public List<String> findFans(@PathVariable("friendid") String friendid);

    /**
     * 查询我的关注数：根据我的userid去查
     */
    @GetMapping("/friend/findFollow/{userid}")
    public List<String> findMyFollow(@PathVariable("userid") String userid);
}
