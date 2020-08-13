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

import ir.moke.toolkit.api.Option;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class TelnetSocketThread extends Thread {

    private Socket socket;
    private String prompt;
    private List<ObjectStore> objectStoreList;

    public TelnetSocketThread(Socket socket, String prompt, List<ObjectStore> objectStoreList) {
        this.socket = socket;
        this.prompt = prompt;
        this.objectStoreList = objectStoreList;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (!socket.isClosed()) {
                writePrompt(bufferedWriter);
                String str = bufferedReader.readLine();
                System.out.println(">> : " + str);
                if (str != null) {
                    for (byte aByte : str.getBytes()) {
                        if (((int) aByte) < 0) {
                            socket.close();
                        }
                    }
                    if (str.equalsIgnoreCase("quit")) {
                        socket.close();
                    }
                    if (str.equals("")) {
                        continue;
                    } else {
                        String[] s = str.split(" ");
                        List<ObjectStore> objectStoreList = this.objectStoreList.stream()
                                .filter(e -> e.getCommand().equals(s[0]))
                                .collect(toList());
                        if (!objectStoreList.isEmpty()) {
                            Optional<ObjectStore> optionalObjectStore;
                            if (s.length == 1) {
                                optionalObjectStore = objectStoreList.stream().filter(e -> e.getAction() == null).findFirst();
                            } else {
                                optionalObjectStore = objectStoreList.stream().filter(e -> e.getAction().equals(s[1])).findFirst();
                            }

                            optionalObjectStore.ifPresent(objectStore -> invokeCommand(objectStore, bufferedWriter, str));

                        } else {
                            printUnknownInformation(bufferedWriter);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printUnknownInformation(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(TtyAsciiCodecs.RED + TtyAsciiCodecs.BLINK + "Unknown Command" + TtyAsciiCodecs.RESET);
        bufferedWriter.write("\n");
        bufferedWriter.write(TtyAsciiCodecs.BACKGROUND_RED + "Enter help for get list of available commands." + TtyAsciiCodecs.RESET);
        bufferedWriter.write("\n");
        bufferedWriter.flush();
    }

    private void invokeCommand(ObjectStore objectStore, BufferedWriter bufferedWriter, String cmdLine) {
        Object object = objectStore.getInstance();
        Method method = objectStore.getMethod();
        try {
            String[] parts = cmdLine.split(" ");
            String result = "";
            if (parts.length > 1) {
                List<Parameter> parameterList = Arrays.asList(method.getParameters());
                if (parameterList.size() > 0) {
                    List<String> values = new ArrayList<>();
                    for (Parameter parameter : parameterList) {
                        String parameterName = parameter.getAnnotation(Option.class).value();
                        for (int i = 2; i < parts.length; i++) {
                            String key = parts[i].split("=")[0];
                            String value = parts[i].split("=")[1];
                            if (key.equals(parameterName)) {
                                values.add(value);
                            }
                        }
                    }
                    result = (String) method.invoke(objectStore.getInstance(), values);
                } else {
                    result = (String) method.invoke(object);
                }
            } else {
                result = (String) method.invoke(object);
            }
            bufferedWriter.write(result);
            bufferedWriter.write('\n');
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writePrompt(BufferedWriter bufferedWriter) throws IOException {
        if (!socket.isClosed()) {
            bufferedWriter.write(prompt);
            bufferedWriter.flush();
        }
    }
}