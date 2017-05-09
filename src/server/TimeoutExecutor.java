package server;

import java.util.function.Consumer;

/**
 * Created by vivace on 01.05.17.
 *
 * This class runs a given runnable in a new thread
 * and interrupts it if not finished in desired time.
 */
public class TimeoutExecutor {

    public static void execute(Runnable task, int timeout,
                 Consumer<InterruptedException> exceptionHandler) {
        try {
            Thread taskThread = new Thread(task);
            taskThread.start();
            taskThread.join(timeout);
            taskThread.interrupt();
        } catch (InterruptedException e) {
            exceptionHandler.accept(e);
        }
    }

    public static void execute(Runnable task, int timeout) {
        execute(task, timeout, ignored -> {});
    }
}
