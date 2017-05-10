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

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Implementation of resource lock manager that uses jdbc as backend.
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ResourceLockManagerJdbcFunctionImpl implements ResourceLockManager {

	private JdbcTemplate template;

	/**
	 * Creates a new ResourceLockManagerJdbcFunctionImpl with the given JDBC template.
	 * 
	 * @param template
	 *            the JDBC template
	 */
	public ResourceLockManagerJdbcFunctionImpl(JdbcTemplate template) {
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
			template.query("SELECT acquire_resource_lock(?, ?, ?);", new Object[] {resourceName, lockName, ttl == 0 ? null : DateUtils.addSeconds(new Date(), ttl)},
                    new RowMapper<Object>() {
                        @Override
                        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                            return null;
                        }
                    });
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
		template.query("SELECT release_resource_lock(?, ?);", new Object[] {resourceName, lockName},
                new RowMapper<Object>() {
                    @Override
                    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                        return null;
                    }
                });
	}

}
