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

import ir.moke.toolkit.api.CLI;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ToolkitReflection {
    private static final Logger logger = ToolkitLogger.newInstance(ToolkitReflection.class.getName()).getLogger();
    public static ToolkitReflection newInstance = new ToolkitReflection();

    private ToolkitReflection() {
    }

    public List<Class<?>> getCommandClass(String packageName) {
        List<Class<?>> classList = new ArrayList<>();
        String packagePath = packageName.replaceAll("\\.", "/");
        URL systemResource = ClassLoader.getSystemResource(packagePath);
        if (systemResource != null) {
            String path = systemResource.getPath();
            File filePath = new File(path);
            if (filePath.isDirectory()) {
                File[] files = filePath.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        try {
                            Class<?> aClass = Class.forName(packageName + "." + file.getName().replaceAll("\\.class", ""));
                            if (aClass.isAnnotationPresent(CLI.class)) {
                                classList.add(aClass);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    logger.fine("[ERROR] package is empty.");
                }
            } else {
                logger.fine("[ERROR] path is not a directory.");
            }
        } else {
            logger.fine("[ERROR] Resource can not loaded , maybe package is empty or path does not exist. ");
        }
        return classList;
    }
}
