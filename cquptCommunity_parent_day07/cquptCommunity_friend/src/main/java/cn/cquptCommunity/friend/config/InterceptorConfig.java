package cn.cquptCommunity.friend.config;

import cn.cquptCommunity.friend.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtInterceptor jwtInterceptor;//自定义的拦截器

    //注册拦截器
    protected void addInterceptors(InterceptorRegistry registry) {
        //声明拦截器对象和要拦截的请求:登录的路径不能拦截
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login/**");
    }

}
