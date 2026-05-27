package cycode;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kopo.cycode.product.ProductService;

public class JDBCTest_ProductService {

	public static void main(String[] args) {
		// 1. 현재 beans_product.xml 위치에 맞게 경로 수정
		ApplicationContext context = new GenericXmlApplicationContext(
				"file:src/main/webapp/WEB-INF/spring/beans_product.xml");

		// 2. ProductService Bean 가져오기
		ProductService prcs = (ProductService) context.getBean("productService");

		/* 결과 */
		String callResult = prcs.getMaxQtyByRegion("A01");
		System.out.println(callResult);

	}// main(Test 확인용)

}// JDBCTest_ProductService(ProductService테스트용)
