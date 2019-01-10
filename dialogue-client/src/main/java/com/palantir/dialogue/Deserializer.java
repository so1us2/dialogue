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
import javax.annotation.Nullable;

/**
 * Extracts a call response of type {@link RespT} from the given {@link InputStream}. May throw exceptions to indicate
 * failure.
 */
public interface Deserializer<RespT> {
    RespT deserialize(@Nullable InputStream stream);
}
