package com.james.ystechchallenge.datasyncdaemon.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Service locator used to inject dependencies into the scheduled service where ordinary DI is not supported
 * @author james
 */
@Component
public class ServiceLocator implements ApplicationContextAware{
    private static ApplicationContext context;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }
    
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}
