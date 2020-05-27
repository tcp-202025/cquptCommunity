package cn.cquptCommunity.gathering;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
import util.JwtUtil;
import util.MailUtils;

@SpringBootApplication
@EnableEurekaClient  //启动eureka客户端
@EnableCaching //表示要使用springboot的cache缓存
@EnableFeignClients //feign客户端，启动feign组件
@EnableDiscoveryClient //发现服务
public class GatheringApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatheringApplication.class);
	}

	@Bean
	public IdWorker idWorker(){ //生成id的工具类
		return new IdWorker();
	}

	@Bean
	public JwtUtil jwtUtil(){ //生成token和解析token的工具类
		return new JwtUtil();
	}

	@Bean
	public MailUtils mailUtils(){
		return new MailUtils();
	}
}
