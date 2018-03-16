import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zyf on 2018/3/2.
 */
public class Main {
	public static void main(String[] args) {
		//原来是创建一个ClassPath...是传入一个spring配置文件的路径
		//现在使用java配置，是传入一个java类对象
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(JavaConfig.class);
		UseFunctionService useFunctionService = context.getBean(UseFunctionService.class);
		System.out.println(useFunctionService.fun("哈哈哈哈哈哈"));

		//关闭容器
		context.close();
	}
}
