package me.j360.conf.service;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

public class Bootstrap {

    public static void main(String[] args) throws InterruptedException {
        System.out.print(args);

        System.setProperty("resources.config.path","/Users/min_xu/Documents/IdeaProjects/fotoplace/apps/app2.properties");

        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:conf-service-context.xml");
        ctx.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                ctx.close();
            }
        });
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

}
