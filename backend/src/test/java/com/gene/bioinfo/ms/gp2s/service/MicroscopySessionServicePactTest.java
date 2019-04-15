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

package com.gene.bioinfo.ms.gp2s.service;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import lombok.NonNull;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

public class MicroscopySessionServicePactTest {

    private static final String MICROSCOPY_SESSION_API_PATH = "/api/v1/microscopy-session/microscope/";
    private static final String EXISTING_MICROSCOPE_ID = "3";
    private static final String EXISTING_MICROSCOPE_SLUG = "microscope-3";
    private static final String MICROSCOPY_SESSION_ID = "1";
    private static final String DIRECTORY_NAME = "dir_name";

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("project_provider", this);

    private DslPart generateJsonBody() {
        return newJsonBody((o) -> {
            o.stringValue("id", MICROSCOPY_SESSION_ID);
            o.stringValue("microscope.id", EXISTING_MICROSCOPE_ID);
            o.stringValue("microscope.slug", EXISTING_MICROSCOPE_SLUG);
            o.stringValue("dataStorageDirectoryName", DIRECTORY_NAME);
        }).build();
    }

    @Pact(consumer = "microscopy_session_consumer")
    @NonNull
    public RequestResponsePact createMicroscopySessionResourcePact(@NonNull final PactDslWithProvider builder) {
        return builder
                .uponReceiving("a GET request with a valid microscope ID")
                .path(MICROSCOPY_SESSION_API_PATH + EXISTING_MICROSCOPE_ID)
                .method("GET")
                .willRespondWith()
                .status(HttpStatus.OK.value())
                .headers(Collections.singletonMap("ContentType", "application/json"))
                .body(this.generateJsonBody())

                .uponReceiving("a GET request with a valid microscope slug")
                .path(MICROSCOPY_SESSION_API_PATH + EXISTING_MICROSCOPE_SLUG)
                .method("GET")
                .willRespondWith()
                .status(HttpStatus.OK.value())
                .headers(Collections.singletonMap("ContentType", "application/json"))
                .body(this.generateJsonBody())

                .uponReceiving("a GET request with a non-existing session with given microscope ID or slug")
                .path(MICROSCOPY_SESSION_API_PATH + "9999999999999")
                .method("GET")
                .willRespondWith()
                .status(HttpStatus.NOT_FOUND.value())

                .uponReceiving("a GET request without ID or slug")
                .path(MICROSCOPY_SESSION_API_PATH)
                .method("GET")
                .willRespondWith()
                .status(HttpStatus.BAD_REQUEST.value())

                .toPact();
    }


    @Test
    @PactVerification
    public void microscopySessionConsumerTest() {
        shouldReturnMicroscopySessionWhenRequestContainsExistingMicroscopeId();
        shouldReturnMicroscopySessionWhenRequestContainsExistingMicroscopeSlug();
        shouldReturn404WhenRequestingANonExistingSessionWithGivenMicroscopeIdOrSlug();
        shouldReturn400WhenRequestIsSentWithoutMicroscopeId();
    }

    private void checkExistingMicroscopySession(ResponseEntity<String> response) {
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getHeaders().get("ContentType").contains("application/json")).isTrue();
        assertThat(response.getBody()).contains("microscope.id", EXISTING_MICROSCOPE_ID);
        assertThat(response.getBody()).contains("microscope.slug", EXISTING_MICROSCOPE_SLUG);
        assertThat(response.getBody()).contains("dataStorageDirectoryName", DIRECTORY_NAME);
    }

    private void shouldReturnMicroscopySessionWhenRequestContainsExistingMicroscopeId() {
        // when
        final ResponseEntity<String> response = new RestTemplate().getForEntity(
                mockProvider.getUrl() + MICROSCOPY_SESSION_API_PATH + EXISTING_MICROSCOPE_ID, String.class);
        // then
        checkExistingMicroscopySession(response);
    }

    private void shouldReturnMicroscopySessionWhenRequestContainsExistingMicroscopeSlug() {
        // when
        final ResponseEntity<String> response = new RestTemplate().getForEntity(
                mockProvider.getUrl() + MICROSCOPY_SESSION_API_PATH + EXISTING_MICROSCOPE_SLUG, String.class);
        // then
        checkExistingMicroscopySession(response);
    }

    private void shouldReturn404WhenRequestingANonExistingSessionWithGivenMicroscopeIdOrSlug() {
        // when
        try {
            new RestTemplate().getForEntity(
                    mockProvider.getUrl() + MICROSCOPY_SESSION_API_PATH + "9999999999999", String.class);
        } catch (HttpClientErrorException exception) {
            // then
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            return;
        }

        failBecauseExceptionWasNotThrown(HttpClientErrorException.class);
    }

    private void shouldReturn400WhenRequestIsSentWithoutMicroscopeId() {
        // when
        try {
            new RestTemplate().getForEntity(
                    mockProvider.getUrl() + MICROSCOPY_SESSION_API_PATH, String.class);
        } catch (HttpClientErrorException exception) {
            // then
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            return;
        }

        failBecauseExceptionWasNotThrown(HttpClientErrorException.class);
    }
}
