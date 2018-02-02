package com.stylefeng.guns.core.log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * 日志管理器
 *
 * @author fengshuonan
 * @date 2017-03-30 16:29
 */
public class LogManager {

    /**
     * 日志记录操作延时
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     *  异步操作记录日志的线程池
     */
    private ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);

    private LogManager() {
    }

    public static LogManager logManager = new LogManager();

    public static LogManager me() {
        return logManager;
    }

    public void executeLog(TimerTask task) {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }
}
