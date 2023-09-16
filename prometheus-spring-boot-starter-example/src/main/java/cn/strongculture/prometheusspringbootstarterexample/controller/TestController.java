package cn.strongculture.prometheusspringbootstarterexample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @Author lucky_wxn
 * @Date 10/9/2023 下午2:01
 * @Content
 */
@RestController
public class TestController {

    Random random = new Random();

    @GetMapping(value = "test1")
    public String test1(){
        try {
            Thread.sleep(random.nextInt(50) + 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    @GetMapping(value = "test2")
    public String test2(){
        try {
            Thread.sleep(random.nextInt(80) + 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }
    @GetMapping(value = "test3")
    public String test3(){
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }
    @GetMapping(value = "test4")
    public String test4(){
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

}
