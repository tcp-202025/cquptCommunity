package cn.cquptCommunity.gathering;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
@SpringBootApplication
@EnableCaching //表示要使用springboot的cache缓存
public class GatheringApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatheringApplication.class);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker();
	}
	
}
