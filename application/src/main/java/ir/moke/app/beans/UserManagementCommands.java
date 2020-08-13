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

import ir.moke.app.model.User;
import ir.moke.toolkit.TtyAsciiCodecs;
import ir.moke.toolkit.api.CLI;
import ir.moke.toolkit.api.Command;
import ir.moke.toolkit.api.Option;

import java.util.ArrayList;
import java.util.List;

@CLI
public class UserManagementCommands implements TtyAsciiCodecs {

    private static final List<User> USER_LIST = new ArrayList<>();

    @Command(value = "user", action = "add", description = "Add new user")
    public String createUser(@Option("username") String username,
                             @Option("emailAddress") String emailAddress,
                             @Option("name") String firstName,
                             @Option("family") String lastName,
                             @Option("age") int age) {
        String rowSeparator = "+-----+--------------------+--------------------+--------------------+--------------------+----+";
        String headerFormat = "|" + BOLD + GREEN + "%-5s" + RESET + "|" + BOLD + GREEN + "%-20s" + RESET + "|" + BOLD + GREEN + "%-20s" + RESET + "|" + BOLD + GREEN + "%-20s" + RESET + "|" + BOLD + GREEN + "%-20s" + "|" + BOLD + GREEN + "%-4s" + RESET + "|";
        User user = new User(USER_LIST.size() + 1, username, emailAddress, firstName, lastName, age);
        USER_LIST.add(user);
        String header = String.format(headerFormat, "ID", "USERNAME", "Email Address", "NAME", "FAMILY", "AGE");

        StringBuilder result = new StringBuilder();
        result.append(rowSeparator).append("\n");
        result.append(header).append("\n");
        result.append(rowSeparator).append("\n");
        result.append(user.toString()).append("\n");
        result.append(rowSeparator).append("\n");
        return result.toString();
    }

    @Command(value = "user", action = "list", description = "Print list of registered users")
    public String getUserList() {
        StringBuilder result = new StringBuilder();
        if (USER_LIST.isEmpty()) {
            result.append(BLINK)
                    .append(BACKGROUND_RED)
                    .append("List is empty")
                    .append(RESET)
                    .append("\n");
        } else {
            result.append(BLINK)
                    .append(GREEN)
                    .append("List size [")
                    .append(BLINK)
                    .append(USER_LIST.size())
                    .append("]")
                    .append(RESET)
                    .append("\n");
            String rowSeparator = "+-----+--------------------+--------------------+--------------------+--------------------+----+";
            String headerFormat = "|" + BOLD + GREEN + "%-5s" + RESET + "|" + BOLD + GREEN + "%-20s" + RESET + "|" + BOLD + GREEN + "%-20s" + RESET + "|" + BOLD + GREEN + "%-20s" + RESET + "|" + BOLD + GREEN + "%-20s" + "|" + BOLD + GREEN + "%-4s" + RESET + "|";
            String header = String.format(headerFormat, "ID", "USERNAME", "Email Address", "NAME", "FAMILY", "AGE");

            result.append(rowSeparator).append("\n");
            result.append(header).append("\n");
            result.append(rowSeparator).append("\n");
            USER_LIST.stream().map(User::toString).peek(result::append).forEach(e -> result.append("\n"));
            result.append(rowSeparator).append("\n");
        }
        return result.toString();
    }


    public static void main(String[] args) {
        UserManagementCommands umc = new UserManagementCommands();
        String newUser = umc.createUser("dsa", "dsadas", "dsadas", "dsada", 12);
        User user1 = new User(1, "dsa", "dsadas", "dsadas", "dsada", 12);
        User user2 = new User(2, "dsjadhka", "mahsom@gmail.com", "dsadas", "dsada", 12);
        User user3 = new User(3, "dsadadad", "dsajhdk@yahoo.com", "dsadas", "dsada", 12);
        USER_LIST.add(user1);
        USER_LIST.add(user2);
        USER_LIST.add(user3);
    }
}
