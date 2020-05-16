package cn.cquptCommunity.sms.utils;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.stereotype.Component;


/**
 * 发送短信的工具类：使用的是阿里的云短信服务
 */
@Component
public class SmsUtil {
    public void sendSms(String mobile,String checkCode){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4G1hR7rnBTnmuEM9YMFV", "3tzNfgS4rBDHtf6ZHwIfzlMp2YF5LA");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers",mobile);
        request.putQueryParameter("SignName", "cquptBar");
        request.putQueryParameter("TemplateCode", "SMS_190266056");
        request.putQueryParameter("TemplateParam", "{\"checkCode\":\""+checkCode+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
