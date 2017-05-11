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
package org.zalando.resourcelock;

import lombok.extern.slf4j.Slf4j;
import org.zalando.boot.etcd.EtcdClient;
import org.zalando.boot.etcd.EtcdError;
import org.zalando.boot.etcd.EtcdErrorCodes;
import org.zalando.boot.etcd.EtcdException;

import java.io.File;

/**
 * Implementation of resource lock manager that uses etcd as backend.
 */
@Slf4j
public class ResourceLockManagerEtcdImpl implements ResourceLockManager {

	/**
	 * key prefix in etcd
	 */
	private static final String RESOURCE_LOCK_PREFIX = "resource-locks";

	/**
	 * etcd client
	 */
	private EtcdClient etcdClient;

	/**
	 * application name
	 */
	private String applicationName;

	/**
	 * Creates a new ResourceLockManagerEtcdImpl with the given etcd client and
	 * application name.
	 * 
	 * @param etcdClient
	 *            the etcd client
	 * @param applicationName
	 *            the application name
	 */
	public ResourceLockManagerEtcdImpl(EtcdClient etcdClient, String applicationName) {
		this.etcdClient = etcdClient;
		this.applicationName = applicationName;
	}

	private String createApplicationKey(String name) {
		return applicationName + "/" + name;
	}

	@Override
	public boolean acquireLock(String resourceName, String lockName) {
		String key = createApplicationKey(RESOURCE_LOCK_PREFIX + File.separator + resourceName);
		try {
			etcdClient.compareAndSwap(key, lockName, false);
			return true;
		} catch (EtcdException e) {
			log.warn("Unable to acquirer lock {} for resource {} with key {}.", lockName, resourceName, key, e);
			return false;
		}
	}

	@Override
	public boolean acquireLock(String resourceName, String lockName, int ttl) {
		String key = createApplicationKey(RESOURCE_LOCK_PREFIX + File.separator + resourceName);
		try {
			etcdClient.compareAndSwap(key, lockName, ttl, false);
			return true;
		} catch (EtcdException e) {
			log.warn("Unable to acquirer lock {} for resource {} with key {}.", lockName, resourceName, key, e);
			return false;
		}
	}

	@Override
	public void releaseLock(String resourceName, String lockName) {
		String key = createApplicationKey(RESOURCE_LOCK_PREFIX + File.separator + resourceName);
		try {
			etcdClient.compareAndDelete(key, lockName);
		} catch (EtcdException e) {
			EtcdError error = e.getError();

			// error code 100 (Key not found) is ok, all others is an error
			if (error == null || error.getErrorCode() != EtcdErrorCodes.ECODE_KEY_NOT_FOUND) {
				throw new ResourceLockException("Could not release lock with key " + key + ".", e);
			}
		}
	}

}
