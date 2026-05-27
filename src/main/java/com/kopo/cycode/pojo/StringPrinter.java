package com.kopo.cycode.pojo;

public class StringPrinter implements Printer {

	@Override
	public void print(String message) {

		System.out.println("Stirng message -> " + message);

	}// print

}// StringPrinter
