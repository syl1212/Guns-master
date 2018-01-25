package com.thirdparty.rabbitMQ.vo.demo;

import com.alibaba.fastjson.JSONObject;
import com.thirdparty.rabbitMQ.base.MQRequestBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lagon
 * @time 2017/10/18 16:25
 * @description 结算结果通知请求主体
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EatingReqBody extends MQRequestBody {

    private String backStatus;//放款收回状态 2=确认收回成功 3=确认收回失败
    private String backPicUrl;//放款收回证据图片链接

    public static void main(String[] args) {
        EatingReqBody reqBody=new EatingReqBody();
        reqBody.setBackStatus("2");
        reqBody.setBackPicUrl("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1999993257,2634893920&fm=173&s=21A0E7B24E73448400083A6003007075&w=218&h=146&img.JPEG");
        EatingInformReq req = new EatingInformReq();
        req.setRequestBody(reqBody);
        req.setLoanCode("BJPHYB2017060100021");
        req.setEcho("FFFFFFFFFFFFFFFFFFFFF");
        System.out.println(JSONObject.toJSONString(req));
    }

}
