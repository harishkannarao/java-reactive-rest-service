package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

public class SpringBootSetUpListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        SpringBootTestRunner.start(SpringBootTestRunner.getIntegrationTestProperties());
        TestExecutionListener.super.testPlanExecutionStarted(testPlan);
    }
}
