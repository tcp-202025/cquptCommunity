package cn.cquptCommunity.article;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
import util.JwtUtil;

@SpringBootApplication
@EnableEurekaClient  //启动eureka客户端
@EnableFeignClients //feign客户端，启动feign组件
@EnableDiscoveryClient //发现服务
public class ArticleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticleApplication.class);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker();
	}

	@Bean
	public JwtUtil jwtUtil(){ //生成Token和解析Token的工具类
		return new JwtUtil();
	}
}
