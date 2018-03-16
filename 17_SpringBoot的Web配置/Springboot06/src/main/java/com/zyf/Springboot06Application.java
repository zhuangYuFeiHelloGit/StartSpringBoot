package com.zyf;

import com.zyf.config.CustomServletContainer;
import com.zyf.domain.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@SpringBootApplication
public class Springboot06Application {

	@Component
	public static class CustomServletContainer implements EmbeddedServletContainerCustomizer{

		@Override
		public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {

		}
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer(){
		TomcatEmbeddedServletContainerFactory factory =
				new TomcatEmbeddedServletContainerFactory();
		factory.setPort(8888);
		factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/404.html"));
		factory.setSessionTimeout(10, TimeUnit.MINUTES);
		return factory;
	}

	@RequestMapping("/")
	public String index(Model model){
		Person single = new Person("abc",123);

		List<Person> people = new ArrayList<>();
		Person p1 = new Person("x",11);
		Person p2 = new Person("y",22);
		Person p3 = new Person("z",33);

		people.add(p1);
		people.add(p2);
		people.add(p3);

		model.addAttribute("singlePerson",single);
		model.addAttribute("people",people);

		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(Springboot06Application.class, args);
	}
}
