package com.toughbyte.workshop;

import java.beans.IntrospectionException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.stream.Stream;

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

    @Rule
    public DocumentationRule documentationRule = new DocumentationRule();

    @Test
    public void structure() throws IntrospectionException {
        {
            // tag::structure-lambda[]
            LongUnaryOperator op = val -> val + 3;
            LOG.info("lambda: " + op.getClass());

            Field[] fields = op.getClass().getDeclaredFields();
            for (Field field : fields) {
                LOG.info("field: " + field);
            }
            // end::structure-lambda[]
        }
        {
            // tag::structure-class[]
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
            // end::structure-class[]
        }
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
        staticLikeStructure();
    }

    public static void staticLikeStructure() {
        // tag::staticLikeStructure[]
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
        // end::staticLikeStructure[]
    }

    @Test
    public void serialize() throws IOException, ClassNotFoundException {
        // tag::serialize[]
        SerializableUnaryOperator op = (x, y) -> x * y;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(baos);
        stream.writeObject(op);
        stream.flush();
        LOG.info("bytes: " + baos.toByteArray().length);
        // Files.write(Paths.get("lambda"), baos.toByteArray());

        ObjectInputStream is = new ObjectInputStream(
                new ByteArrayInputStream(baos.toByteArray()));
        Object read = is.readObject();
        LOG.info("read: " + read);
        LOG.info(
                "result: " + ((DoubleBinaryOperator) read).applyAsDouble(5, 6));
        // tag::serialize[]
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
        // tag::deserialize[]
    }

    // tag::serializable[]
    public interface SerializableUnaryOperator
            extends DoubleBinaryOperator, Serializable {

    }
    // end::serializable[]
}
