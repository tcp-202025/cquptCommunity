package cn.cquptCommunity.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import util.JwtUtil;

@SpringBootApplication
@EnableEurekaClient //启动eureka的客户端
@EnableZuulProxy //启动zuul组件
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil(); //生成Token和解析Token的工具类
    }
}
