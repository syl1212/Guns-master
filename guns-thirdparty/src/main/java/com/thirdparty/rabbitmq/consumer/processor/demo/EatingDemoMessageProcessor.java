package com.thirdparty.rabbitmq.consumer.processor.demo;

import com.alibaba.fastjson.JSONObject;
import com.thirdparty.rabbitmq.base.MQConstants;
import com.thirdparty.rabbitmq.base.MQProcessingException;
import com.thirdparty.rabbitmq.base.MQTransStatus;
import com.thirdparty.rabbitmq.base.ResDetail;
import com.thirdparty.rabbitmq.conditional.DemoConsumerCondition;
import com.thirdparty.rabbitmq.util.DateUtil;
import com.thirdparty.rabbitmq.vo.demo.EatingInformReq;
import com.thirdparty.rabbitmq.vo.demo.EatingInformResultRes;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lagon
 * @time 2017/10/19 11:16
 * @description 普惠运营>>MQ服务>>结算结果通知处理器
 */
@Slf4j
@Component
@Conditional(DemoConsumerCondition.class)
public class EatingDemoMessageProcessor implements DemoMessageProcessor<EatingInformReq> {

    @Value("${rabbitmq.demo.trans.req.routingkey}")
    private String routingKey;

//    @Autowired
//    private EatingResultMessageProducer eatingResultMessageProducer;

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Override
    public String getDataTransferFormat() {
        return MQConstants.DATA_TRANSFER_FORMAT_JSON;
    }

    @Override
    public Class<EatingInformReq> getTClass() {
        return EatingInformReq.class;
    }

    @Override
    public Set<String> getProcessedRoutingKeys() {
        return new HashSet<String>() {
            {
                add(routingKey);
            }
        };
    }

    @Override
    public ResDetail process(MessageProperties messageProperties, EatingInformReq message) {
        log.info("处理吃饭开始，时间：{}", DateUtil.getStandardCurrentTime());
        EatingInformResultRes resMessage=dozerBeanMapper.map(message,EatingInformResultRes.class);
        try {
            System.out.println(JSONObject.toJSONString(resMessage));
        } catch (Exception e) {
            resMessage.setResponseCode(MQTransStatus.ERROR.name());
            resMessage.setResponseMessage(MQTransStatus.ERROR.getDesc());
//            eatingResultMessageProducer.sendMessage(resMessage);
            throw new MQProcessingException("处理吃饭通知异常",e);
        }
        //成功后响应
        resMessage.setResponseCode(MQTransStatus.SUCCESS.name());
        resMessage.setResponseMessage(MQTransStatus.SUCCESS.getDesc());
//        eatingResultMessageProducer.sendMessage(resMessage);
        log.info("完成吃饭，时间：{}", DateUtil.getStandardCurrentTime());
        return new ResDetail(true, "success");
    }
}
