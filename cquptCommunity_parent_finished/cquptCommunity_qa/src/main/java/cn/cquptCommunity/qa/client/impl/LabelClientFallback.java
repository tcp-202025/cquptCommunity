package cn.cquptCommunity.qa.client.impl;

import cn.cquptCommunity.qa.client.LabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * 定义熔断类：需要实现你声明的那个feign接口，然后去实现其中的方法，这些方法在熔断类中就是熔断方法
 */
@Component
public class LabelClientFallback implements LabelClient {
    @Override
    public Result findById(String labelId) {
        return new Result(false, StatusCode.ERROR,"服务器正忙，请稍后再试");
    }
}
