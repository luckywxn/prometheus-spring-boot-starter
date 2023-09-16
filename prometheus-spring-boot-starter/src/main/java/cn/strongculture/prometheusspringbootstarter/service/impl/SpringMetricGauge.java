package cn.strongculture.prometheusspringbootstarter.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.ToDoubleFunction;

/**
 * @Author lucky_wxn
 * @Date 16/9/2023 上午11:32
 * @Content
 */
public class SpringMetricGauge {
    public static final ToDoubleFunction<Callable<Double>> metricFunc = doubleCallable -> {
        try {
            return doubleCallable.call();
        } catch (Exception e) {
            e.printStackTrace(); // NOSONAR
            return 0L;
        }
    };
    private String metricsName;
    private String description;
    private Map<String, String> tagMap;
    private Callable<Double> callable;

    public SpringMetricGauge(String metricsName, String description, Map<String, String> tagMap,
                             Callable<Double> callable) {
        this.metricsName = metricsName;
        this.description = description;
        this.tagMap = tagMap;
        this.callable = callable;
    }

    public String getMetricsName() {
        return metricsName;
    }

    public void setMetricsName(String metricsName) {
        this.metricsName = metricsName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getTagMap() {
        return tagMap;
    }

    public void setTagMap(Map<String, String> tagMap) {
        this.tagMap = tagMap;
    }

    public Callable<Double> getCallable() {
        return callable;
    }

    public void setCallable(Callable<Double> callable) {
        this.callable = callable;
    }

    @Override
    public String toString() {
        return "SpringMetricGauge{" + "metricsName='" + metricsName + '\'' + ", description='" + description + '\''
                + ", tagMap=" + (tagMap != null && !tagMap.isEmpty() ? tagMap.toString() : "{}") + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpringMetricGauge myGuage = (SpringMetricGauge) o;
        return Objects.equals(metricsName, myGuage.metricsName) && Objects.equals(description, myGuage.description)
                && Objects.equals(tagMap, myGuage.tagMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metricsName, description, tagMap);
    }

}
