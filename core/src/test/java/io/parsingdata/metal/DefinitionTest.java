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

package io.parsingdata.metal;

import static io.parsingdata.metal.Shorthand.con;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;
import static io.parsingdata.metal.util.EncodingFactory.enc;
import static io.parsingdata.metal.util.EnvironmentFactory.stream;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import io.parsingdata.metal.data.ParseGraph;
import io.parsingdata.metal.data.ParseResult;
import io.parsingdata.metal.token.Token;

public class DefinitionTest {
	
	// TODO: Add a bunch of additional tests here to check whether all Token types work correctly (#8)
	
	@Test
	public void singleDef() throws IOException {
		final Token singleDef = def("a", con(1));
		final ParseResult result = singleDef.parse(stream(1), enc());
		Assert.assertTrue(result.succeeded);
		Assert.assertTrue(result.environment.order.getDefinition() == ParseGraph.NONE);
		Assert.assertTrue(result.environment.order.head.getDefinition() == singleDef);
		Assert.assertTrue(result.environment.order.tail.getDefinition() == ParseGraph.NONE);
	}

	@Test
	public void smallSeq() throws IOException {
		Token defA = def("a", con(1));
		Token defB = def("b", con(1));
		final Token smallSeq = seq(defA, defB);
		final ParseResult result = smallSeq.parse(stream(1, 2), enc());
		Assert.assertTrue(result.succeeded);
		Assert.assertTrue(result.environment.order.getDefinition() == ParseGraph.NONE);
		Assert.assertTrue(result.environment.order.head.getDefinition() == smallSeq);
		Assert.assertTrue(result.environment.order.head.asGraph().head.getDefinition() == defB);
		Assert.assertTrue(result.environment.order.head.asGraph().tail.getDefinition() == smallSeq);
		Assert.assertTrue(result.environment.order.head.asGraph().tail.head.getDefinition() == defA);
		Assert.assertTrue(result.environment.order.head.asGraph().tail.tail.getDefinition() == smallSeq);
		Assert.assertTrue(result.environment.order.tail.getDefinition() == ParseGraph.NONE);
	}

}
