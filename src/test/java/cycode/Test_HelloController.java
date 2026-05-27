package cycode;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kopo.cycode.pojo.Hello;
import com.kopo.cycode.pojo.Printer;

public class Test_HelloController {

	public static void main(String[] args) {

		// 1. 현재 beans.xml 위치에 맞게 경로 수정
		ApplicationContext context = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/beans.xml");

		// 2. Hello Bean 가져오기
		Hello hello = (Hello) context.getBean("hello");

		/* 결과 */
		// 결과 1) hello에 설정된 property인 name에 설정된 값으로 출력
		System.out.println(hello.sayHello());

		// 결과 2) hello에 매핑된 print
		hello.print();

		Printer printer = context.getBean("stringPrinter", Printer.class);
		// 결과 3) bean id = print로 설정된 부분에서 매핑됨
		printer.print("_test");

	}// main

}// HelloController
