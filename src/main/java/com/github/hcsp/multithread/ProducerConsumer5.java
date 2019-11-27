package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer5 {
    private static BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(1);
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                Integer value = new Random().nextInt();
                System.out.println("Producing " + value);
                    blockingQueue.put(value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int value = blockingQueue.take();
                    System.out.println("Consuming " + value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
