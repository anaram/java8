package com.toughbyte.workshop;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CsvReader<T> {
    private final Supplier<T> supplier;

    private Map<String, BiConsumer<T, String>> fields = new HashMap<>();

    public CsvReader(Supplier<T> supplier) {
        super();
        this.supplier = supplier;
    }

    public void read(Path path, Consumer<T> consumer) {
        try {
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
        }} catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Map<String, BiConsumer<T, String>> getFields() {
        return fields;
    }

    public void setFields(Map<String, BiConsumer<T, String>> fields) {
        this.fields = fields;
    }
}