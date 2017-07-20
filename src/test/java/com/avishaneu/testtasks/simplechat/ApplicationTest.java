package com.avishaneu.testtasks.simplechat;

import com.avishaneu.testtasks.simplechat.configuration.WebSocketConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by tkalnitskaya on 7/20/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=WebSocketConfiguration.class)
public class ApplicationTest {

    @Test
    public void applicationContextStartedTest() throws Exception {}

}