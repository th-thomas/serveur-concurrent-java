package polytech.tthomas;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MessageLogger {
    public static void LogMessage(String author, String message) {
        final LocalTime dateTime = LocalTime.now();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.printf("[%s] %s - %s%n", dateTimeFormatter.format(dateTime), author, message);
    }
}
