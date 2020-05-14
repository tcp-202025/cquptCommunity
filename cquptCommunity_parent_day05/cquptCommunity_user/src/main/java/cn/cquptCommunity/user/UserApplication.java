package cn.cquptCommunity.user;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker();
	}
	
}
