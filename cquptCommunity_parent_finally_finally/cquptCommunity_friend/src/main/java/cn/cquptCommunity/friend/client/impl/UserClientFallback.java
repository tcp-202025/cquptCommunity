package cn.cquptCommunity.friend.client.impl;

import cn.cquptCommunity.friend.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 定义熔断类：需要实现你声明的那个feign接口，然后去实现其中的方法，这些方法在熔断类中就是熔断方法
 */
@Component
public class UserClientFallback implements UserClient {
    @Autowired
    private HttpServletRequest request;

    @Override
    public void updateFansCountAndFollowCount(String userid, String friendid, int x) {
        request.setAttribute("error","服务器正忙，请稍后再试");
    }
}
