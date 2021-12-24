package com.mile.portal.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

public class JavaTest {
    @Test
    @DisplayName("1. IntStream 사용법")
    void test1() {
        new Random().ints().limit(5).forEach(i -> System.out.printf("ints().limit(5) %d%n", i));
        System.out.println("------------------------------------------------------------------------");
        new Random().ints(5).forEach(i -> System.out.printf("init(5) %s%n", i));
    }

    @Test
    @DisplayName("2. 병렬 Stream")
    void test2() {
        List<String> stringList = new ArrayList<>();
        IntStream.range(0, 100000).forEach(i -> stringList.add(createString(100)));

        stringList.parallelStream().forEach(str -> {
            System.out.println("Starting " + Thread.currentThread().getName()
                    + ", String=" + str + ", " + new Date());

//            try{
//                Thread.sleep(1000);
//            } catch (InterruptedException e){
//            }
        });
    }

    @Test
    @DisplayName("3. 병렬 Stream - 2")
    void test3() throws ExecutionException, InterruptedException {
        List<String> stringList = new ArrayList<>();
        IntStream.range(0, 100000).forEach(i -> stringList.add(createString(100)));

        ForkJoinPool forkJoinPool = new ForkJoinPool(100);
        forkJoinPool.submit(() -> {
            stringList.parallelStream().forEach(str -> {
                System.out.println("Starting " + Thread.currentThread().getName()
                        + ", String=" + str + ", " + new Date());
//                try{
//                    Thread.sleep(1000);
//                } catch (InterruptedException e){
//                }
            });
        }).get();
    }

    @Test
    @DisplayName("4. 싱글 stream - 1")
    void test4() {
        List<String> stringList = new ArrayList<>();
        IntStream.range(0, 100000).forEach(i -> stringList.add(createString(100)));

        stringList.stream().forEach(str -> {
            System.out.println("Starting " + Thread.currentThread().getName()
                    + ", String=" + str + ", " + new Date());
//            try{
//                Thread.sleep(1000);
//            } catch (InterruptedException e){
//            }
        });
    }

    @Test
    @DisplayName("5. 병렬 더하기")
    void test5() {
        long start = System.currentTimeMillis();
        long sum = LongStream.range(0, 1_000_000_000)
                .parallel()
                .sum();

        System.out.println(sum);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    @DisplayName("6. 싱글 더하기")
    void test6(){
        long start = System.currentTimeMillis();
        long sum = LongStream.range(0, 1_000_000_000)
                .sum();

        System.out.println(sum);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }


    String createString(int strLength) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < strLength; i++) {
            stringBuilder.append((char) (random.nextInt(26) + 'a'));
        }

        return stringBuilder.toString();
    }
}
