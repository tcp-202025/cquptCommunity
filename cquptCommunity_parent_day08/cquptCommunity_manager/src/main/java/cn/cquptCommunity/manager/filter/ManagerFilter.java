package cn.cquptCommunity.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 定义过滤器
 * 在zuul中为我们提供了一个顶级接口IZuulFilter，如果想要定义过滤器的话，就需要去实现该接口。而其中已经实现好的抽象类ZuulFilter是过滤器的顶级父类。
 */
@Component
public class ManagerFilter extends ZuulFilter {
    /**
     * shouldFilter :返回一个Boolean值，判断该过滤器是否需要执行。返回true执行过滤器中的run方法，返回false不执行。
     * run:过滤器的具体业务逻辑。
     * filterType :返回字符串，代表过滤器的类型。包含以下4种:
     *     *pre :请求在被路由之前执行
     *     *route :在路由请求时调用
     *     *post :在route和error过滤器之后调用
     *     *error :处理请求时发生错误调用
     * filterOrder :通过返回的int值来定义过滤器的执行顺序，数字越小优先级越高。
     */


    @Autowired
    private JwtUtil jwtUtil; //生成Token和解析Token的工具类

    @Override
    public String filterType() {
        return "pre"; //在方法执行前执行过滤器
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的操作:
     * return任何object的值都表示继续执行
     * 如果想不继续执行：可以获取context上下文对象，然后设置为false
     * RequestContext context=RequestContext.getCurrentContext();
     * context.setSendZuulResponse(false)
     */
    @Override
    public Object run() throws ZuulException {
        //经过zuul网关会造成头信息的丢失，所以我们需要在过滤器中接收header，转发给微服务
        //1.初始化context上下文对象(使用的是zuul网关中的RequestContext。)：上下文可以理解为就是把一堆东西整合在一起，可以理解为环境，类似于语境
        RequestContext context= RequestContext.getCurrentContext();
        //2.获取request域
        HttpServletRequest request = context.getRequest();
        /**
         * 特殊方法直接放行
         */
        if(request.getMethod().equals("OPTIONS")){   //OPTIONS方法是zuul网关内部负责分发的方法
            return null;
        }
        String url=request.getRequestURL().toString();
        if(url.indexOf("/admin/login")>0){  //url.indexOf("x")  判断字符串是否包含x，如果包含，则返回在字符串中的位置，如果不包含，则返回0
            return null;
        }



        //3.拿到头信息
        String header = request.getHeader("Authorization");
        if(header!=null&&!"".equals(header)){
            if(header.startsWith("Bearer ")){
                String token=header.substring(7);//传入的header的格式是：Bearer xxxxxxxxxxxx，所以从第7位截取到末尾的就是token的字符串
                try {
                    //解析Token
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles=(String) claims.get("roles");//通过key拿到令牌中存储的信息
                    if(roles!=null&&roles.equals("admin")){//如果该访问的用户所携带的令牌中的roles是admin，表示是admin在访问，那就转发头信息并且放行
                        context.addZuulRequestHeader("Authorization",header);//转发给微服务
                        return null;//表示继续执行
                    }
                } catch (Exception e) {
                    context.setSendZuulResponse(false);//如果有错，则终止运行
                }
            }
        }
        context.setSendZuulResponse(false);//如果没有进入上面的if分支，表示头信息为空，则终止运行
        context.setResponseStatusCode(403);//403权限不足
        context.setResponseBody("权限不足");
        context.getResponse().setContentType("text/html;charset=utf-8");
        return null;
    }
}
