package com.toughbyte.workshop;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleToLongFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodReferencesTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodReferencesTest.class);

    @Rule
    public DocumentationRule documentationRule = new DocumentationRule();

    // tag::Student[]
    public static class Student {
        private String name;

        private int age;

        public static Student ofName(String name) {
            Student student = new Student();
            student.setName(name);
            return student;
        }

        public Student(int age) {
            super();
            this.age = age;
        }

        public Student() {
            super();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public static int compareByAge(Student left, Student right) {
            return Integer.compare(left.age, right.age);
        }
    }
    // end::Student[]

    @Test
    public void classMethods() {
        // tag::classMethods[]
        // tag::classMethods-1[]
        Supplier<Student> supplier = Student::new;
        IntFunction<Student> intFunction = Student::new;
        Function<String, Student> function = Student::ofName;

        BiConsumer<Student, String> biconsumer = Student::setName;
        Function<Student, String> getter = Student::getName;
        // end::classMethods-1[]

        {
            // tag::classMethods-2[]
            Student student = supplier.get();
            biconsumer.accept(student, "Mike");
            LOG.info(student.getName());
            // end::classMethods-2[]
        }

        {
            // tag::classMethods-3[]
            Student student = function.apply("Mike");
            LOG.info(getter.apply(student));
            // end::classMethods-3[]
        }
        // end::classMethods[]
    }

    @Test
    public void instanceMethods() {
        // tag::instanceMethods[]
        // tag::instanceMethods-1[]
        Student student = new Student();
        Supplier<String> getter = student::getName;
        Consumer<String> consumer = student::setName;
        // end::instanceMethods-1[]

        // tag::instanceMethods-2[]
        String name = getter.get();
        Assert.assertThat(name, Matchers.equalTo(null));
        consumer.accept("Molly");
        Assert.assertThat(student.getName(), Matchers.equalTo("Molly"));
        // end::instanceMethods-2[]
        // end::instanceMethods[]
    }

    @Test
    public void dateTimeFormatter() {
        // tag::dateTimeFormatter[]
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = formatter.parse("2017-02-27 09:00:00", LocalDateTime::from);
        LOG.info("" + time.atZone(ZoneId.of("GMT")).toInstant());
        // tag::dateTimeFormatter[]
    }

    @Test
    public void listAsConsumer() {
        // tag::listAsConsumer[]
        class StudentFactory {
            public void student(String name, int age, Consumer<Student> consumer) {
                Student student = new Student();
                student.setName(name);
                student.setAge(age);
                consumer.accept(student);
            }
        }
        StudentFactory factory = new StudentFactory();
        List<Student> students = new ArrayList<>();
        factory.student("Miko", 31, students::add);
        factory.student("Molly", 35, students::add);

        Assert.assertThat(students.size(), Matchers.equalTo(2));
        List<String> names = students.stream().map(Student::getName).collect(Collectors.toCollection(ArrayList::new));
        Assert.assertThat(names, Matchers.equalTo(Arrays.asList("Miko", "Molly")));
        // tag::listAsConsumer[]
    }

}
