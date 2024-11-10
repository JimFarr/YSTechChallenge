package com.james.ystechchallenge.datasyncdaemon.config;

import com.james.ystechchallenge.datasyncdaemon.impl.DataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Configuration of the scheduler used to periodically trigger expiration updates
 * @author james
 */
@Configuration
public class SchedulerConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);
    
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder
                .newJob(DataSyncService.class)
                .withIdentity("dailyTask")
                .storeDurably()
                .build();
    }
    
    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder
                .newTrigger()
                .forJob(jobDetail)
                .withIdentity("dailyTaskTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")) // trigger once per day
                .build();
    }
    
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger trigger, JobDetail jobDetail) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setJobDetails(jobDetail);
        schedulerFactory.setTriggers(trigger);
        logger.info("Quartz Scheduler configured and ready to start.");
        return schedulerFactory;
    }
}
