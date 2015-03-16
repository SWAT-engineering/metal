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

package nl.minvenj.nfi.metal.token;

import java.io.IOException;

import nl.minvenj.nfi.metal.data.Environment;
import nl.minvenj.nfi.metal.data.ParseGraph;
import nl.minvenj.nfi.metal.data.ParseResult;
import nl.minvenj.nfi.metal.encoding.Encoding;
import nl.minvenj.nfi.metal.expression.value.OptionalValue;
import nl.minvenj.nfi.metal.expression.value.ValueExpression;

public class Sub extends Token {

    private final Token _op;
    private final ValueExpression _addr;

    public Sub(final Token op, final ValueExpression addr, final Encoding enc) {
        super(enc);
        if (op == null) { throw new IllegalArgumentException("Argument op may not be null."); }
        _op = op;
        if (addr == null) { throw new IllegalArgumentException("Argument addr may not be null."); }
        _addr = addr;
    }

    @Override
    protected ParseResult parseImpl(final String scope, final Environment env, final Encoding enc) throws IOException {
        final OptionalValue ov = _addr.eval(env, enc);
        if (ov.isPresent()) {
            if (ParseGraph.findRef(env.order.getGraphs(), ov.get().asNumeric().longValue()) == null) {
                final ParseResult res = _op.parse(scope, new Environment(env.order.addBranch(), env.input, ov.get().asNumeric().longValue()), enc);
                if (res.succeeded()) {
                    return new ParseResult(true, new Environment(res.getEnvironment().order, res.getEnvironment().input, env.offset));
                }
            } else {
                return new ParseResult(true, new Environment(env.order.addRef(ov.get().asNumeric().longValue()), env.input, env.offset));
            }
        }
        return new ParseResult(false, env);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + _op + ", " + _addr + ")";
    }

}
