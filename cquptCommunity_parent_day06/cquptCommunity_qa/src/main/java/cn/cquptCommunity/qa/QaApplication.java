package cn.cquptCommunity.qa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
import util.JwtUtil;

@SpringBootApplication
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
