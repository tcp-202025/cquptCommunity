package cn.cquptCommunity.user;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import util.IdWorker;
import util.JwtUtil;

@SpringBootApplication
@EnableDiscoveryClient  //发现服务
@EnableEurekaClient //启动eureka客户端
@EnableFeignClients //feign客户端，启动feign组件
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class);
	}

	@Bean
	public IdWorker idWorkker(){  //id生成器
		return new IdWorker();
	}

	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder(){ //BCrypt密码加密的部分：使用BCryptPasswordEncoder来进行密码加密
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtUtil jwtUtil(){ //生成Token和解析Token的工具类
		return new JwtUtil();
	}

}
