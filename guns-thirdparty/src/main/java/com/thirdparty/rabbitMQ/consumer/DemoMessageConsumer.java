package com.thirdparty.rabbitMQ.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.thirdparty.rabbitMQ.base.MQConstants;
import com.thirdparty.rabbitMQ.base.MQMessage;
import com.thirdparty.rabbitMQ.base.MQProcessingException;
import com.thirdparty.rabbitMQ.base.ResDetail;
import com.thirdparty.rabbitMQ.conditional.DemoConsumerCondition;
import com.thirdparty.rabbitMQ.config.RabbitDemoConfig;
import com.thirdparty.rabbitMQ.consumer.processor.demo.DemoMessageProcessor;
import com.thirdparty.rabbitMQ.util.XStreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author syl
 * @description 测试MQ消费者
 */
@Slf4j
@Component
@Conditional(DemoConsumerCondition.class)
public class DemoMessageConsumer {

    //消息处理器容器
    private static final Map<String,DemoMessageProcessor> messageProcessorContainer=new ConcurrentHashMap<String,DemoMessageProcessor>();

    @Value("${rabbitmq.demo.address}")
    private String serverAddress;

    @Value("${rabbitmq.demo.trans.exchange.name}")
    private String transExchange;

    @Autowired
    public void setMessageProcessors(DemoMessageProcessor[] messageProcessors){
        for(DemoMessageProcessor messageProcessor:messageProcessors){
            Set<String> processedRoutingKeys = messageProcessor.getProcessedRoutingKeys();
            if(CollectionUtils.isNotEmpty(processedRoutingKeys)){
                for(String routingKey:processedRoutingKeys){
                    messageProcessorContainer.put(routingKey,messageProcessor);
                }
            }
        }
    }

    @RabbitListener(queues = {"${rabbitmq.demo.trans.req.queue.name}"}, containerFactory = "phoRabbitListenerContainerFactory")
    public void onMessage(Message message, Channel channel) throws Exception {
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("received message routingkey:{}",receivedRoutingKey);
        DemoMessageProcessor messageProcessor=messageProcessorContainer.get(receivedRoutingKey);
        if(messageProcessor!=null){
            log.info("message processor:{}",messageProcessor.getClass().getCanonicalName());
            String messageBody=null;
            try {
                messageBody = new String(message.getBody(), MQConstants.DEFAULT_CHARSET);
                onMessageBySpecificProcessor(message.getMessageProperties(),messageBody,channel,messageProcessor);
            } catch (Exception e){
                //处理消息时发生异常，记录MQ错误日志，并确认完成消费
                log.error("message consume exception",e);
//                String scene=null;
//                if(e instanceof MQProcessingException){
//                    scene="普惠系统MQ消息处理业务异常";
//                }else{
//                    scene="普惠系统MQ消息处理未知异常";
//                }
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } finally {
                log.info("receive message from rabbitmq:{}", messageBody);
            }
        }else{
            log.error("routing key not be found matching message processor,routing key:{}",receivedRoutingKey);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    //针对特定消息处理器处理消息
    private void onMessageBySpecificProcessor(MessageProperties messageProperties, String messageBody, Channel channel, DemoMessageProcessor messageProcessor) throws Exception {
        String dtf = messageProcessor.getDataTransferFormat();
        Class clazz = messageProcessor.getTClass();
        log.info("processor handling message type:{}",clazz);
        Object messageBean = null;
        if (MQConstants.DATA_TRANSFER_FORMAT_JSON.equals(dtf)) {
            messageBean = JSONObject.parseObject(messageBody, clazz);
        } else if (MQConstants.DATA_TRANSFER_FORMAT_XML.equals(dtf)) {
            messageBean = XStreamUtil.fromXml(messageBody,clazz);
        }
        //记录日志
        MQMessage mqMessage=(MQMessage) messageBean;
        String serverUri="exchange："+transExchange+"；routingKey："+messageProperties.getReceivedRoutingKey();
        String title="普惠系统>>MQ服务>>"+mqMessage.getMqApiIdentifier().getName();
        String bizId=mqMessage.getLoanCode();
        String serialNo=mqMessage.getEcho();
        log.info(String.format("serverUri:{},title:{},bizId:{},seriaNo:{}",serverUri,title,bizId,serialNo));
        ResDetail resDetail = messageProcessor.process(messageProperties, messageBean);
        if (resDetail.isSuccess()) {
            log.info("process message success");
            channel.basicAck(messageProperties.getDeliveryTag(), false);
        } else {
            log.error("process message failure,redeliver message");
            channel.basicNack(messageProperties.getDeliveryTag(), false, true);
        }
    }

}
