package server;

/**
 * Created by vivace on 01.05.17.
 *
 * This class runs a given runnable in a new thread
 * and interrupts it if not finished in desired time.
 */
public class Counter implements Runnable {

    private static final int COUNT_STEP = 100;

    private int count;
    private int currentCount;
    private Runnable countdownHandler;

    Counter(int count, Runnable countdownHandler) {
        this.count = this.currentCount = count / COUNT_STEP;
        this.countdownHandler = countdownHandler;
    }

    @Override
    public void run() {
        try {
            while (0 < currentCount) {
                Thread.sleep(COUNT_STEP);
                currentCount--;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countdownHandler.run();
    }

    synchronized void reset() {
        currentCount = count;
    }
}
