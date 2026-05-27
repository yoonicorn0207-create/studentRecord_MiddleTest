package com.kopo.cycode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kopo.cycode.pojo.Hello;

@Controller
public class HelloController {
	
	@Autowired
	private Hello hello_ccycode; //스프링이  beans.xml있는 hello 객체를 자동을 꽂아줌
	
	@Autowired
	private Hello hello; //스프링이  beans.xml있는 hello 객체를 자동을 꽂아줌
	

	@RequestMapping("/helloTest") // 웹 브라우저에서 /helloTest로 접속하면 실행
	public String helloTest(Model model) {
		String result  = hello_ccycode.sayHello();
		String result2  = hello.sayHello();
		model.addAttribute("msg", result);
		model.addAttribute("msg2", result2);
		return "hellopojo"; // /WEB-INF/views/hellopojo.jsp 를 찾아감
	}
	
	@RequestMapping("/hello") // 웹 브라우저에서 /hello로 접속하면 실행
	public String hello(Model model) {
		model.addAttribute("message", "Hello World from Spring MVC!");
		return "hello"; // /WEB-INF/views/hello.jsp 를 찾아감
	}
}//HelloController