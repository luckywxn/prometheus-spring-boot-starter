package cn.strongculture.prometheusspringbootstarter.service.impl;

import cn.strongculture.prometheusspringbootstarter.config.SpringMetricsRegistry;
import cn.strongculture.prometheusspringbootstarter.service.MetricCounter;
import cn.strongculture.prometheusspringbootstarter.service.MetricTimer;
import cn.strongculture.prometheusspringbootstarter.service.MetricsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author lucky_wxn
 * @Date 16/9/2023 上午11:30
 * @Content
 */
public class SpringMetricsClient implements MetricsClient {
    private static Logger log = LoggerFactory.getLogger(SpringMetricsClient.class);
    @Autowired
    @Lazy
    private SpringMetricsRegistry register;
    private Set<SpringMetricGauge> gauges = ConcurrentHashMap.newKeySet();
    private ConcurrentMap<String, MetricCounter> counters = new ConcurrentHashMap<>();
    private ConcurrentMap<String, MetricTimer> timers = new ConcurrentHashMap<>();

    @PreDestroy
    public void destroy() {
        gauges.clear();
        counters.clear();
        timers.clear();
    }

    @Override
    public void gauge(String metricName, String description, Map<String, String> tagMap,
                      Callable<Double> callable) {
        SpringMetricGauge g = new SpringMetricGauge(metricName, description, tagMap, callable);
        if (gauges.add(g)) {
            register.registerGauge(g);
            log.debug("gauge metric added for: {}", g);
        } else {
            log.error("duplicated gauge: {}", g);
        }
    }

    @Override
    public MetricCounter counter(String metricName, String description, Map<String, String> tagMap) {
        MetricCounter c;
        String key = getKey(metricName, tagMap);
        if ((c = counters.get(key)) == null) {
            c = counters.computeIfAbsent(key, k -> getSpringMetricCounter(metricName, description, tagMap));
        }
        return c;
    }

    @Override
    public MetricTimer timer(String metricName, String description, Map<String, String> tagMap) {
        MetricTimer t;
        String key = getKey(metricName, tagMap);
        if ((t = timers.get(key)) == null) {
            t = timers.computeIfAbsent(key, k -> getSpringMetricTimer(metricName, description, tagMap));
        }
        return t;
    }

    private MetricTimer getSpringMetricTimer(String metricName, String description, Map<String, String> tagMap) {
        return new SpringMetricTimer(metricName, tagMap, register.registerTimer(metricName, description, tagMap));
    }

    private MetricCounter getSpringMetricCounter(String metricName, String description,
                                                 Map<String, String> tagMap) {
        return new SpringMetricCounter(metricName, tagMap,
                register.registerCounter(metricName, description, tagMap));
    }

    private String getKey(String metricName, Map<String, String> tagMap) {
        return metricName + (tagMap != null ? tagMap.toString() : "");
    }

    public Set<SpringMetricGauge> getGauges() {
        return gauges;
    }

    public Map<String, MetricCounter> getCounters() {
        return counters;
    }

    public Map<String, MetricTimer> getTimers() {
        return timers;
    }
}
