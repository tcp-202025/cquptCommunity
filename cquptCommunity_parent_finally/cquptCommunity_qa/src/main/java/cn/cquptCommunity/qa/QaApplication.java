package cn.cquptCommunity.qa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
import util.JwtUtil;

@SpringBootApplication
@EnableDiscoveryClient  //发现微服务
@EnableFeignClients  //启动feign组件
@EnableEurekaClient //启动eureka客户端
public class QaApplication {

	public static void main(String[] args) {
		SpringApplication.run(QaApplication.class);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker();
	}

	@Bean
	public JwtUtil jwtUtil(){
		return new JwtUtil();
	}
}
