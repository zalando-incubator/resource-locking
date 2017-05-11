# resource-locking

[![Maven Central](https://img.shields.io/maven-central/v/org.zalando/resource-locking.svg)](https://github.com/zalando-incubator/resource-locking)
[![Build Status](https://travis-ci.org/zalando-incubator/resource-locking.svg?branch=master)](https://travis-ci.org/zalando-incubator/resource-locking)

## Purpose

The resource-locking library provides a service that does pessimistic locking of a resource using different backends. You can use this library to synchronize running jobs over different instances or do pessimistic locking for the same resource over different instances.

## Features

* resource-locking with etcd as backend
* resource-locking with JDBC as backend and access via SQL statements
* resource-locking with JDBC as backend and access via SQL stored procedures

## Usage

Use the provided Spring Boot Starter library and either.

    <dependency>
      <groupId>org.zalando</groupId>
      <artifactId>resource-locking-starter</artifactId>
      <version>RELEASE</version>
    </dependency>

### etcd

If you want to use etcd backend include the following dependency that provides the etcd client:  
    
    <dependency>
      <groupId>org.zalando</groupId>
      <artifactId>zalando-boot-etcd</artifactId>
      <version>2.3</version>
    </dependency>

Additionally write a configuration that provides the specific backend for resource locking.
    
    @Configuration()
    public class ResourceLockManagerConfiguration {
    
        @Bean
        public ResourceLockManager resourceLockManager(EtcdClient etcdClient) {
            return new ResourceLockManagerEtcdImpl(etcdClient, "resource-locking-sample");
        }
    }

### jdbc

If you want to use jdbc backend include the following dependency that provides the etcd client:  

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
 
Additionally write a configuration that provides the specific backend for resource locking.

    @Configuration()
    public class ResourceLockManagerConfiguration {
    
        @Bean
        public ResourceLockManager resourceLockManager2(JdbcTemplate template) {
            return new ResourceLockManagerJdbcImpl(template);
            // OR: return new ResourceLockManagerJdbcFunctionImpl(template);
        }
    }
   

    


## Development 

### Building

    mvn clean install

### Releasing

    mvn release:prepare release:perform

## Contributions

This project accepts contributions from the open-source community, including bug fixes and feature adds.

Before making a contribution, please let us know by posting a comment to the relevant issue. And if you would like to propose a new feature, do start a new issue explaining the feature you’d like to contribute.

## License

The MIT License (MIT)

Copyright (c) 2017 Zalando SE, https://tech.zalando.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
