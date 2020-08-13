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

import ir.moke.toolkit.api.Command;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TelnetSocketServer implements Closeable {
    private static final Logger logger = ToolkitLogger.newInstance(TelnetSocketServer.class.getName()).getLogger();
    private static final TelnetSocketServer telnetSocketServer = new TelnetSocketServer();
    private ServerSocket serverSocket;
    private static int PORT;
    private String prompt;
    private List<String> packageList = new ArrayList<>();
    public static List<ObjectStore> objectStoreList = new ArrayList<>();

    private TelnetSocketServer() {
    }

    public static TelnetSocketServer getInstance(int port) {
        PORT = port;
        return telnetSocketServer;
    }

    public TelnetSocketServer setPrompt(String prompt) {
        this.prompt = prompt;
        return telnetSocketServer;
    }

    public TelnetSocketServer setCliPackages(List<String> packageList) {
        this.packageList = packageList;
        return telnetSocketServer;
    }

    private void scanPackages() {
        for (String packagePath : packageList) {
            try {
                List<Class<?>> commandClass = ToolkitReflection.newInstance.getCommandClass(packagePath);
                for (Class<?> aClass : commandClass) {
                    Object o = aClass.getDeclaredConstructor().newInstance();
                    Method[] declaredMethods = o.getClass().getDeclaredMethods();
                    for (Method method : declaredMethods) {
                        if (method.isAnnotationPresent(Command.class)) {
                            Command commandAnnotation = method.getAnnotation(Command.class);
                            ObjectStore objectStore = new ObjectStore();
                            objectStore.setCommand(commandAnnotation.value());
                            objectStore.setDescription(commandAnnotation.description());
                            objectStore.setAction(commandAnnotation.action().isEmpty() ? null : commandAnnotation.action());
                            objectStore.setMethod(method);
                            objectStore.setInstance(o);
                            objectStoreList.add(objectStore);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        scanPackages();
        try {
            serverSocket = new ServerSocket(PORT);
            if (!serverSocket.isClosed()) {
                logger.info("Server started success .");
                logger.info("Listen on port: " + PORT);
            }
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                new TelnetSocketThread(socket, prompt, objectStoreList).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}
