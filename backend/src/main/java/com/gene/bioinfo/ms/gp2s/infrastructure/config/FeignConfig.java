/*
 * Copyright 2018 Genentech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gene.bioinfo.ms.gp2s.infrastructure.config;

import com.gene.bioinfo.ms.gp2s.GP2SApplication;
import com.gene.bioinfo.ms.gp2s.service.feign.ThirdPartyServiceExceptionErrorDecoder;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Global feign clients configuration.
 */
@Import(FeignConfig.FeignEnabled.class)
@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder springErrorDecoder() {
        return new ThirdPartyServiceExceptionErrorDecoder();
    }

    @Profile("!prod")
    @Bean
    public Logger.Level defaultLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * For integration tests we don't want to create actual FeignClients, so the
     * mocked versions would be used instead during tests.
     *
     * Since the @Profile integration is per class level, were using
     * @EnableFeignClients on an internal class which is then @Imported on the global class.
     * But since there a @Profile annotation nullifying this whole class for integration tests.
     */
    @Profile("!integration-test")
    @EnableFeignClients(defaultConfiguration = FeignConfig.class, basePackageClasses = GP2SApplication.class)
    public static class FeignEnabled {
    }
}
