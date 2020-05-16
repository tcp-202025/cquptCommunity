package cn.cquptCommunity.spit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
@SpringCloudApplication
public class ArticleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticleApplication.class);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker();
	}
	
}
