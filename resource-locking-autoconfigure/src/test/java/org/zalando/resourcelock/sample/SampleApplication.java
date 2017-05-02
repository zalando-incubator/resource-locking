package org.zalando.resourcelock.sample;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.zalando.boot.etcd.EtcdClient;

@SpringBootApplication
public class SampleApplication {

	@Bean
	public EtcdClient etcdClient() {
		return new EtcdClient("http://localhost:2379");
	}
}
