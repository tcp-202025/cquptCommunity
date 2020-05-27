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

    private  DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4G1hR7rnBTnmuEM9YMFV", "3tzNfgS4rBDHtf6ZHwIfzlMp2YF5LA");

    //发送验证码
    public void sendSms1(String mobile,String checkCode){
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

    /**
     * 用户成功参加活动后向用户发送通知短信
     */
    public void sendSms2(String mobile, String gatheringId){
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "cquptBar");
        request.putQueryParameter("TemplateCode", "SMS_190729059");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+gatheringId+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户取消参加活动时向用户发送通知短信
     */
    public void sendSms3(String mobile,String gatheringId) {
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers",mobile);
        request.putQueryParameter("SignName", "cquptBar");
        request.putQueryParameter("TemplateCode", "SMS_190729034");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+gatheringId+"\"}");
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
