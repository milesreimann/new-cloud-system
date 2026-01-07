package io.github.milesreimann.cloudsystem.application.handler;

import io.github.milesreimann.cloudsystem.application.exception.MaxReconnectAttemptsExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Miles R.
 * @since 07.01.26
 */
public class NodeWatcherReconnectHandler {
    private static final Logger LOG = LoggerFactory.getLogger(NodeWatcherReconnectHandler.class);

    private final int maxAttempts;
    private final long delayMs;

    private final AtomicInteger attempts = new AtomicInteger(0);

    public NodeWatcherReconnectHandler(int maxAttempts, long delayMs) {
        this.maxAttempts = maxAttempts;
        this.delayMs = delayMs;
    }

    public void attemptReconnect(Runnable reconnectRunnable) throws InterruptedException {
        int currentAttempt = attempts.incrementAndGet();

        if (currentAttempt > maxAttempts) {
            throw new MaxReconnectAttemptsExceededException("NodeWatcher reached max reconnect attempts (" + maxAttempts + ")");
        }

        LOG.info("Attempting to reconnect NodeWatcher (attempt {}/{})", currentAttempt, maxAttempts);

        try {
            Thread.sleep(delayMs);
            reconnectRunnable.run();
            attempts.set(0);
            LOG.info("NodeWatcher reconnected successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    public void reset() {
        attempts.set(0);
    }

    public boolean shouldReconnect() {
        return attempts.get() < maxAttempts;
    }
}
