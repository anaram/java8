package com.toughbyte.workshop.slides;

import org.junit.Test;

import com.toughbyte.workshop.DocumentationRule;

public class FunctionalInterfacesTest {

	public DocumentationRule documentationRule = new DocumentationRule();

	@Test
	public void runnable() {
		// tag::runnable[]
		Runnable runnable = () -> System.out.println("Hello World!");
		runnable.run();
		// end::runnable[]
	}
}
