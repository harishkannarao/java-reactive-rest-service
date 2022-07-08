package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

public class SpringBootTearDownListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        if (SpringBootTestRunner.isRunning()) {
            try {
                SpringBootTestRunner.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        TestExecutionListener.super.testPlanExecutionFinished(testPlan);
    }
}
