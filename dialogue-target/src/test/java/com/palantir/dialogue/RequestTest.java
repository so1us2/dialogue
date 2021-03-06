/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.dialogue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;

import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import org.junit.jupiter.api.Test;

public final class RequestTest {

    @Test
    public void testRequestHeaderInsensitivity() {
        Request request = Request.builder().putHeaderParams("Foo", "bar").build();
        assertThat(request.headerParams())
                .containsKeys("foo")
                .containsKeys("FOO")
                .containsKeys("Foo");
    }

    @Test
    public void testHeadersAreRedacted() {
        String sentinel = "shouldnotbelogged";
        BearerToken token = BearerToken.valueOf(sentinel);
        Request request = Request.builder()
                .putHeaderParams("authorization", AuthHeader.of(token).toString())
                .putHeaderParams("other", token.toString())
                .build();
        assertThat(request).asString().doesNotContain(sentinel);
    }
}
