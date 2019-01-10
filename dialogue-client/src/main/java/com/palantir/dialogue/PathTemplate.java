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

import com.google.common.base.Verify;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import java.util.Map;
import java.util.Set;

public final class PathTemplate {

    // TODO(rfink): Add query parameters.
    private final ImmutableList<Segment> segments;

    private PathTemplate(Iterable<Segment> segments) {
        this.segments = ImmutableList.copyOf(segments);
        Set<String> seenVariables = Sets.newHashSetWithExpectedSize(this.segments.size());
        for (Segment segment : segments) {
            if (segment.variable != null) {
                boolean seen = seenVariables.add(segment.variable);
                Preconditions.checkArgument(seen, "Duplicate segment variable names not allowed",
                        SafeArg.of("variable", segment.variable));
            }
        }
    }

    public static PathTemplate of(Iterable<Segment> segments) {
        return new PathTemplate(segments);
    }

    /** Populates this template with the given named parameters. */
    public String fill(Map<String, String> parameters) {
        StringBuilder builder = new StringBuilder();
        int numVariableSegments = 0;
        for (Segment segment : segments) {
            builder.append("/");
            if (segment.fixed != null) {
                builder.append(segment.fixed);
            } else {
                Preconditions.checkArgument(parameters.containsKey(segment.variable),
                        "Provided parameter map does not contain segment variable name",
                        SafeArg.of("variable", segment.variable));
                builder.append(parameters.get(segment.variable));
                numVariableSegments += 1;
            }
        }
        Verify.verify(numVariableSegments == parameters.size(), "Too many parameters supplied, this is a bug");
        String path = builder.toString();
        return path.isEmpty() ? "/" : path;
    }

    public static final class Segment {
        private final String fixed;
        private final String variable;

        private Segment(String fixed, String variable) {
            this.fixed = fixed;
            this.variable = variable;
        }

        public static Segment fixed(String fixed) {
            return new Segment(fixed, null);
        }

        public static Segment variable(String variable) {
            return new Segment(null, variable);
        }
    }
}
