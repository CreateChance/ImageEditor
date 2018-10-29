package com.createchance.imageeditordemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Work运行器
 *
 * @author createchance
 * @date 21/11/2017
 */

public final class WorkRunner {
    /**
     * 自定义的带有优先级的后台线程池
     * 核心线程数为：cpu核心数 + 1
     * 最大线程数为：cpu核心数 x 2 + 1
     * 线程空闲保活时间为：60s
     */
    private static ExecutorService mThreadPool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2);

    /**
     * 添加一个任务到后台线程队列中运行
     *
     * @param task 需要执行的任务
     */
    public static void addTaskToBackground(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }

        mThreadPool.execute(task);
    }
}
