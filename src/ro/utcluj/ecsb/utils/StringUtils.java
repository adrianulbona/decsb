package ro.utcluj.ecsb.utils;

import java.util.regex.Pattern;

import com.thoughtworks.xstream.XStream;

/**
 * Offers two methods to convert an object to a string representation and restore the object given its string
 * representation. Should use Hadoop Stringifier whenever available.
 */
public final class StringUtils {

    private static final XStream XSTREAM = new XStream();
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\n");

    private StringUtils() {
        // do nothing
    }

    /**
     * Converts the object to a one-line string representation
     *
     * @param obj
     *          the object to convert
     * @return the string representation of the object
     */
    public static String toString(Object obj) {
        return NEWLINE_PATTERN.matcher(XSTREAM.toXML(obj)).replaceAll("");
    }

    /**
     * Restores the object from its string representation.
     *
     * @param str
     *          the string representation of the object
     * @return restored object
     */
    public static <T> T fromString(String str) {
        return (T) XSTREAM.fromXML(str);
    }

    public static String escapeXML(String input){
        return input.replaceAll("\"|\\&|\\<|\\>|\'", "_");
    }
}
