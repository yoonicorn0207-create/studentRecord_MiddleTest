package cycode;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kopo.cycode.annot.Hello;
import com.kopo.cycode.annot.Printer;

public class HelloAnotTest {
	public static void main(String[] args) {

		// 1. 현재 beans.xml 위치에 맞게 경로 수정
		ApplicationContext context = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/beans_annot.xml");

		// 2. Hello Bean 가져오기
		Hello hello = (Hello) context.getBean("hello1331"); // G
//		Hello hello = (Hello) context.getBean(Hello.class); // componnet 이름 없이 Hello클래스 전체 다긁어와

		/* 결과 */
		// 결과 1) hello에 설정된 property인 name에 설정된 값으로 출력
		System.out.println(hello.sayHello());

		// 결과 2) hello에 매핑된 print
		hello.print();
		// 24라인 수정 및 추가
		String result = hello.getName();
		System.out.println("getName으로 가져온 값: " + result);
		
		Printer printer = context.getBean("sstringPrinter", Printer.class);
		// 결과 3) bean id = print로 설정된 부분에서 매핑됨
		printer.print("_test");

	}// main

}
