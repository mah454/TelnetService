/*
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

package ir.moke.app;


import ir.moke.toolkit.TelnetSocketServer;
import ir.moke.toolkit.TtyAsciiCodecs;

import java.util.Collections;

public class MainClass implements TtyAsciiCodecs {
    private static final int PORT = 2222;
    private static final String PROMPT = BOLD + "jvm-prompt:> " + RESET;

    public static void main(String[] args) {
        TelnetSocketServer tss = TelnetSocketServer.getInstance(PORT);
        tss.setPrompt(PROMPT)
                .setCliPackages(Collections.singletonList("ir.moke.app.beans"))
                .start();
    }
}