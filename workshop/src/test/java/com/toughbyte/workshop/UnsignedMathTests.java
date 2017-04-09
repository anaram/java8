package com.toughbyte.workshop;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnsignedMathTests {

    private static final Logger LOG = LoggerFactory
            .getLogger(UnsignedMathTests.class);

    @Rule
    public DocumentationRule documentationRule = new DocumentationRule();

    @Test
    public void parse() {
        // tag::parse[]
        int unsignedInt = Integer.parseUnsignedInt("3147483647");
        LOG.info(String.format("Signed: %s", unsignedInt));
        LOG.info(String.format("Unsigned: %s",
                Integer.toUnsignedString(unsignedInt)));
        // end::parse[]
    }

    @Test
    public void divide() {
        // tag::divide[]
        int unsignedInt = Integer.parseUnsignedInt("3000000000");
        LOG.info(String.format("Signed: %s",
                Integer.toUnsignedString(unsignedInt / 2)));
        LOG.info(String.format("Unsigned: %s", Integer
                .toUnsignedString(Integer.divideUnsigned(unsignedInt, 2))));
        // end::divide[]
    }

    @Test(expected = NumberFormatException.class)
    public void failParseSignedInt() {
        // tag::failParseSignedInt[]
        try {
            Integer.parseInt("3000000000");
        } catch (NumberFormatException e) {
            LOG.info(e.toString());
            throw e;
        }
        // end::failParseSignedInt[]
    }
    
    @Test(expected = NumberFormatException.class)
    public void failParseUnsignedInt() {
        // tag::failParseUnsignedInt[]
        try {
            Integer.parseUnsignedInt("5000000000");
        } catch (NumberFormatException e) {
            LOG.info(e.toString());
            throw e;
        }
        // end::failParseUnsignedInt[]
    }

}
