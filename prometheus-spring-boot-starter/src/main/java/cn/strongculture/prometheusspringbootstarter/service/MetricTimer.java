package cn.strongculture.prometheusspringbootstarter.service;

import java.util.concurrent.TimeUnit;

/**
 * @Author lucky_wxn
 * @Date 16/9/2023 上午11:29
 * @Content 计时器埋点类型
 */
public interface MetricTimer {
    /**
     * 记录消耗的时间（毫秒）
     *
     * @param millis
     */
    void record(long millis);

    /**
     * 记录消耗的时间（指定时间单位）
     *
     * @param time
     * @param unit
     */
    void record(long time, TimeUnit unit);
}
