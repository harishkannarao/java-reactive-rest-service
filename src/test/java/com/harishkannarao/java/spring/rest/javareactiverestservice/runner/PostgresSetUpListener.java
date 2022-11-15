package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

public class PostgresSetUpListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        if (Boolean.parseBoolean(System.getProperty("preemptiveStartAppAndDependencies", "false"))) {
            PostgresTestRunner.start();
        }
        TestExecutionListener.super.testPlanExecutionStarted(testPlan);
    }
}
