package com.kopo.cycode.pojo;

public class Hello {

	private String name;
	private Printer printer;

	/* setter 영역 */
	public void setName(String name) {
		this.name = name;
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
