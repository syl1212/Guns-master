package com.stylefeng.guns.core.base.tips;

/**
 * 返回给前台的错误提示
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午5:05:22
 */
public class ErrorAbstractTip extends AbstractTip {

    public ErrorAbstractTip(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
