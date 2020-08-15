package bat.ke.qq.com.zklock.controller;

import bat.ke.qq.com.zklock.lock.Lock;
import bat.ke.qq.com.zklock.lock.zk.ZkDistributedLock;
import bat.ke.qq.com.zklock.service.OrderCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.ReentrantLock;

@RestController
public class OrderController {


    @Autowired
    private OrderCodeGenerator orderCodeGenerator;

    @Autowired
    private Lock zkLock;


    private java.util.concurrent.locks.Lock lock=new ReentrantLock();
    @RequestMapping("/getOrderCode")
    public void getOrderCode(){
        try {
            zkLock.lock();

            String orderCode=orderCodeGenerator.getOrderCode();
            System.out.println("生产订单号："+orderCode);
        }finally {
            zkLock.unlock();

        }

    }

    @Bean
    public Lock zkLock(){
        //基于临时节点
        String config="127.0.0.1:2181";
        String path="/lock";
        return new ZkDistributedLock(path,config);
    }
}
