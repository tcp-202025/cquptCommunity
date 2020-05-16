package cn.cquptCommunity.spit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringCloudApplication
public class SpitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpitApplication.class);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker();
	}
	
}
