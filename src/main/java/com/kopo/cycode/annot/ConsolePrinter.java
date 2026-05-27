package com.kopo.cycode.annot;

import org.springframework.stereotype.Component;

@Component("consolePrinter") // bean id를 consolePrinter로 지정
public class ConsolePrinter implements Printer {

	@Override
	public void print(String message) {

		System.out.println("console call : " + message);

	}// print

}// ConsolePrinter
