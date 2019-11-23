package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {
    private static Container container = new Container(null);

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

    static class Container {
        volatile Integer value;

        Container(Integer value) {
            this.value = value;
        }
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            synchronized (container) {
                if (container.value != null) {
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                container.value = new Random().nextInt();
                System.out.println("Producing " + container.value);
                container.notify();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            synchronized (container) {
                if (container.value == null) {
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Consuming " + container.value);
                container.value = null;
                container.notify();
            }
        }
    }
}
