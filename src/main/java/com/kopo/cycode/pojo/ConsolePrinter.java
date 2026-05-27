package com.kopo.cycode.pojo;

public class ConsolePrinter implements Printer {

	@Override
	public void print(String message) {

		System.out.println("console call : " + message);

	}// print

}// ConsolePrinter
