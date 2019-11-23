package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer3 {
    static Semaphore emptySlot = new Semaphore(1);
    static Semaphore fullSlot = new Semaphore(0);
    static ProducerConsumer1.Container container = new ProducerConsumer1.Container(null);

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
            synchronized (ProducerConsumer3.class) {
                try {
                    emptySlot.acquire();
                    container.value = new Random().nextInt();
                    System.out.println("Producing " + container.value);
                    fullSlot.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                fullSlot.acquire();
                System.out.println("Consuming " + container.value);
                container.value = null;
                emptySlot.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
