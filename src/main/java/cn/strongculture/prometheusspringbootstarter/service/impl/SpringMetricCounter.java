package cn.strongculture.prometheusspringbootstarter.service.impl;

import cn.strongculture.prometheusspringbootstarter.service.MetricCounter;
import io.micrometer.core.instrument.Counter;

import java.util.Map;
import java.util.Objects;

public class SpringMetricCounter implements MetricCounter {
    private String metricName;
    private Map<String, String> tagMap;
    private Counter counter;

    public SpringMetricCounter(String metricName, Map<String, String> tagMap, Counter counter) {
        this.metricName = metricName;
        this.tagMap = tagMap;
        this.counter = counter;
    }

    @Override
    public void increment() {
        this.counter.increment();
    }

    @Override
    public void incrementBy(long delta) {
        this.counter.increment(delta);
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

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(metricName, tagMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpringMetricCounter that = (SpringMetricCounter) o;
        return Objects.equals(metricName, that.metricName) && Objects.equals(tagMap, that.tagMap);
    }

    @Override
    public String toString() {
        return "SpringMetricCounter{" + "metricName='" + metricName + '\'' + ", tagMap="
                + (tagMap != null && !tagMap.isEmpty() ? tagMap.toString() : "{}") + ", counter=" + counter.count()
                + '}';
    }
}
