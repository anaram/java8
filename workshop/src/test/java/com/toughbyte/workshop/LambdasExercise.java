package com.toughbyte.workshop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.toughbyte.workshop.MethodReferencesTest.Student;

@Ignore
public class LambdasExercise {

    private CsvReader<Student> reader() {
        // FIXME:
        CsvReader<Student> reader = new CsvReader<>(null);
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
