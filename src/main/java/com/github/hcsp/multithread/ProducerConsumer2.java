package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    static Lock lock = new ReentrantLock();
    static Condition queueFull = lock.newCondition();
    static Condition queueEmpty = lock.newCondition();
    private static ProducerConsumer1.Container container = new ProducerConsumer1.Container(null);

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
                    lock.lock();
                    while (container.value != null) {
                        queueEmpty.await();
                    }
                    int count = new Random().nextInt();
                    container.value = count;
                    System.out.println("Producing " + count);
                    queueFull.signal();
                } catch (Exception ignored) {

                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (container.value == null) {
                        queueFull.await();
                    }
                    System.out.println("Consuming " + container.value);
                    container.value = null;
                    queueEmpty.signal();
                } catch (Exception ignored) {

                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
