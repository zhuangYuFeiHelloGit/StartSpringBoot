import org.springframework.stereotype.Service;

/**
 * Created by zyf on 2018/3/2.
 */
//@Service 这里没有使用注解
public class FunctionService {

	public String fun(String str){
		return "愉快：" + str;
	}
}
