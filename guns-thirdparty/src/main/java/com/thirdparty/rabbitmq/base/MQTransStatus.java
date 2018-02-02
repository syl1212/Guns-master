package com.thirdparty.rabbitmq.base;

/**
 * @author lagon
 * @time 2017/5/23 11:13
 * @description MQ交易状态枚举
 */
public enum MQTransStatus {

    /**
     * 处理中
     */
    PROCESSING("0","处理中"),
    /**
     * 成功
     */
    SUCCESS("1", "成功"),
    /**
     * 失败
     */
    ERROR("2", "失败");

    private String code;
    private String desc;

    MQTransStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static MQTransStatus getEnum(String code){
        for (MQTransStatus transStatus : MQTransStatus.values()) {
            if (transStatus.getCode().equals(code)) {
                return transStatus;
            }
        }
        return null;
    }
}
