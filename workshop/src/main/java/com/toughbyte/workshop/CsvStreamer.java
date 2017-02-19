package com.toughbyte.workshop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.channels.ClosedChannelException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CsvStreamer<T> {
    private final Supplier<T> supplier;

    private Map<String, BiConsumer<T, String>> fields = new HashMap<>();

    public CsvStreamer(Supplier<T> supplier) {
        super();
        this.supplier = supplier;
    }

    public Stream<T> stream(Path path) {
        try (InputStream is = Files.newInputStream(path)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String[] headers = reader.readLine().split(",");
            AbstractSpliterator<T> spliterator = new Spliterators.AbstractSpliterator<T>(0, 0) {

                @Override
                public boolean tryAdvance(Consumer<? super T> action) {
                    try {
                        String line = reader.readLine();
                        if (line == null) {
                            return false;
                        }
                        
                        T object = supplier.get();
                        
                        String[] parts = line.split(",");
                        
                        for (int i = 0; i < parts.length; i++) {
                            BiConsumer<T, String> setter = fields.get(headers[i]);
                            if (setter != null) {
                                setter.accept(object, parts[i]);
                            }
                        }
                        action.accept(object);
                        
                        return true;
                    } catch(ClosedChannelException e) {
                        return false;
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            };

            return StreamSupport.stream(spliterator, false);

        } catch (IOException e) {
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