package cn.cquptCommunity.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * springboot的引导类
 */
@SpringBootApplication
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class);//这样就可以访问到引导类BaseApplication同级包或者子级包中的controller
    }

    @Bean  //此注解表示将方法的的返回值注入容器
    public IdWorker getIdWorker(){
        //执行该方法会自动生成id
        return new IdWorker();
    }
}
