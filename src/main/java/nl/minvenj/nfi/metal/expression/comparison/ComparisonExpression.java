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

package nl.minvenj.nfi.metal.expression.comparison;

import nl.minvenj.nfi.metal.data.Environment;
import nl.minvenj.nfi.metal.encoding.Encoding;
import nl.minvenj.nfi.metal.expression.Expression;
import nl.minvenj.nfi.metal.expression.value.OptionalValue;
import nl.minvenj.nfi.metal.expression.value.Value;
import nl.minvenj.nfi.metal.expression.value.ValueExpression;

public abstract class ComparisonExpression implements Expression {

    private final ValueExpression _current;
    private final ValueExpression _predicate;

    public ComparisonExpression(final ValueExpression current, final ValueExpression predicate) {
        _current = current;
        if (predicate == null) { throw new IllegalArgumentException("Argument predicate may not be null."); }
        _predicate = predicate;
    }

    @Override
    public boolean eval(final Environment env, final Encoding enc) {
        final OptionalValue ocv = _current == null ? OptionalValue.of(env.order.flatten().current()) : _current.eval(env, enc);
        if (!ocv.isPresent()) { return false; }
        final OptionalValue opv = _predicate.eval(env, enc);
        if (!opv.isPresent()) { return false; }
        return compare(ocv.get(), opv.get());
    }

    public abstract boolean compare(final Value current, final Value predicate);

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + (_current == null ? "" : _current + ",") + _predicate + ")";
    }

}
