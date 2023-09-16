package cn.strongculture.prometheusspringbootstarter.config;

import cn.strongculture.prometheusspringbootstarter.service.MetricTimer;
import cn.strongculture.prometheusspringbootstarter.service.MetricsClient;
import cn.strongculture.prometheusspringbootstarter.service.impl.SpringMetricsClient;
import com.google.common.util.concurrent.AtomicDouble;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
@ConditionalOnClass(MetricsClient.class)
public class SpringMetricsAutoConfiguration {

    private static Logger logger = LoggerFactory.getLogger(SpringMetricsAutoConfiguration.class);

    @Autowired
    private MetricsClient metricsClient;
    @Value("${monitor.api.pointcut.expression:execution(public * a.b.c.d..*.*(..))}")
    private String apiMonitorPointcutExpression;

    private Lock lock = new ReentrantLock();

    private Map<Method, AtomicDouble> concurrentExecutionCount = new ConcurrentHashMap<>();



    @Bean
    public AspectJExpressionPointcutAdvisor getAspectJExpressionPointcutAdvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(apiMonitorPointcutExpression);
        advisor.setAdvice((MethodInterceptor) invocation -> {
            Object result = null;
            Method method = invocation.getMethod();
            Map<String, String> tags = new HashMap<>();
            tags.put("method", method.getName());
            tags.put("class", getSimplifiedClassName(method.getDeclaringClass()));
            long startTime = System.currentTimeMillis();
            try {
                if (!concurrentExecutionCount.containsKey(method)) {
                    addGauge(method);
                }
                concurrentExecutionCount.get(method).addAndGet(1.0);
                result = invocation.proceed();
                tags.put("status", "OK");
            } catch (Exception e) {
                tags.put("status", "EXCEPTION");
                throw e;
            } finally {
                concurrentExecutionCount.get(method).addAndGet(-1.0);
                MetricTimer timer = metricsClient.timer("api_cost", "Monitoring interface time consumption", tags);
                timer.record(System.currentTimeMillis() - startTime);
            }
            return result;
        });
        return advisor;
    }

    private String getSimplifiedClassName(Class<?> classObj) {
        return classObj.getName().replaceAll("([^\\.])[^\\.]*\\.", "$1.");
    }

    private void addGauge(Method method) {
        try {
            lock.lock();
            Map<String, String> tags = new HashMap<>();
            tags.put("method", method.getName());
            tags.put("class", getSimplifiedClassName(method.getDeclaringClass()));
            concurrentExecutionCount.put(method, new AtomicDouble(0));
            metricsClient.gauge("api_concurrent_execution_current", "Instantaneous concurrency of interfaces", tags, new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    return concurrentExecutionCount.get(method).get();
                }
            });
        } catch (Exception e) {
            logger.error("addGauge error " + method.getName(), e);
        } finally {
            lock.unlock();
        }
    }

    @Bean
    @Qualifier("metricsClient")
    @ConditionalOnMissingBean
    public MetricsClient metricsClient() {
        return new SpringMetricsClient();
    }

    @Bean
    public SpringMetricsRegistry springMetricsRegister() {
        return new SpringMetricsRegistry();
    }

}
