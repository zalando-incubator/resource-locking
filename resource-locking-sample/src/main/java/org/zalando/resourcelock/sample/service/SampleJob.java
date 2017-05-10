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
package org.zalando.resourcelock.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zalando.resourcelock.ResourceLockManager;

import java.util.UUID;

@Component
public class SampleJob {

	@Autowired
	private ResourceLockManager resourceLockManager;

	@Scheduled(fixedDelay = 1000)
	public void doSomething() {
		UUID lockName = UUID.randomUUID();
		try {
			if (resourceLockManager.acquireLock("sample-job", lockName.toString())) {
				Thread.sleep(5000);
			}
		} catch (InterruptedException e) {
			// TODO logging
		} finally {
			resourceLockManager.releaseLock("sample-job", lockName.toString());
		}
	}
}
