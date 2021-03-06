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

package io.parsingdata.metal.expression.value;

import static io.parsingdata.metal.Shorthand.con;
import static io.parsingdata.metal.Shorthand.div;
import static io.parsingdata.metal.Shorthand.exp;
import static io.parsingdata.metal.Shorthand.ref;
import static io.parsingdata.metal.Shorthand.rep;
import static io.parsingdata.metal.util.EncodingFactory.enc;
import static io.parsingdata.metal.util.EnvironmentFactory.env;
import static io.parsingdata.metal.util.ParseStateFactory.stream;
import static io.parsingdata.metal.util.TokenDefinitions.any;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.parsingdata.metal.data.ParseState;

public class RefEdgeCaseTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    ParseState parseState;

    @Before
    public void before() throws IOException {
        parseState = rep(any("a")).parse(env(stream(1, 2, 3))).get();
    }

    @Test
    public void multiLimit() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Limit must evaluate to a single non-empty value.");
        ref("a", exp(con(1), con(3))).eval(parseState, enc());
    }

    @Test
    public void emptyLimit() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Limit must evaluate to a single non-empty value.");
        ref("a", div(con(1), con(0))).eval(parseState, enc());
    }

}
