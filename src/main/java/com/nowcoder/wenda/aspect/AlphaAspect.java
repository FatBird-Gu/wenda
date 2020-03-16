package com.nowcoder.wenda.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    /**
     * com.nowcoder.wenda.service.*所有的业务组件
     * com.nowcoder.wenda.service.*.* 所有的方法
     * com.nowcoder.wenda.service.*.*(..)所有的参数
     *
     */
    @Pointcut("execution(* com.nowcoder.wenda.service.*.*(..))")
    public void pointCut(){

    }

    @Before(value = "pointCut()")
    public void before(){
        System.out.println("before");
    }

    @After(value = "pointCut()")
    public void after(){
        System.out.println("after");
    }

    @AfterReturning(value = "pointCut()")
    public void afterReturning(){
        System.out.println("afterReturn");
    }

    @AfterThrowing(value = "pointCut()")
    public void afterThrowing(){
        System.out.println("afterThrow");
    }

    @Around(value = "pointCut()")
    public Object arount(ProceedingJoinPoint joinPoint) throws  Throwable{
        // 执行目标组件
        System.out.println("around Before");
        Object obj = joinPoint.proceed();
        System.out.println("around After");
        return obj;
    }
}
