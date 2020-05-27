package cn.cquptCommunity.gathering.client;

import cn.cquptCommunity.gathering.client.impl.UserClientFallback;
import entity.Result;
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
     * 根据userid查询信息
     */
    @GetMapping(value="/user/{id}")
    public Result findById(@PathVariable("id") String id);


    /**
     * 根据用户id查询邮箱
     */
    @GetMapping("/user/getemail/{userid}")
    public String findEmail(@PathVariable String userid);

    /**
     * 根据用户id查询手机号
     */
    @GetMapping("/user/getphonenumber/{userid}")
    public String findPhoneNumber(@PathVariable String userid);

    /**
     * 根据用户昵称查询userid
     */
    @GetMapping("/user/findByNickNname/{nickname}")
    public String findUserId(@PathVariable String nickname);
}
