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

import java.util.Date;
import java.util.logging.*;

public class ToolkitLogger implements TtyAsciiCodecs {
    private static Logger logger = null;
    private static final ToolkitLogger instance = new ToolkitLogger();
    private static Handler handler;

    private ToolkitLogger() {
        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                Level level = record.getLevel();
                String result = "[" + BOLD;
                if (level.equals(Level.INFO)) {
                    result += BLUE;
                } else if (level.equals(Level.WARNING)) {
                    result += GREEN;
                } else if (level.equals(Level.FINE)) {
                    result += BLINK + RED;
                }

                result += level + RESET + "] " + new Date(record.getMillis()) + " " + record.getSourceClassName() + " [" + record.getSourceMethodName() + "]: " + record.getMessage() + "\n";

                return result;
            }
        };
        handler = new ConsoleHandler();
        handler.setFormatter(formatter);
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
