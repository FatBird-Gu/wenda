package com.nowcoder.wenda;

import com.nowcoder.wenda.entity.DiscussPost;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
@ContextConfiguration(classes = WendaApplication.class)
public class SpringBootTest {

    @Autowired
    private DiscussPost discussPost;
    @BeforeClass
    public static void beforeClass(){
        System.out.println("before class");
    }

    @AfterClass
    public static void afterclass(){
        System.out.println("after class");
    }

    @Before
    public void before(){
        System.out.println("before");
    }

    @After
    public void after(){
        System.out.println("after");
    }

    @Test
    public void test1(){
        System.out.println("test1");
    }

    @Test
    public void test2(){
        System.out.println("test2");
    }
}
