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

package com.gene.bioinfo.ms.gp2s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;


@SpringBootApplication
@EnableConfigurationProperties
public class GP2SApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(GP2SApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(GP2SApplication.class, args);
        logAccessURLs(applicationContext);
    }

    private static void logAccessURLs(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment env = applicationContext.getEnvironment();
        String contextPath = env.getProperty("server.contextPath", "");
        String port = env.getProperty("server.port", "");
        String hostAddress = "unknown";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // do nothing
        }
        LOGGER.info("Access URLs:\n----------------------------------------------------------\n\t"
                            + "Local: \t\thttp://127.0.0.1:{}{}\n\t"
                            + "External: \thttp://{}:{}{}\n\t"
                            + "Swagger: \thttp://127.0.0.1:{}{}/swagger-ui.html"
                            + "\n----------------------------------------------------------",
                    port, contextPath, hostAddress, port, contextPath, port, contextPath);
    }
}
