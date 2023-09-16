package cn.strongculture.prometheusspringbootstarter.service;

public interface MetricCounter {

    void increment();

    void incrementBy(long delta);
}
