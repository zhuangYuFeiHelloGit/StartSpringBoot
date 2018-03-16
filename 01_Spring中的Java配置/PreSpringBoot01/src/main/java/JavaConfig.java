import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zyf on 2018/3/2.
 */
@Configuration//表示当前类是一个配置类
public class JavaConfig {

	@Bean//表示该方法会返回一个bean，bean的名称就是方法名
	public FunctionService functionService(){
		//自己创建一个FunctionService对象并返回
		return new FunctionService();
	}

	@Bean//表示该方法会返回一个bean
	public UseFunctionService useFunctionService(){
		//自己构建一个UserFunctionService对象并返回
		UseFunctionService useFunctionService = new UseFunctionService();
		//直接调用functionService注入functionService对象
		useFunctionService.setFunctionService(functionService());
		return useFunctionService;
	}

	/**
	 * spring容器提供的功能，只要容器中存在某个bean，就可以在另一个bean的方法声明中使用参数注入
	 * @param functionService
	 * @return
	 */
	@Bean//表示该方法会返回一个bean
	public UseFunctionService useFunctionService(FunctionService functionService){
		//自己构建一个UserFunctionService对象并返回
		UseFunctionService useFunctionService = new UseFunctionService();
		//直接调用将参数functionService对象注入
		useFunctionService.setFunctionService(functionService);
		return useFunctionService;
	}

}
