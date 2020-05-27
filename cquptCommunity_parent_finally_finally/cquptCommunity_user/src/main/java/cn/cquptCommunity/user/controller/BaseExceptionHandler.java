package cn.cquptCommunity.user.controller;
import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 统一异常处理类
 * @ControllerAdvice 表示是一个增强的controller
 * 可以实现三个方面的功能：
 *      全局异常处理
 *      全局数据绑定
 *      全局数据预处理
 */
@ControllerAdvice //配合@ExceptionHandler使用。当将异常抛到controller时,可以对异常进行统一处理,规定返回的json格式
public class BaseExceptionHandler {
	
    @ExceptionHandler(value = Exception.class) //指明要处理的异常类型
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();        
        return new Result(false, StatusCode.ERROR, "执行出错");
    }
}
