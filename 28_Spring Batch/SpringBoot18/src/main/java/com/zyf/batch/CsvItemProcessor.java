package com.zyf.batch;

import com.zyf.domain.Person;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

/**
 * Created by zyf on 2018/3/15.
 */
public class CsvItemProcessor extends ValidatingItemProcessor<Person> {

	@Override
	public Person process(Person item) throws ValidationException {
		//调用自定义的校验器
		super.process(item);

		//处理数据
		if(item.getNation().equals("蜀国")){
			item.setNation("01");
		}else {
			item.setNation("02");
		}

		return item;
	}
}
