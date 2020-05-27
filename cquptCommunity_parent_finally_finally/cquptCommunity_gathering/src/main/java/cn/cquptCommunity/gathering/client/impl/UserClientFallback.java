package cn.cquptCommunity.gathering.client.impl;

import cn.cquptCommunity.gathering.client.UserClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * 定义熔断类：需要实现你声明的那个feign接口，然后去实现其中的方法，这些方法在熔断类中就是熔断方法
 */
@Component
public class UserClientFallback implements UserClient {

    @Override
    public Result findById(String id) {
         return new Result(false, StatusCode.REMOTEERROR,"服务器正忙，请稍后再试");
    }

    @Override
    public String findEmail(String userid) {
        return "服务器正忙，请稍后再试";
    }

    @Override
    public String findPhoneNumber(String userid) {
        return "服务器正忙，请稍后再试";
    }

    @Override
    public String findUserId(String nickname) {
        return "服务器正忙，请稍后再试";
    }

}
