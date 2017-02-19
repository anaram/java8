package com.toughbyte.workshop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamsTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(StreamsTest.class);

    @Rule
    public DocumentationRule documentationRule = new DocumentationRule();

    @Test
    public void intStream() {
        // tag::intStream[]
        IntStream.range(0, 3).forEach(i -> LOG.info("Integer " + i));
        LOG.info("--");
        IntStream.range(0, 3).mapToObj(i -> "Integer " + i).forEach(LOG::info);
        // end::intStream[]
    }

    @Test
    public void collect() {
        // @formatter:off
        // tag::collect[]
        // tag::collect-1[]
        class NumberWord {
            private String text;

            public NumberWord(String text) {
                super();
                this.text = text;
            }
        }
        List<NumberWord> list = Stream.of("one", "two", "three")
                .map(NumberWord::new)
                .collect(Collectors.toList());
        // end::collect-1[]
        // tag::collect-2[]
        ArrayList<NumberWord> arrayList = list
                .stream()
                .collect(Collectors.toCollection(ArrayList::new));
        Optional<NumberWord> any = arrayList
                .stream()
                .filter(nw -> nw.text.length() > 4).findAny();
        // also: findFirst
        Assert.assertThat(any.get().text, Matchers.equalTo("three"));
        // end::collect-2[]
        // end::collect[]
        // @formatter:on
    }

    @Test(expected = IllegalStateException.class)
    public void limit() {
        // tag::limit[]
        Stream.generate(() -> "hello").limit(2).forEach(LOG::info);

        class LogHelper {
            int max = 3;
            int count = 0;

            public void log(String msg) {
                if (count++ < max) {
                    LOG.info(msg);
                } else {
                    throw new IllegalStateException();
                }
            }
        }
        Stream.generate(() -> "ahoy").forEach(new LogHelper()::log);
        // end::limit[]
    }

    @Test
    public void reduce() {
        // tag::reduce[]
        OptionalInt sum = IntStream.of(1, 2, 6, 7, 8).reduce(Integer::sum);
        sum.ifPresent(s -> LOG.info("Sum is: " + sum));
        Assert.assertThat(sum.getAsInt(), Matchers.equalTo(24));
        // end::reduce[]
    }

    @Test
    public void reduceFailed() {
        // tag::reduceFailed[]
        OptionalInt missing = IntStream.of(1, 3, 7).filter(i -> i > 10)
                .reduce(Integer::sum);
        Assert.assertThat(missing.orElse(-1), Matchers.equalTo(-1));
        Assert.assertThat(missing.orElseGet(() -> -1), Matchers.equalTo(-1));
        try {
            missing.orElseThrow(() -> new IllegalStateException("no"));
            throw new IllegalStateException("yes");
        } catch (IllegalStateException e) {
            Assert.assertThat(e.getMessage(), Matchers.equalTo("no"));
        }
        // end::reduceFailed[]
    }

    @Test
    public void groupingList() {
        // tag::groupingList[]
        List<Tuple> list = IntStream.range(0, 10)
                .mapToObj(i -> new Tuple(i % 2 == 0 ? "even" : "odd", i))
                .collect(Collectors.toList());

        Map<String, List<Tuple>> mapped = list.stream()
                .collect(Collectors.groupingBy(Tuple::getKey));

        LOG.info("" + mapped);
        // end::groupingList[]
    }
    
    @Test
    public void groupingReduce() {
        // tag::groupingReduce[]
        List<Tuple> list = IntStream.range(0, 10)
                .mapToObj(i -> new Tuple(i % 2 == 0 ? "even" : "odd", i))
                .collect(Collectors.toList());

        Map<String, Integer> mapped = list.stream()
                .collect(Collectors.groupingBy(Tuple::getKey,
                        Collectors.reducing(0, Tuple::getValue, Integer::sum)));

        LOG.info("" + mapped);
        // end::groupingReduce[]
    }
    
    @Test
    public void groupingPartitioning() {
        // tag::groupingPartitioning[]
        List<Tuple> list = IntStream.range(0, 10)
                .mapToObj(i -> new Tuple(i % 2 == 0 ? "even" : "odd", i))
                .collect(Collectors.toList());

        Map<Boolean, List<String>> mapped = list.stream().map(Tuple::getKey)
                .collect(Collectors.partitioningBy("even"::equals));

        LOG.info("" + mapped);
        // end::groupingPartitioning[]
    }


    // tag::tuple[]
    public static final class Tuple {
        private String key;

        private int value;

        public Tuple(String key, int value) {
            super();
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
    // end::tuple[]

}
