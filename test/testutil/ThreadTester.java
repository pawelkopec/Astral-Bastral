package testutil;

/**
 * Created by vivace on 23.04.17.
 *
 * Class enabling catching asserion errors
 * from another thread.
 */
public class ThreadTester {
    private Thread thread;
    private volatile AssertionError exc;

    public ThreadTester(Runnable runnable) {
        thread = new Thread(() -> {
            try {
                runnable.run();
            } catch (AssertionError e) {
                exc = e;
            }
        });
    }

    public void startThread() {
        thread.start();
    }

    public void checkAssertion() throws InterruptedException {
        thread.join();
        if (exc != null) {
            throw exc;
        }
    }
}
