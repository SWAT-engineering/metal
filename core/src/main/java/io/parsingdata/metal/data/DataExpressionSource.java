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

package io.parsingdata.metal.data;

import static io.parsingdata.metal.Util.checkNotNull;

import java.io.IOException;

import io.parsingdata.metal.encoding.Encoding;
import io.parsingdata.metal.expression.value.OptionalValue;
import io.parsingdata.metal.expression.value.ValueExpression;

public class DataExpressionSource extends Source {

    public final ValueExpression dataExpression;
    public final int index;
    public final Environment environment;
    public final Encoding encoding;

    public DataExpressionSource(final ValueExpression dataExpression, final int index, final Environment environment, final Encoding encoding) {
        this.dataExpression = checkNotNull(dataExpression, "dataExpression");
        this.index = index;
        this.environment = checkNotNull(environment, "environment");
        this.encoding = checkNotNull(encoding, "encoding");
    }

    @Override
    protected byte[] getData(final long offset, final int size) throws IOException {
        ImmutableList<OptionalValue> results = dataExpression.eval(environment, encoding);
        if (results.size <= index) { throw new IllegalStateException("ValueExpression dataExpression yields incorrect amount of results."); }
        final byte[] inputData = getValueAtIndex(results, index, 0).get().getValue();
        if (offset >= inputData.length) { return new byte[0]; }
        final int toCopy = (int)offset + size > inputData.length ? inputData.length - (int)offset : size;
        final byte[] outputData = new byte[toCopy];
        System.arraycopy(inputData, (int)offset, outputData, 0, toCopy);
        return outputData;
    }

    private OptionalValue getValueAtIndex(ImmutableList<OptionalValue> results, int index, int current) {
        if (index == current) { return results.head; }
        return getValueAtIndex(results.tail, index, current + 1);
    }

    @Override
    public String toString() {
        return dataExpression.toString() + "(" + environment + "," + encoding + ")";
    }
}
