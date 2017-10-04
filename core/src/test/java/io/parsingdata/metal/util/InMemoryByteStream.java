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

package io.parsingdata.metal.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import io.parsingdata.metal.Util;
import io.parsingdata.metal.data.ByteStream;

public class InMemoryByteStream implements ByteStream {

    private final byte[] data;

    public InMemoryByteStream(final byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] read(final long offset, final int length) throws IOException {
        if (!isAvailable(offset, length)) { throw new IOException("Data to read is not available."); }
        byte[] data = new byte[length];
        System.arraycopy(this.data, (int)offset, data, 0, length);
        return data;
    }

    @Override
    public boolean isAvailable(final long offset, final int length) {
        return offset + length <= data.length;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + data.length + ")";
    }

    @Override
    public boolean equals(final Object obj) {
        return Util.notNullAndSameClass(this, obj)
            && Arrays.equals(data, ((InMemoryByteStream)obj).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().hashCode(), Arrays.hashCode(data));
    }

}
