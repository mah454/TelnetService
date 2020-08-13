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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface CoreUtils {

    double GB = 1024.0 * 1024.0;
    String meminfoFile = "/proc/meminfo";

    static String getOsType() {
        return System.getProperty("os.name");
    }

    static String getUsername() {
        return System.getProperty("user.name");
    }

    static String getJavaVersion() {
        return System.getProperty("java.runtime.version");
    }

    static String getJavaVendor() {
        return System.getProperty("java.specification.vendor");
    }

    static double getOperatingSystemTotalMemory() {
        double totalMemory = 0;
        try {
            String totalMemStr = Files.readAllLines(Paths.get(meminfoFile)).get(0).split(" * ")[1];
            totalMemory = Double.parseDouble(totalMemStr) / GB;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalMemory;
    }

    static double getOperatingSystemFreeMemory() {
        double freeMemory = 0;
        try {
            String freeMemStr = Files.readAllLines(Paths.get(meminfoFile)).get(2).split(" * ")[1];
            freeMemory = Double.parseDouble(freeMemStr) / GB;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return freeMemory;
    }

    static double getOperatingSystemUsedMemory() {
        return getOperatingSystemTotalMemory() - getOperatingSystemFreeMemory();
    }
}
