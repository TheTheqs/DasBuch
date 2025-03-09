package com.example.base_server.automation;

import com.example.base_server.utils.TxtFileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//This class will be the system that will configure and call the automation aspect.
@Service
public class AutomationScheduler {

    private final ApplicationContext context;
    private final AutomationTask automationTask;

    //Call count
    private int callCount = 0;

    public AutomationScheduler(ApplicationContext context, AutomationTask automationTask) {
        this.automationTask = automationTask;
        this.context = context;
    }

    @Scheduled(cron = "0 */7 * * * ?") //3 minutes
    public void runAutomation() {
        if (automationTask.continueTask()) {
            if(callCount == 0) {
                writeLog("Starting Call...");
            }
            callCount ++;
            automationTask.execute();
        } else {
            writeLog("There is no requisition in queue, task won't be called...");
        }
    }

    //Log control
    private void writeLog(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        TxtFileUtil.write("automation-system-log.txt", false, "[" + timestamp + "] " + message);
    }

    //End application function
    private void exitApplication() {
        SpringApplication.exit(context, () -> 0);
    }
}
