package server;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by vivace on 01.05.17.
 */
public interface Logger extends Consumer<String>, Closeable {

    void initialize() throws IOException;

    void accept(String s);

    void close() throws IOException;

    static Logger getDefaultLogger() {
        return new Logger() {
            @Override
            public void initialize() throws IOException {
                System.out.println("Default logger initialized");
            }

            @Override
            public void accept(String s) {
                System.out.println(s);
            }

            @Override
            public void close() throws IOException {
                System.out.println("Default logger closed");
            }
        };
    }
}
