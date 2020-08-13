## Telnet Socket Server

Implemented Telnet Socket Server with Java Runtime Version 11 

#### build and run :     
```shell script
mvn clean compile install ;(cd application/ ; mvn exec:java)
```

implement your command collection on a class , for example :    
```java
@CLI
public class InformationCommands {

        @Command(value = "echo", description = "Echo your texts")
        public String doEcho(@Option String str) {
            return str;
        }
}
``` 

now introduce package of this class to toolkit and run MainClass:    
```java
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
```