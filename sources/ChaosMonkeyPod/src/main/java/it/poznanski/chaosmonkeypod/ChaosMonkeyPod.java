package it.poznanski.chaosmonkeypod;

import it.poznanski.chaosmonkeypod.quartz.ChaosMonkeyJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;


public class ChaosMonkeyPod {

    private static final Logger logger = LogManager.getRootLogger();

    public static void main (String[] args) {
        try {
            logger.info("Starting scheduler");
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            logger.info("Creating job ChaosMonkey in group killers");
            JobDetail job = JobBuilder.newJob(ChaosMonkeyJob.class)
                    .withIdentity("ChaosMonkey", "killers")
                    .build();
            logger.info("Creating cron trigger for ChaosMonkey in group killers");
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("ChaosMonkeyTrigger", "killers")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(System.getenv().get("QUARTZ_CRON_EXPR")))
                    .build();
            logger.info("Configuring trigger for ChaosMonkey in scheduler");
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
            logger.info("Job ChaosMonkeyPodJob schedulated");
        }catch(SchedulerException e){
            String msg = "Error while executing preliminary configgurations for Chaos Monkey Job";
            logger.error(msg, e);
        }
    }
}
