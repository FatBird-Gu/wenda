package com.nowcoder.wenda;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueTests {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        new Thread(new producer(queue)).start();
        new Thread(new comsumer(queue)).start();
        new Thread(new comsumer(queue)).start();
        new Thread(new comsumer(queue)).start();
    }
}

class producer implements  Runnable{

    private BlockingQueue<Integer> queue;
    public producer(BlockingQueue queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        try{
            for (int i = 0; i<100; i++){
                Thread.sleep(20);
                queue.put(i);
                System.out.println(Thread.currentThread().getName()+"生产:" + queue.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class comsumer implements  Runnable{

    private BlockingQueue<Integer> queue;
    public comsumer(BlockingQueue queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        try{
            while(true){
                Thread.sleep(new Random().nextInt(500));
                queue.take();
                System.out.println(Thread.currentThread().getName()+"消费:" + queue.size());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}