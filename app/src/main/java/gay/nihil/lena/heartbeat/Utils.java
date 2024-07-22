package gay.nihil.lena.heartbeat;

import java.time.Instant;

public class Utils {

    public static User getPlaceholderUser() {
        User u = new User();
        u.id = -1;
        u.name = "Unknown User";

        return u;
    }

    public static String flagsToText(int flags) {
        if (flags == 0) {
            return "Device offline";
        } else {
            return "Device online";
        }
    }

    public static String timestampToText(long timestamp) {

        long now = Instant.now().getEpochSecond();

        long difference = now - timestamp;

        if (difference < 60) {
            return "Now";
        }

        if (difference < 3600) {
            return (difference / 60) + "m";
        }

        if (difference < 86400) {
            return (difference / 3600) + "h";
        }

        if (difference < 604800) {
            return (difference / 86400) + "d";
        }

        return Instant.ofEpochSecond(timestamp).toString();
    }
}
