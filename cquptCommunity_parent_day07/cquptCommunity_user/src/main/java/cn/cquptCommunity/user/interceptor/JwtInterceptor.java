package cn.cquptCommunity.user.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 配置一个拦截器：将权限判断的操作放在拦截器中
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 在方法执行前拦截
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");//拿到请求头信息

        if(header!=null&&!"".equals(header)){
            //如果包含有头信息，就解析Token，进行权限的判断
            if(header.startsWith("Bearer ")){
                //如果传入的是符合约定的头信息
                String token = header.substring(7);//从第7位截取，拿到Token
                try {
                    //解析token:如果解析通过，就将令牌中的参数值存入request域中，
                    // 在业务层如果是admin来访问，就可以获取到claims_admin，表明有删除权限；如果是user来访问，在request域中拿到的就是claims_user，表明没有删除权限
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles=(String) claims.get("roles");
                    if(roles!=null&&roles.equals("admin")){
                        request.setAttribute("claims_admin",token);
                    }
                    if(roles!=null&&roles.equals("user")){
                        request.setAttribute("claims_user",token);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("令牌不正确");
                }
            }
        }

        return true;//放行
    }

}
