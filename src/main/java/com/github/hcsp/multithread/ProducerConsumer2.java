package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    static Lock lock = new ReentrantLock();
    static Condition queueFull = lock.newCondition();
    static Condition queueEmpty = lock.newCondition();
    static LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

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
            try {
                lock.lock();
                while (queue.size() >= 10) {
                    queueEmpty.await();
                }
                int count = new Random().nextInt();
                queue.add(count);
                System.out.println("Producing " + count);
                queueFull.signal();
            } catch (Exception ignored) {

            } finally {
                lock.unlock();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                lock.lock();
                while (queue.size() == 0) {
                    queueFull.await();
                }
                Integer consumerCount = queue.remove();
                System.out.println("Consuming " + consumerCount);
                queueEmpty.signal();
            } catch (Exception ignored) {

            } finally {
                lock.unlock();
            }
        }
    }
}
