package com.kopo.cycode.annot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("hello1331")
public class Hello {

	@Value("CCYcode_0515")
	private String name;
	

	@Autowired
	@Qualifier("sstringPrinter")
	private Printer printer;

	/* setter 영역 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* getter 영역 */
	public String getName() {
		return name;
	}

	public void setPrinter(Printer printer) {
		this.printer = printer;
	}

	public String sayHello() {
		return "Hello : " + name;
	}

	/* 출력영역 */
	public void print() {
		this.printer.print(sayHello());
	}

}// Hello
