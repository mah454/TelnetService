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

package ir.moke.toolkit;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToolkitLogger {
    private static Logger logger = null;
    private static final ToolkitLogger instance = new ToolkitLogger();
    private static Handler handler;

    private ToolkitLogger() {
        handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
    }

    public static ToolkitLogger newInstance(String name) {
        logger = Logger.getLogger(name);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }
}
