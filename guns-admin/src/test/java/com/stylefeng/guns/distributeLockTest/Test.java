package com.stylefeng.guns.distributeLockTest;

/**
 * 文件名称： Test
 * 文件描述：
 * <p>
 * 公司部门： 东方银谷 JDK
 * 创建时间： 2018年03月02 14:34:27
 *
 * @author syl sunyuanli@yingu.com
 * @version 1.0
 */
public class Test {

    public static void main(String[] args) {
        SaleService service = new SaleService();
        for (int i = 0; i < 200; i++) {
            SaleThread threadA = new SaleThread(service);
            threadA.start();
        }
    }
}
