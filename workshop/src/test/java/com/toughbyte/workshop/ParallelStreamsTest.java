package com.toughbyte.workshop;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParallelStreamsTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(ParallelStreamsTest.class);

    @Rule
    public DocumentationRule documentationRule = new DocumentationRule();

    @Test
    public void creating() {
        // tag::creating[]
        Arrays.asList("Tim", "Tom").parallelStream();
        Stream.of("Tim", "Tom").parallel();
        StreamSupport.stream(Arrays.asList("Tim", "Tom").spliterator(), true);
        // end::creating[]
    }

    @Test
    public void parallelForEach() {
        // tag::parallelForEach[]
        // Result is not deterministic
        List<Integer> result = Collections.synchronizedList(new ArrayList<>());
        IntStream.range(0, 8).parallel().forEach(result::add);
        LOG.info("" + result);
        // end::parallelForEach[]
    }

    @Test
    public void parallelForEachOrdered() {
        // tag::parallelForEachOrdered[]
        List<Integer> result = Collections.synchronizedList(new ArrayList<>());
        IntStream.range(0, 8).parallel().forEachOrdered(result::add);
        LOG.info("" + result);
        // end::parallelForEachOrdered[]
    }

    @Test
    public void parallelCollect() {
        // tag::parallelCollect[]
        List<Integer> result = IntStream.range(0, 8).parallel()
                .mapToObj(Integer::valueOf).collect(Collectors.toList());
        LOG.info("" + result);
        // end::parallelCollect[]
    }

    @Test
    public void parallelCollectUnordered() {
        // tag::parallelCollectUnordered[]
        // Result is not deterministic
        List<Integer> result = IntStream.range(0, 8).parallel().unordered()
                .mapToObj(Integer::valueOf).collect(Collectors.toList());
        LOG.info("" + result);
        // end::parallelCollectUnordered[]
    }

    @Test
    public void threadNames() {
        // tag::threadNames[]
        IntStream.range(0, 8).parallel().forEach(i -> {
            LOG.info("" + Thread.currentThread().getName());
        });
        // end::threadNames[]
    }

    @Test
    public void customForkJoinPool() {
        // tag::customForkJoinPool[]
        ForkJoinPool pool = new ForkJoinPool(2);
        ForkJoinTask<?> task = pool.submit(() -> {
            LOG.info("Adding in thread: " + Thread.currentThread().getName());
            IntStream.range(0, 4).parallel().forEach(i -> {
                LOG.info("" + Thread.currentThread().getName());
            });
        });
        task.join();
        // end::customForkJoinPool[]
    }

    @Test
    public void summaryStatistics() {
        // tag::summaryStatistics[]
        IntSummaryStatistics statistics = IntStream.range(0, 9876).parallel()
                .summaryStatistics();
        LOG.info("" + statistics);
        // end::summaryStatistics[]
    }

}
