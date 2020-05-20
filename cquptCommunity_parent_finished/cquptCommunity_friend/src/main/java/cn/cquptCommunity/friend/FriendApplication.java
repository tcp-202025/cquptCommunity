package cn.cquptCommunity.friend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.JwtUtil;

@SpringBootApplication
@EnableEurekaClient //启动eureka的客户端
@EnableDiscoveryClient //发现服务
@EnableFeignClients //feign客户端，启动feign组件
public class FriendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FriendApplication.class);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil(); //生成token的工具类
    }

}
