package cn.strongculture.prometheusspringbootstarter.service;

import java.util.concurrent.TimeUnit;

public interface MetricTimer {

    void record(long millis);

    void record(long time, TimeUnit unit);
}
