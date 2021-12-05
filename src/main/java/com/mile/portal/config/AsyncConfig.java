package com.mile.portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); //기본적으로 실행을 대기하고 있는 Thread의 갯수
        executor.setMaxPoolSize(50); //동시 동작하는, 최대 Thread 갯수
        executor.setQueueCapacity(2); //MaxPoolSize를 초과하는 요청이 Thread 생성 요청시 해당 내용을 Queue에 저장
        executor.setThreadNamePrefix("Async-Executor-"); //spring이 생성하는 쓰레드의 접두사를 지정
        executor.initialize();
        return executor;
    }
}
