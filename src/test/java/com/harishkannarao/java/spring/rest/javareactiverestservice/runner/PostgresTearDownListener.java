package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

public class PostgresTearDownListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        if (PostgresTestRunner.isRunning()) {
            try {
                PostgresTestRunner.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        TestExecutionListener.super.testPlanExecutionFinished(testPlan);
    }
}
