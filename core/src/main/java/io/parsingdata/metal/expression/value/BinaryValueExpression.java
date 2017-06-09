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

import static io.parsingdata.metal.SafeTrampoline.complete;
import static io.parsingdata.metal.SafeTrampoline.intermediate;
import static io.parsingdata.metal.Util.checkNotNull;
import static io.parsingdata.metal.data.transformation.Reversal.reverse;

import java.util.Objects;
import java.util.Optional;

import io.parsingdata.metal.SafeTrampoline;
import io.parsingdata.metal.Util;
import io.parsingdata.metal.data.ImmutableList;
import io.parsingdata.metal.data.ParseGraph;
import io.parsingdata.metal.encoding.Encoding;

/**
 * Base class for {@link ValueExpression}s with two operands.
 * <p>
 * A BinaryValueExpression implements a ValueExpression that has two operands:
 * <code>left</code> and <code>right</code> (both {@link ValueExpression}s).
 * Both operands are themselves first evaluated. If at least one of the
 * operands evaluates to {@link Optional#empty()}, the result of the
 * ValueExpression itself will be empty as well.
 * <p>
 * For lists, values with the same index are evaluated in this manner. If
 * lists are of unequal length, the result is a list with evaluated values the
 * same size as the shortest list, appended with empty values to match the
 * size of the longest list.
 * <p>
 * To implement a BinaryValueExpression, only the
 * {@link #eval(Value, Value, ParseGraph, Encoding)} must be implemented,
 * handling the case of evaluating two values. This base class takes care of
 * evaluating the operands and handling list semantics.
 *
 * @see UnaryValueExpression
 */
public abstract class BinaryValueExpression implements ValueExpression {

    public final ValueExpression left;
    public final ValueExpression right;

    public BinaryValueExpression(final ValueExpression left, final ValueExpression right) {
        this.left = checkNotNull(left, "left");
        this.right = checkNotNull(right, "right");
    }

    @Override
    public ImmutableList<Optional<Value>> eval(final ParseGraph graph, final Encoding encoding) {
        return evalLists(left.eval(graph, encoding), right.eval(graph, encoding), graph, encoding);
    }

    private ImmutableList<Optional<Value>> evalLists(final ImmutableList<Optional<Value>> leftValues, final ImmutableList<Optional<Value>> rightValues, final ParseGraph graph, final Encoding encoding) {
        return reverse(padList(evalLists(leftValues, rightValues, graph, encoding, new ImmutableList<>()).computeResult(), Math.abs(leftValues.size - rightValues.size)).computeResult());
    }

    private SafeTrampoline<ImmutableList<Optional<Value>>> evalLists(final ImmutableList<Optional<Value>> leftValues, final ImmutableList<Optional<Value>> rightValues, final ParseGraph graph, final Encoding encoding, final ImmutableList<Optional<Value>> result) {
        if (leftValues.isEmpty() || rightValues.isEmpty()) { return complete(() -> result); }
        return intermediate(() -> evalLists(leftValues.tail, rightValues.tail, graph, encoding, result.add(eval(leftValues.head, rightValues.head, graph, encoding))));
    }

    private SafeTrampoline<ImmutableList<Optional<Value>>> padList(final ImmutableList<Optional<Value>> list, final long size) {
        if (size <= 0) { return complete(() -> list); }
        return intermediate(() -> padList(list.add(Optional.empty()), size - 1));
    }

    private Optional<Value> eval(final Optional<Value> left, final Optional<Value> right, final ParseGraph graph, final Encoding encoding) {
        if (!left.isPresent() || !right.isPresent()) { return Optional.empty(); }
        return eval(left.get(), right.get(), graph, encoding);
    }

    public abstract Optional<Value> eval(final Value left, final Value right, final ParseGraph graph, final Encoding encoding);

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + left + "," + right + ")";
    }

    @Override
    public boolean equals(final Object obj) {
        return Util.notNullAndSameClass(this, obj)
            && Objects.equals(left, ((BinaryValueExpression)obj).left)
            && Objects.equals(right, ((BinaryValueExpression)obj).right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

}
