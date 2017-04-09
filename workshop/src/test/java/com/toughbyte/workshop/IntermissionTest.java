package com.toughbyte.workshop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;
import java.util.stream.Stream;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * More technical stuff about lambdas.
 * 
 * @author teemu
 *
 */
public class IntermissionTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(IntermissionTest.class);

    // tag::INFO[]
    private static final Consumer<Object> INFO = o -> LOG.info("" + o);
    // end::INFO[]

    @Rule
    public DocumentationRule documentationRule = new DocumentationRule();

    @Test
    public void structureClass() {
        // tag::structureClass[]
        LongUnaryOperator op = new LongUnaryOperator() {

            @Override
            public long applyAsLong(long operand) {
                return operand + 3;
            }
        };
        LOG.info("anonymous inner class: " + op.getClass());

        Field[] fields = op.getClass().getDeclaredFields();
        for (Field field : fields) {
            LOG.info("field: " + field);
        }
        // end::structureClass[]
    }

    @Test
    public void structureLambda() {
        // tag::structureLambda[]
        LongUnaryOperator op = val -> val + 3;
        LOG.info("lambda: " + op.getClass());

        Field[] fields = op.getClass().getDeclaredFields();
        for (Field field : fields) {
            LOG.info("field: " + field);
        }
        // end::structureLambda[]
    }

    @Test
    public void structureLogged() {
        // tag::structureLogged[]
        Consumer<Field> logger = (Consumer<Field>) f -> LOG
                .info("logged: " + f);
        LongUnaryOperator op = val -> val + 3;
        LOG.info("" + op.getClass());
        Stream.of(op.getClass().getDeclaredFields()).forEach(logger);
        // end::structureLogged[]

    }

    @Test
    public void staticLikeStructureTest() {
        staticContext();
    }

    // tag::staticContext[]
    public static void staticContext() {
        LongUnaryOperator op = new LongUnaryOperator() {

            @Override
            public long applyAsLong(long operand) {
                return operand + 3;
            }
        };
        LOG.info("static: " + op.getClass());

        Field[] fields = op.getClass().getDeclaredFields();
        for (Field field : fields) {
            LOG.info("'static' anonymous inner class: " + field);
        }
    }
    // end::staticContext[]

    // tag::structureUnary[]
    private double priv = 1.0;

    @Test
    public void structureUnary() {
        double x = 1.0;
        long y = 2;
        DoubleUnaryOperator op = arg -> arg * x * y * priv;
        LOG.info("" + op.getClass());
        Stream.of(op.getClass().getDeclaredFields()).forEach(INFO);
    }
    // end::structureUnary[]

    @Test
    public void serialize() throws IOException, ClassNotFoundException {
        // tag::serialize[]
        SerializableDoubleBinaryOperator op = (x, y) -> x + y;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(baos);
        stream.writeObject(op);
        stream.flush();
        LOG.info("bytes: " + baos.toByteArray().length);
        // Files.write(Paths.get("src/test/resources/com/toughbyte/workshop/serialize.lambda"),
        // baos.toByteArray());

        ObjectInputStream is = new ObjectInputStream(
                new ByteArrayInputStream(baos.toByteArray()));
        Object read = is.readObject();
        LOG.info("read: " + read);
        LOG.info(
                "result: " + ((DoubleBinaryOperator) read).applyAsDouble(5, 6));
        // end::serialize[]
    }

    @Test
    public void deserialize() throws IOException, ClassNotFoundException {
        // tag::deserialize[]
        URL resource = StreamsTest.class.getResource("serialize.lambda");
        InputStream ris = resource.openStream();
        ObjectInputStream is = new ObjectInputStream(ris);
        Object read = is.readObject();
        LOG.info("read: " + read);
        LOG.info(
                "result: " + ((DoubleBinaryOperator) read).applyAsDouble(5, 6));
        // end::deserialize[]
    }

    @Test
    public void nashorn() throws ScriptException {
        // tag::nashorn[]
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        Bindings eval = (Bindings) engine.eval("f = function(name) { return name.length() }");
        LOG.info("" + eval.getClass());
        LOG.info("Length: " + engine.eval("f('hello')"));
        // end::nashorn[]
    }
    
    // tag::serializable[]
    @FunctionalInterface
    public interface SerializableDoubleBinaryOperator
            extends DoubleBinaryOperator, Serializable {
    }

    @FunctionalInterface
    public interface SerializableDoubleUnaryOperator
            extends DoubleUnaryOperator, Serializable {
    }
    // end::serializable[]
}
