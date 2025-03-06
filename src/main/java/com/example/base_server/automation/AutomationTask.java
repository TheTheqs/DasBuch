package com.example.base_server.automation;

//This interface apply the execute function, the base function which will be auto-called.
public interface AutomationTask {
    //This function will tell if the automation system move on or break
    boolean continueTask();
    //The actual callable function
    void execute();
}
