package polytech.tthomas;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MessageFormatter {
    public static String format(String author, String message) {
        final LocalTime dateTime = LocalTime.now();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("[%s] %s - %s%n", dateTimeFormatter.format(dateTime), author, message);
    }
}
