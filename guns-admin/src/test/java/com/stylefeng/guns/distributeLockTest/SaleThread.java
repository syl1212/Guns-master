package com.stylefeng.guns.distributeLockTest;

/**
 * 文件名称： SaleThread
 * 文件描述：
 * <p>
 * 公司部门： 东方银谷 JDK
 * 创建时间： 2018年03月02 14:33:04
 *
 * @author syl sunyuanli@yingu.com
 * @version 1.0
 */
public class SaleThread extends Thread {
    private SaleService service;

    public SaleThread(SaleService service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.seckill();
    }
}
