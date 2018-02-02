package com.thirdparty.rabbitmq.vo.demo;

import com.thirdparty.rabbitmq.base.MQApiIdentifier;
import com.thirdparty.rabbitmq.base.MQReqMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lagon
 * @time 2017/10/19 11:41
 * @description 结算结果推送请求实体
 */
@Getter
@Setter
@ToString
public class EatingInformReq extends MQReqMessage<EatingReqBody> {

    public EatingInformReq(){
        mqApiIdentifier= MQApiIdentifier.SETTLEMENT_RESULT_INFORM;
        apiIdentifier= MQApiIdentifier.SETTLEMENT_RESULT_INFORM.getMark();
    }

}
