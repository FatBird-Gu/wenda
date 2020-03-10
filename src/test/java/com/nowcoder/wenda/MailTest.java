package com.nowcoder.wenda;

import com.nowcoder.wenda.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WendaApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void testTextMail(){
        mailClient.sendMail("1014246175@qq.com","Test","welcome to daddy");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","sunday");

        String content = templateEngine.process("/mail/demo",context);
        mailClient.sendMail("1014246175@qq.com","牛客网",content);
    }
}
