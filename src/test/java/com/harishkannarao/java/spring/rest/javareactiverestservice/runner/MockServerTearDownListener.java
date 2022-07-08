package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

public class MockServerTearDownListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        if (MockServerTestRunner.isRunning()) {
            try {
                MockServerTestRunner.getClient().close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                MockServerTestRunner.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        TestExecutionListener.super.testPlanExecutionFinished(testPlan);
    }
}
