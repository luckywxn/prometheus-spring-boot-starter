package cn.strongculture.prometheusspringbootstarter.service.impl;

import cn.strongculture.prometheusspringbootstarter.service.MetricTimer;
import io.micrometer.core.instrument.Timer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author lucky_wxn
 * @Date 16/9/2023 上午11:32
 * @Content
 */
public class SpringMetricTimer implements MetricTimer {
    private String metricName;
    private Map<String, String> tagMap;
    private Timer timer;

    public SpringMetricTimer(String metricName, Map<String, String> tagMap, Timer timer) {
        this.metricName = metricName;
        this.tagMap = tagMap;
        this.timer = timer;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Map<String, String> getTagMap() {
        return tagMap;
    }

    public void setTagMap(Map<String, String> tagMap) {
        this.tagMap = tagMap;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void record(long millis) {
        this.record(millis, TimeUnit.MILLISECONDS);
    }

    @Override
    public void record(long time, TimeUnit unit) {
        timer.record(time, unit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpringMetricTimer that = (SpringMetricTimer) o;
        return Objects.equals(metricName, that.metricName) && Objects.equals(tagMap, that.tagMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metricName, tagMap);
    }

    @Override
    public String toString() {
        return "SpringMetricTimer{" + "metricName='" + metricName + '\'' + ", tagMap="
                + (tagMap != null && !tagMap.isEmpty() ? tagMap.toString() : "{}") + '}';
    }
}
