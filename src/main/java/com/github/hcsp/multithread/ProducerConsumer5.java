package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer5 {
    static BlockingQueue blockingQueue = new LinkedBlockingQueue(1);
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Producer producer = new Producer();
            Consumer consumer = new Consumer();

            producer.start();
            consumer.start();

            producer.join();
            producer.join();
        }
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            Integer value = new Random().nextInt();
            System.out.println("Producing " + value);
            try {
                blockingQueue.put(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Consuming " + blockingQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
