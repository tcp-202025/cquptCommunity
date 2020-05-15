package cn.cquptCommunity.user;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import util.IdWorker;
import util.JwtUtil;

@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class);
	}

	@Bean
	public IdWorker idWorkker(){
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
