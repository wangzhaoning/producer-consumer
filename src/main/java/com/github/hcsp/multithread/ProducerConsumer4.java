package com.github.hcsp.multithread;


import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer4 {

    public static void main(String[] args) throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<>();

        Producer producer = new Producer(exchanger);
        Consumer consumer = new Consumer(exchanger);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Exchanger<Integer> exchanger;

        public Producer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    exchanger.exchange(value);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Exchanger<Integer> exchanger;

        public Consumer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int value = exchanger.exchange(null);
                    System.out.println("Consuming " + value);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
