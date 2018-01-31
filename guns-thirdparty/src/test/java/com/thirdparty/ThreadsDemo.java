package com.thirdparty;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by syl on 2018/1/29.
 */
public class ThreadsDemo {

    private static String[] userIds = {"000","001","002","003","004","005","006","007","008","009",
            "010","011","012","013","014","015","016","017","018","019"};

    private static ExecutorService threadPool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            new ThreadFactoryBuilder().setNameFormat("thread-test-pool-%d").build(), new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args){
        List<String> taskList = new ArrayList<String>(100);
        //随机创建100个任务
        for(int i=0; i<100; i++){
            int index = (int)Math.ceil(Math.random()*20 - 1);
            taskList.add(userIds[index]);
        }

        System.out.println("集合：" + taskList.toString());

        long start = System.currentTimeMillis();
        System.out.println("任务开始时间：" + start);

        final CountDownLatch latch=new CountDownLatch(taskList.size());//两个工人的协作

        for(String str : taskList)
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    synchronized (str) {
                        try {
                            System.out.println(String.format("当前线程：" + Thread.currentThread().getName() + "，当前用户：" + str));
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            latch.countDown();
                        }
                    }
                }
            });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPool.shutdown();

        long end = System.currentTimeMillis();
        System.out.println("任务结束时间：" + end + "，一共消耗时间：" + (end - start));
    }
}
