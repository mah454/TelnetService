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

package ir.moke.app.beans;

import ir.moke.app.CoreUtils;
import ir.moke.toolkit.ObjectStore;
import ir.moke.toolkit.TelnetSocketServer;
import ir.moke.toolkit.TtyAsciiCodecs;
import ir.moke.toolkit.api.CLI;
import ir.moke.toolkit.api.Command;

@CLI
public class InformationCommands implements TtyAsciiCodecs {

    @Command(value = "version", description = "Display application version")
    public String getVersion() {
        return "Version 1.0-SNAPSHOT";
    }

    @Command(value = "help", description = "Print list of available commands")
    public String help() {
        StringBuilder result = new StringBuilder("List of available commands:").append("\n");
        final String formatLine = BOLD + "%-10s" + RESET + BLUE + "%-10s" + RESET + "%s\n";
        for (ObjectStore objectStore : TelnetSocketServer.objectStoreList) {
            result.append(String.format(formatLine, objectStore.getCommand(), objectStore.getAction() != null ? objectStore.getAction() : "", objectStore.getDescription()));
        }
        return result.toString();
    }

    @Command(value = "system", description = "System Information")
    public String system() {
        String osType = CoreUtils.getOsType();
        String username = CoreUtils.getUsername();
        String javaVendor = CoreUtils.getJavaVendor();
        String javaVersion = CoreUtils.getJavaVersion();
        double runtimeTotalMemory = CoreUtils.getOperatingSystemTotalMemory();
        double runtimeUsedMemory = CoreUtils.getOperatingSystemUsedMemory();
        double runtimeFreeMemory = CoreUtils.getOperatingSystemFreeMemory();
        return "\n" +
                GREEN + "Operating System" + "\t\t" + BLUE + osType + "\n" +
                GREEN + "Username" + "\t\t\t" + BLUE + username + "\n" +
                GREEN + "Java Vendor" + "\t\t\t" + BLUE + javaVendor + "\n" +
                GREEN + "Java Version" + "\t\t\t" + BLUE + javaVersion + "\n" +
                GREEN + "Total Memory" + "\t\t\t" + BLUE + String.format("%.3f/GB", runtimeTotalMemory) + "\n" +
                GREEN + "Used Memory" + "\t\t\t" + BLUE + String.format("%.3f/GB", runtimeUsedMemory) + "\n" +
                GREEN + "Free Memory" + "\t\t\t" + BLUE + String.format("%.3f/GB", runtimeFreeMemory) + "\n" +
                RESET;
    }
}
