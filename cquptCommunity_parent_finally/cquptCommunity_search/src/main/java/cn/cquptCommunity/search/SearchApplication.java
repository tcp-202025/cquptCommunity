package cn.cquptCommunity.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringBootApplication
@EnableEurekaClient  //启动eureka客户端
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class);//springboot的引导类，这样就可以扫描到与SearchApplication同级包子包下面的controller
    }

    @Bean
    public IdWorker idWorker(){  //id生生成器
        return new IdWorker();
    }
}
