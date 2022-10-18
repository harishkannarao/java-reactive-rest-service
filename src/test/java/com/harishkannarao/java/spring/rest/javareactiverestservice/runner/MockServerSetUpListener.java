package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

public class MockServerSetUpListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        MockServerTestRunner.start();
        TestExecutionListener.super.testPlanExecutionStarted(testPlan);
    }
}
