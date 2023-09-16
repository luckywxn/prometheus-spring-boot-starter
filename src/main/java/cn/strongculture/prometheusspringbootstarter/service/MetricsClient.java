package cn.strongculture.prometheusspringbootstarter.service;

import java.util.Map;
import java.util.concurrent.Callable;

public interface MetricsClient {

    MetricCounter counter(String metricsName, String description, Map<String, String> tagMap);

    MetricTimer timer(String metricsName, String description, Map<String, String> tagMap);

    void gauge(String metricsName, String description, Map<String, String> tagMap, Callable<Double> callable);

}
