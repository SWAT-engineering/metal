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

package io.parsingdata.metal.data.callback;

import static io.parsingdata.metal.Util.checkNotNull;

import io.parsingdata.metal.token.Token;

public class TokenCallback {

    public final Token token;
    public final Callback callback;

    public TokenCallback(final Token token, final Callback callback) {
        this.token = checkNotNull(token, "token");
        this.callback = checkNotNull(callback, "callback");
    }

    @Override
    public String toString() {
        return token + "->" + callback;
    }

}
