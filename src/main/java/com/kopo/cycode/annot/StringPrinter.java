package com.kopo.cycode.annot;

import org.springframework.stereotype.Component;

@Component("sstringPrinter") // bean id를 springPrinter로 지정
public class StringPrinter implements Printer {

	@Override
	public void print(String message) {

		System.out.println("Stirng message -> " + message);

	}// print

}// StringPrinter
