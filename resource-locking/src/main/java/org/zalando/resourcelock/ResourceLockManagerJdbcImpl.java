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

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of resource lock manager that uses jdbc as backend.
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ResourceLockManagerJdbcImpl implements ResourceLockManager {

    private static final String TABLE_NAME = "resource_lock";

    private static final String COLUMN_RESOURCE_NAME = "resource_name";

    private static final String COLUMN_LOCK_NAME = "lock_name";

    private static final String COLUMN_CREATED_AT = "created_at";

    private static final String COLUMN_EXPIRES_AT = "expires_at";

    private JdbcTemplate template;

    /**
     * Creates a new ResourceLockManagerJdbcImpl with the given JDBC template.
     *
     * @param template
     *            the JDBC template
     */
    public ResourceLockManagerJdbcImpl(JdbcTemplate template) {
        this.template = template;
    }

    /**
     * {@inheritDoc}
     *
     * @see ResourceLockManager#acquireLock(String, String)
     */
    @Override
    public boolean acquireLock(String resourceName, String lockName) {
        return acquireLock(resourceName, lockName, 0);
    }

    /**
     * {@inheritDoc}
     *
     * @see ResourceLockManager#acquireLock(String, String, int)
     */
    @Override
    public boolean acquireLock(String resourceName, String lockName, int ttl) {
        try {
            Date now = new Date();

            // delete any expired resource locks upfront
            template.update(String.format("DELETE FROM %s WHERE %s = ? AND %s IS NOT NULL AND %s < ?", TABLE_NAME,
                    COLUMN_RESOURCE_NAME, COLUMN_EXPIRES_AT, COLUMN_EXPIRES_AT), resourceName, now);
            // try to insert a resource lock
            template.update(
                    String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)", TABLE_NAME,
                            COLUMN_RESOURCE_NAME, COLUMN_LOCK_NAME, COLUMN_CREATED_AT, COLUMN_EXPIRES_AT),
                    resourceName, lockName, now, ttl == 0 ? null : DateUtils.addSeconds(now, ttl));
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see ResourceLockManager#releaseLock(String, String)
     */
    @Override
    public void releaseLock(String resourceName, String lockName) {
        template.update(String.format("DELETE FROM %s WHERE %s = ? AND %s = ?", TABLE_NAME, COLUMN_RESOURCE_NAME,
                COLUMN_LOCK_NAME), resourceName, lockName);
    }

}