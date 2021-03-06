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

package com.palantir.dialogue.clients;

import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Reflection {
    private static final Logger log = LoggerFactory.getLogger(Reflection.class);

    private Reflection() {}

    static <T> T callStaticFactoryMethod(Class<T> dialogueInterface, Channel channel, ConjureRuntime conjureRuntime) {
        Preconditions.checkNotNull(dialogueInterface, "dialogueInterface");
        Preconditions.checkNotNull(channel, "channel");

        try {
            Method method = getStaticOfMethod(dialogueInterface)
                    .orElseThrow(() -> new SafeIllegalStateException(
                            "A static 'of(Channel, ConjureRuntime)' method is required",
                            SafeArg.of("dialogueInterface", dialogueInterface)));

            return dialogueInterface.cast(method.invoke(null, channel, conjureRuntime));

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SafeIllegalArgumentException(
                    "Failed to reflectively construct dialogue client. Please check the "
                            + "dialogue interface class has a public static of(Channel, ConjureRuntime) method",
                    e,
                    SafeArg.of("dialogueInterface", dialogueInterface));
        }
    }

    private static Optional<Method> getStaticOfMethod(Class<?> dialogueInterface) {
        try {
            return Optional.ofNullable(dialogueInterface.getMethod("of", Channel.class, ConjureRuntime.class));
        } catch (NoSuchMethodException e) {
            log.debug("Failed to get static 'of' method", SafeArg.of("interface", dialogueInterface), e);
            return Optional.empty();
        }
    }
}
