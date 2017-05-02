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

/**
 * A resource lock manager.
 */
public interface ResourceLockManager {

	/**
	 * Acquires a lock for the given resource name using the given lock name.
	 * 
	 * @param resourceName
	 *            the resource name to be locked on
	 * @param lockName
	 *            the name of the lock
	 * @return <code>true</code> if the lock was acquired, <code>false</code>
	 *         otherwise
	 * @throws ResourceLockException
	 *             in case an error occurs
	 */
	boolean acquireLock(String resourceName, String lockName) throws ResourceLockException;

	/**
	 * Acquires a lock for the given resource name using the given lock name.
	 * 
	 * @param resourceName
	 *            the resource name to be locked on
	 * @param lockName
	 *            the name of the lock
	 * @param ttl
	 *            the time the lock exists in seconds
	 * @return <code>true</code> if the lock was acquired, <code>false</code>
	 *         otherwise
	 * @throws ResourceLockException
	 *             in case an error occurs
	 */
	boolean acquireLock(String resourceName, String lockName, int ttl) throws ResourceLockException;

	/**
	 * Releases a lock for the given resource name using the given lock name.
	 * 
	 * @param resourceName
	 *            the resource name to be locked on
	 * @param lockName
	 *            the name of the lock
	 * @throws ResourceLockException
	 *             in case an error occurs
	 */
	void releaseLock(String resourceName, String lockName) throws ResourceLockException;

}
