package com.toughbyte.workshop;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentationRule implements TestRule {

	private static final Logger LOG = LoggerFactory.getLogger(DocumentationRule.class);

	@Override
	public Statement apply(Statement base, Description description) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				String name = description.getMethodName() == null ? description.getMethodName()
						: description.getClassName() + "-" + description.getMethodName();
				try {
					LOG.info("tag::{}[]", name);
					base.evaluate();
				} finally {
					LOG.info("end::{}[]", name);
				}
			}
		};
	}

}
