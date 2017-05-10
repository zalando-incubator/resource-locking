/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Zalando SE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.zalando.resourcelock.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.zalando.resourcelock.ResourceLockManagerJdbcFunctionImpl;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceLockManagerJdbcTest {

    @Autowired
    private ResourceLockManagerJdbcFunctionImpl manager;

    @Test
    public void testLocks() throws InterruptedException {

        // acquire lock
        boolean result = manager.acquireLock("a", "a");
        assertTrue("Lock could not be acquired", result);

        // acquire lock and fail
        result = manager.acquireLock("a", "a");
        assertFalse("Lock was acquired", result);

        // release lock and successfully reacquire lock
        manager.releaseLock("a", "a");
        result = manager.acquireLock("a", "a");
        assertTrue("Lock could not be acquired", result);
        manager.releaseLock("a", "a");

        // acquire lock with ttl
        result = manager.acquireLock("a", "a", 1);
        result = manager.acquireLock("a", "a", 1);
        assertFalse("Lock was acquired", result);
        Thread.sleep(1200);
        result = manager.acquireLock("a", "a");
        assertTrue("Lock could not be acquired", result);
        manager.releaseLock("a", "a");
    }
}
