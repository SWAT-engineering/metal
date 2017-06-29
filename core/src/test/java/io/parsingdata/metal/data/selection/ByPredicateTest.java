/*
 * Copyright 2013-2016 Netherlands Forensic Institute
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

package io.parsingdata.metal.data.selection;

import static org.junit.Assert.assertEquals;

import static io.parsingdata.metal.Shorthand.rep;
import static io.parsingdata.metal.data.selection.ByPredicate.getAllValues;
import static io.parsingdata.metal.util.EncodingFactory.enc;
import static io.parsingdata.metal.util.EnvironmentFactory.stream;
import static io.parsingdata.metal.util.TokenDefinitions.any;

import java.io.IOException;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import io.parsingdata.metal.data.Environment;

public class ByPredicateTest {

    @Test
    public void limit() throws IOException {
        Optional<Environment> environment = rep(any("a")).parse(stream(1, 2, 3, 4, 5), enc());
        Assert.assertTrue(environment.isPresent());
        for (int i = 0; i < 7; i++) {
            assertEquals(Math.min(5, i), getAllValues(environment.get().order, (value) -> value.matches("a"), i).size);
        }
    }

}
