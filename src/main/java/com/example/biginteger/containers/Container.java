package com.example.biginteger.containers;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by mmonti on 2/3/21.
 */
public interface Container {

    Container NOOP = new NoOpContainer();

    /**
     *
     */
    default Container start() {
        return NOOP.start();
    }

    /**
     *
     */
    default void stop() {
        NOOP.stop();
    }

    /**
     *
     * @return
     */
    default boolean isRunning() {
        return NOOP.isRunning();
    }

    @Slf4j
    class NoOpContainer implements Container {
        @Override
        public Container start() {
            log.info("noop - start called");
            return this;
        }

        @Override
        public void stop() {
            log.info("noop - stop called");
        }

        @Override
        public boolean isRunning() {
            log.info("noop - isRunning called");
            return false;
        }
    }
}
