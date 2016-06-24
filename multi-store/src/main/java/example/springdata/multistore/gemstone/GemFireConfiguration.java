/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.springdata.multistore.gemstone;

import java.util.Properties;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.RegionAttributes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

/**
 * Spring {@link Configuration} class for configuring Pivotal GemFire.
 *
 * @author John Blum
 * @see org.springframework.context.annotation.Bean
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
 */
@Configuration
@EnableGemfireRepositories
@SuppressWarnings("unused")
public class GemFireConfiguration {

	String applicationName() {
		return "GemstonesGemFireApplication";
	}

	String logLevel() {
		return System.getProperty("gemfire.log-level", "warning");
	}

	Properties gemfireProperties() {
		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", applicationName());
		gemfireProperties.setProperty("mcast-port", "0");
		gemfireProperties.setProperty("log-level", logLevel());

		return gemfireProperties;
	}

	@Bean
	CacheFactoryBean gemfireCache() {
		CacheFactoryBean gemfireCache = new CacheFactoryBean();

		gemfireCache.setClose(true);
		gemfireCache.setProperties(gemfireProperties());

		return gemfireCache;
	}

	@Bean(name = "Gemstones")
	LocalRegionFactoryBean gemstonesRegion(Cache gemfireCache,
			RegionAttributes<Long, Gemstone> gemstonesRegionAttributes) {

		LocalRegionFactoryBean<Long, Gemstone> gemstonesRegion = new LocalRegionFactoryBean<>();

		gemstonesRegion.setAttributes(gemstonesRegionAttributes);
		gemstonesRegion.setCache(gemfireCache);
		gemstonesRegion.setClose(false);
		gemstonesRegion.setPersistent(false);

		return gemstonesRegion;
	}

	@Bean
	@SuppressWarnings("unchecked")
	RegionAttributesFactoryBean gemstonesRegionAttributes() {
		RegionAttributesFactoryBean gemstonesRegionAttributes = new RegionAttributesFactoryBean();

		gemstonesRegionAttributes.setKeyConstraint(Long.class);
		gemstonesRegionAttributes.setValueConstraint(Gemstone.class);

		return gemstonesRegionAttributes;
	}
}
