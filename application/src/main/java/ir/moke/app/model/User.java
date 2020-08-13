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

package ir.moke.app.model;

import ir.moke.app.StringUtils;

public class User {
    /*
     * |Username        |EmailAddress       |Name       |Family         |age    |
     */
    private static final String printFormat = "|%-5d|%-20s|%-20s|%-20s|%-20s|%-4d|";
    private int id;
    private String username;
    private String emailAddress;
    private String name;
    private String family;
    private int age;

    public User(int id, String username, String emailAddress, String name, String family, int age) {
        this.id = id;
        this.username = username;
        this.emailAddress = emailAddress;
        this.name = name;
        this.family = family;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return StringUtils.truncate(username, 15);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return StringUtils.truncate(emailAddress, 15);
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return StringUtils.truncate(name, 15);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return StringUtils.truncate(family, 15);
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format(printFormat,
                getId(),
                getUsername(),
                getEmailAddress(),
                getName(),
                getFamily(),
                age);
    }
}
