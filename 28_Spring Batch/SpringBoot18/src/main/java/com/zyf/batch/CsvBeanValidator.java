package com.zyf.batch;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;


/**
 * Created by zyf on 2018/3/15.
 */
public class CsvBeanValidator<T> implements Validator<T>,InitializingBean {

	private javax.validation.Validator validator;


	@Override
	public void validate(T t) throws ValidationException {
		//校验数据，得到验证不通过的约束
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
		if(constraintViolations.size() > 0){
			StringBuilder message = new StringBuilder();
			for (ConstraintViolation<T> constraintViolation : constraintViolations) {
				System.out.println("有一个无法通过的验证问题:"+t.toString());
				message.append(constraintViolation.getMessage()+"\n");
			}
			throw new ValidationException(message.toString());
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//使用JSR-303 的 Validator 来校验我们的数据
		//此处初始化JSR-303 的 Validator
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

		validator = validatorFactory.usingContext().getValidator();
	}

}
