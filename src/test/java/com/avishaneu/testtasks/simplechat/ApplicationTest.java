package com.avishaneu.testtasks.simplechat;

import com.avishaneu.testtasks.simplechat.configuration.WebSocketConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by tkalnitskaya on 20.07.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=WebSocketConfiguration.class)
@EnableAutoConfiguration
public class ApplicationTest {

    @Test
    public void applicationContextStartedTest() throws Exception {}

}