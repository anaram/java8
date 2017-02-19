package com.toughbyte.workshop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.toughbyte.workshop.MethodReferencesTest.Student;

public class StreamsExercise {

    private static final Logger LOG = LoggerFactory.getLogger(StreamsExercise.class);
    
    private CsvStreamer<Student> streamer() {
        CsvStreamer<Student> streamer = new CsvStreamer<>(Student::new);
        streamer.getFields().put("name", Student::setName);
        streamer.getFields().put("ago", (s, a) -> s.setAge(Integer.valueOf(a)));
        return streamer;
    }

    private Path path = new File(StreamsExercise.class.getResource("students.csv").getFile()).toPath();

    @Test
    public void testCollectNames() throws IOException {
        streamer().stream(path).map(Student::getName).forEach(LOG::info);
    }
}
