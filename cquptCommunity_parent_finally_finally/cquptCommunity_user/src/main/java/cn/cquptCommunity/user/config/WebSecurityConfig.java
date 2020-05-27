package cn.cquptCommunity.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 编写一个安全配置类：指定访问权限
 * 我们在添加了spring security依赖后，所有的地址都会被spring security所拦截，
 * 如果不覆盖原有的默认配置，则表示所有都不能访问。所以我们需要编写安全配置类来覆盖掉原有的配置，自定义指定访问权限
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * authorizeRequests：所有security全注解配置实现的开端，表示开始说明需要的权限。
         *    需要的权限分两部分，第一部分是拦截的路径，第二部分访问该路径需要的权限。
         *      antMatchers表示拦截的路径，permitAll()表示任何权限都可以访问，即直接放行所有。
         *      anyRequest表示任何的请求，authenticated()表示认证后才能访问
         *      .and().csrf().disable();固定写法，表示使csrf拦截失效。(csrf：跨站请求伪造，是一种恶意网站攻击技术)
         */

        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
