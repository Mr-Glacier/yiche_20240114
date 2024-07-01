package org.example.Controller;

import org.example.Until.SaveUntil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class youxinpai_time_task {

    /**
     * 优信拍,定时任务
     * 初步决定 ,4H 一次爬取
     * 增加log  日志记录
     * main
     */

    public static void main(String[] args) {
        // 增加任务执行记录 的必要

        // 创建一个单线程的调度程序
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Controller_youxinpai controller_youxinpai = new Controller_youxinpai();

        // 定义要执行的任务
        Runnable task = controller_youxinpai::Method_1_GetData;

        // 设置初始延迟时间：2秒后开始执行
        long initialDelay = 30;

        // 设置任务执行间隔：6小时
        long period = 6 * 60 * 60;

        // 调度任务，每6小时执行一次
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);


        try {
            // 假设你希望程序运行24小时以观察任务执行情况
            Thread.sleep(25 * 60 * 60 * 1000); // 运行24小时
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 主线程 睡眠24

        // 关闭调度程序
        scheduler.shutdown();
    }

}
