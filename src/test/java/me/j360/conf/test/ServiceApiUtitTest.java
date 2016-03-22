package me.j360.conf.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by min_xu on 15/12/10.
 * spring service api unit test
 */

@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback=false)
@ContextConfiguration(locations = {"classpath:/conf-service-context-test.xml"})
public class ServiceApiUtitTest {
    static {
        System.setProperty("resources.config.path","/Users/min_xu/app2.properties");
    }

    @Before
    public void init() {

    }

    @Value("#{app.logRoot}")
    private String logRoot;

    @Test
    public void testQueryAlarmLogs() throws Exception {
        System.out.println(System.getProperty("resources.config.path"));

        System.out.println(logRoot);
    }

}
