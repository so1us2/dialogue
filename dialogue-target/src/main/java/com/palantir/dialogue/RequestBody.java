/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import java.io.InputStream;
import java.util.OptionalLong;

public interface RequestBody {
    /** The number of bytes in the {@link #content}, or absent if unknown. */
    OptionalLong length();

    /** The content of this request body, possibly empty. */
    InputStream content();

    /** A HTTP/Conjure content type (e.g., "application/json") indicating the type of content. */
    String contentType();
}
