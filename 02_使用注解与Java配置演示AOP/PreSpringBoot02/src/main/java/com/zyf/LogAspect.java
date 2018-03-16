package com.zyf;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 切面
 */
@Aspect//声明此类是一个切面
@Component//将此机切面，放入Spring容器中，成为一个bean
public class LogAspect {

	//声明切入点
	@Pointcut("@annotation(com.zyf.Action)")
	public void annotationPointCut(){

	}

	//声明通知（建言），使用上面定义的那个切点
	@After("annotationPointCut()")
	public void after(JoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		Action action = method.getAnnotation(Action.class);
		//通过反射获得注解上的属性,@action(name="我就是属性")
		System.out.println("注解式拦截："+action.name());
	}

	//声明通知（建言），直接使用拦截规则作为参数
	@Before("execution(* com.zyf.ShowMethodService.*(..))")
	public void before(JoinPoint joinPoint){

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		System.out.println("方法规则拦截："+method.getName());

	}
}
