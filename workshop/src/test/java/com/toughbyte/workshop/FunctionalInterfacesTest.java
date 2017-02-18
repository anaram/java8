package com.toughbyte.workshop;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleToLongFunction;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionalInterfacesTest {

    private static final Logger LOG = LoggerFactory.getLogger(FunctionalInterfacesTest.class);

    @Rule
    public DocumentationRule documentationRule = new DocumentationRule();

    @Test
    public void threadBefore() {
        // tag::threadBefore[]
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LOG.info("Hello World!");
            }
        });
        thread.start();
        // end::threadBefore[]
    }

    @Test
    public void threadAfter() {
        // tag::threadAfter[]
        Thread thread = new Thread(() -> LOG.info("Hello World!"));
        thread.start();
        // end::threadAfter[]
    }

    @Test
    public void streamBefore() {
        // tag::streamBefore[]
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            strings.add("" + i);
        }
        // end::streamBefore[]
    }

    @Test
    public void streamAfter() {
        //@formatter:off
		@SuppressWarnings("unused")
		// tag::streamAfter[]
		List<String> strings = IntStream.range(0, 5) // <1>
		    .mapToObj(String::valueOf)               // <2>
		    .collect(Collectors.toList());           // <3>
		// end::streamAfter[]
		//@formatter:on
    }

    @Test
    public void runnable() {
        // tag::runnable[]
        Runnable runnable = () -> LOG.debug("Hello World!");
        runnable.run();
        // end::runnable[]
    }

    @Test
    public void runnableBefore() {
        // tag::runnableBefore[]
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                LOG.debug("Hello World!");
            }
        };
        runnable.run();
        // end::runnableBefore[]
    }

    @Test
    public void supplier() {
        // tag::supplier[]
        Supplier<Integer> supplier = () -> new Integer(5);
        LOG.info("Value: " + supplier.get());
        // end::supplier[]
    }

    @Test
    public void consumer() {
        // tag::consumer[]
        Consumer<String> consumer = s -> LOG.info(s);
        consumer.accept("Hello World!");
        // end::consumer[]
    }

    @Test
    public void function() {
        // tag::function[]
        Function<Integer, String> function = i -> "integer is: " + i;
        LOG.info(function.apply(3));
        // end::function[]
    }

    @Test
    public void predicate() {
        // tag::predicate[]
        Predicate<Integer> function = i -> i < 5;
        LOG.info("3 < 5: " + function.test(3));
        LOG.info("6 < 5: " + function.test(6));
        // end::predicate[]
    }

    @Test
    public void primitive() {
        // tag::primitive[]
        DoubleToLongFunction round = d -> Math.round(d);
        IntUnaryOperator increment = i -> i + 1;
        LOG.info("Values: " + round.applyAsLong(5.8) + ", " + increment.applyAsInt(3));
        // end::primitive[]
    }

    @Test
    public void biConsumer() {
        // tag::biConsumer[]
        BiConsumer<Object, Object> bc = (x, y) -> {
            LOG.info("x: " + x);
            LOG.info("y: " + y);
        };
        bc.accept("foo", "bar");
        // end::biConsumer[]
    }

    @Test
    public void biFunction() {
        // tag::biFunction[]
        BiFunction<Object, Object, String> bc = (x, y) -> String.valueOf(x) + " vs " + String.valueOf(y);
        LOG.info(bc.apply("hello", 5.7));
        // end::biFunction[]
    }

    @Test
    public void local() {
        // tag::local[]
        for (int i = 0; i < 2; i++) {
            final int x = i;
            IntUnaryOperator operator = j -> x + j;
            LOG.info("i: " + i + " value: " + operator.applyAsInt(3));
        }
        // end::local[]
    }

    @SuppressWarnings("unused")
    @Test
    public void syntax() {
        // tag::syntax[]
        Function<String, String> f1 = x -> x;
        Function<String, String> f2 = (x) -> x;
        Function<String, String> f3 = (String x) -> x;
        Function<String, String> f4 = (String x) -> {
            return x;
        };
        Function<String, String> f5 = Function.identity();
        // end::syntax[]
    }

    @Test
    public void chaining() {
        // tag::chaining[]
        IntUnaryOperator operator = i -> i + 5;
        IntUnaryOperator chained = operator.andThen(i -> i * 2); // <1>
        LOG.info("Calc: " + chained.applyAsInt(1));
        // end::chaining[]
    }
}
