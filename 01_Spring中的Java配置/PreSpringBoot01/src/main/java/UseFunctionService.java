import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zyf on 2018/3/2.
 */
public class UseFunctionService {

//	@Autowired 这里也没有使用装配注解
	private FunctionService functionService;

	public void setFunctionService(FunctionService functionService) {
		this.functionService = functionService;
	}

	public String fun(String str){
		return functionService.fun(str);
	}
}
