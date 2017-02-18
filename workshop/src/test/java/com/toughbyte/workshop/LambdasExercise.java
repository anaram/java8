package com.toughbyte.workshop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.toughbyte.workshop.MethodReferencesTest.Student;

public class LambdasExercise {

    public static class CsvReader<T> {
        private final Supplier<T> supplier;

        private Map<String, BiConsumer<T, String>> fields = new HashMap<>();

        public CsvReader(Supplier<T> supplier) {
            super();
            this.supplier = supplier;
        }

        public void read(Path path, Consumer<T> consumer) throws IOException {
            List<String> lines = Files.readAllLines(path);
            String[] headers = lines.remove(0).split(",");
            for (String line : lines) {
                T object = supplier.get();
                String[] parts = line.split(",");
                for (int i = 0; i < parts.length; i++) {
                    BiConsumer<T, String> setter = fields.get(headers[i]);
                    if (setter != null) {
                        setter.accept(object, parts[i]);
                    }
                }
                consumer.accept(object);
            }
        }
    }

    private CsvReader<Student> reader() {
        CsvReader<Student> reader = new CsvReader<>(Student::new);
        // FIXME: map the fields

        reader.fields.put("name", Student::setName);
        reader.fields.put("age", (s, a) -> s.setAge(Integer.valueOf(a)));
        return reader;
    }

    private Path path = new File(LambdasExercise.class.getResource("students.csv").getFile()).toPath();

    @Test
    public void testCollectNames() throws IOException {
        List<String> names = new ArrayList<>();
        reader().read(path, s -> names.add(s.getName()));
        Assert.assertThat(names, Matchers.equalTo(Arrays.asList("Mike", "Molly", "Jones")));
    }

    @Test
    public void testCollectAges() throws IOException {
        List<Integer> ages = new ArrayList<>();
        reader().read(path, s -> ages.add(s.getAge()));
        Assert.assertThat(ages, Matchers.equalTo(Arrays.asList(31, 32, 55)));
    }
}
