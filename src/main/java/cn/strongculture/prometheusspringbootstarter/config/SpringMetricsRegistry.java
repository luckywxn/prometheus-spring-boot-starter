package cn.strongculture.prometheusspringbootstarter.config;

import cn.strongculture.prometheusspringbootstarter.service.impl.SpringMetricGauge;
import io.micrometer.core.instrument.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpringMetricsRegistry implements MeterBinder {
    private static Logger log = LoggerFactory.getLogger(SpringMetricsRegistry.class);

    private volatile MeterRegistry registry;

    @PreDestroy
    public void destroy() {
        if (registry != null && !registry.isClosed()) {
            registry.close();
        }
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        if (this.registry == null) {
            this.registry = registry;
            log.debug("MeterRegistry is set...");
        }
    }

    public Gauge registerGauge(SpringMetricGauge g) {
        checkState();
        List<Tag> tags = getTags(g.getTagMap());
        return Gauge.builder(g.getMetricsName(), g.getCallable(), SpringMetricGauge.metricFunc).tags(tags)
                .description(g.getDescription()).register(this.registry);
    }

    public Counter registerCounter(String metricsName, String description, Map<String, String> tagMap) {
        checkState();
        List<Tag> tags = getTags(tagMap);
        return Counter.builder(metricsName).tags(tags).description(description).register(this.registry);
    }

    public Timer registerTimer(String metricsName, String description, Map<String, String> tagMap) {
        checkState();
        List<Tag> tags = getTags(tagMap);
        return Timer.builder(metricsName).tags(tags).description(description).register(this.registry);
    }

    private List<Tag> getTags(Map<String, String> tagMap) {
        return tagMap.entrySet().stream().map(entry -> Tag.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private void checkState() {
        if (this.registry == null){
            synchronized (this){
                if (this.registry == null){
                    registry = new SimpleMeterRegistry();
                }
            }
        }
        if (this.registry == null) {
            throw new IllegalStateException("Metrics registry is not initialized yet!");
        }
    }
}
