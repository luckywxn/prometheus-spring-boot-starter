package cn.strongculture.prometheusspringbootstarterexample.job;

import cn.strongculture.prometheusspringbootstarterexample.controller.TestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author lucky_wxn
 * @Date 15/9/2023 下午8:55
 * @Content
 */
@Component
public class TestTask {

    final static Logger log = LoggerFactory.getLogger(TestTask.class);

    @Resource
    private TestController testController;

    @Scheduled(cron = "0/2 * * * * ?")
    void test1(){
        log.info("test1 调度时间为 {}",new Date());
        testController.test1();
    }

    @Scheduled(cron = "0/2 * * * * ?")
    void test2(){
        log.info("test2 调度时间为 {}",new Date());
        testController.test2();
    }
}
