package cn.cquptCommunity.user.client.impl;

import cn.cquptCommunity.user.client.FriendClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义熔断类：需要实现你声明的那个feign接口，然后去实现其中的方法，这些方法在熔断类中就是熔断方法
 */
@Component
public class FriendClientFallback implements FriendClient {

    @Override
    public List<String> findFans(String friendid) {
        List<String> list= new ArrayList<>();
        list.add("服务器正忙，请稍后再试");
        return list;
    }

    @Override
    public List<String> findMyFollow(String userid) {
        List<String> list= new ArrayList<>();
        list.add("服务器正忙，请稍后再试");
        return list;
    }

}
