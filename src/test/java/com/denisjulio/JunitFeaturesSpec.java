package com.denisjulio;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * Class for experimenting with JUnit 5 features in tests non related to the
 * application
 */
class JunitFeaturesSpec {

    @Test
    @DisplayName("runs the test in less than a second")
    void finishLessThanASec() throws Exception {
        assertTimeout(Duration.ofSeconds(1), () -> Thread.sleep(1000));
    }

    @Test
    void abortsPreemptvelyAfterTimeout() throws Exception {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> Thread.sleep(500));
    }

    @RepeatedTest(value = 10, name = "repetition number {currentRepetition}/{totalRepetitions}")
    void repeatedTest() throws Exception {
      assertTrue(true);
    }
}
