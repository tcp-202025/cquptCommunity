package cn.cquptCommunity.qa.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 使用feign组件来调用base模块
 */
@FeignClient(value = "cquptCommunity-base")//1.指定微服务的名称，表明要调用哪个微服务
public interface LabelClient {

    //要调用哪个微服务的方法，在feign接口中就参照那个方法去写就可以了
    @GetMapping("/label/{labelId}")
    public Result findById(@PathVariable("labelId") String labelId);

}

