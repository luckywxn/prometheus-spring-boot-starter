package cn.strongculture.prometheusspringbootstarter.service;

/**
 * @Author lucky_wxn
 * @Date 16/9/2023 上午11:27
 * @Content 累加器埋点类型
 */
public interface MetricCounter {
    /**
     * 累加器加1
     */
    void increment();

    /**
     * 累加器加delta
     *
     * @param delta
     */
    void incrementBy(long delta);
}
