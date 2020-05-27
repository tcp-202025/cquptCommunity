package cn.cquptCommunity.qa.client;

import cn.cquptCommunity.qa.client.impl.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 声明一个feign接口，用于远程调用微服务模块
 */
@Component
@FeignClient(value = "cquptCommunity-user",fallback = UserClientFallback.class)//调用user微服务中的方法查询user信息
public interface UserClient {

    /**
     * 根据用户id查询用户昵称
     */
    @GetMapping("/user/getnickname/{userid}")
    public String findNickname(@PathVariable String userid);
}
