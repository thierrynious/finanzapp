package com.finanzmanager.finanzapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AnsyncDemoService {

    @Async
    public CompletableFuture<String> heavyTask() throws InterruptedException {
        log.info("Starte Task in Thread: {}", Thread.currentThread().getName());
        Thread.sleep(2000);
        log.info("Task fertig!");

        return CompletableFuture.completedFuture("OK");
    }

    @Async
    public CompletableFuture<String> sleep() throws InterruptedException {

        TimeUnit.SECONDS.sleep(1);
        return CompletableFuture.completedFuture("done");
    }


    @Async
    public void sendMail() {  }


}
