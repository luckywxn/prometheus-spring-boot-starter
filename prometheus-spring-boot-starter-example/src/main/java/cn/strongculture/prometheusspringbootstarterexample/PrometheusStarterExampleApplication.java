package cn.strongculture.prometheusspringbootstarterexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PrometheusStarterExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrometheusStarterExampleApplication.class, args);
    }

}
