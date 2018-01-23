package com.thirdparty;

/**
 * Created by syl on 2018/1/23.
 */
public class Business {
    private boolean bool = true;

    public synchronized void main(int loop) throws InterruptedException {
        while (bool) {
            this.wait();
        }
        for (int i = 0; i < 100; i++) {
            System.out.println("main thread seq of " + i + ", loop of " + loop);
        }
        bool = true;
        this.notify();
    }

    public synchronized void sub(int loop) throws InterruptedException {
        while (!bool) {
            this.wait();
        }
        for (int i = 0; i < 10; i++) {
            System.out.println("sub thread seq of " + i + ", loop of " + loop);
        }
        bool = false;
        this.notify();
    }
}
