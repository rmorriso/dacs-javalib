package fedroot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * StringUtility class 
 * (?? originally developed by the NFIS Project Office)
 */
public class StringUtility {
    
    /** System specific newline character*/
    public final static String NEW_LINE = System.getProperty("line.separator");
    
    /** Two system specific newline characters */
    public final static String TWO_NEW_LINES = NEW_LINE + NEW_LINE;
    
    /**
     * Windows new line, sometimes needed on Linux server to create text that
     * looks OK in a Windows application like WordPad.
     */
    public final static String WINDOWS_NEW_LINE =
            new String(new byte[] {13, 10});
    
    public final static String TWO_WINDOWS_NEW_LINES =
            WINDOWS_NEW_LINE + WINDOWS_NEW_LINE;
    /**
     *
     * @param listString string containing strings delimited by the delimiter
     * @param delimiter the character that delimits the tokens
     * @return list contains the parsed strings with surrounding white space
     *          trimmed
     */
    public static List<String> getListFromDelimitedString
            (String listString, String delimiter) {
        
        StringTokenizer st = new StringTokenizer(listString, delimiter);
        List<String> stringlist = new ArrayList<String>();
        
        while (st.hasMoreTokens()) {
            stringlist.add(new String(st.nextToken().trim()));
        }
        return stringlist;
    }
    
    /**
     * convert a string to a SQL string
     */
    public static String sqlString(String string) {
        
        string = string.replaceAll("'", "''");
        return string;
    }
    
    /**
     * Method is for backwards compatiability.
     * Calls {@link #formatWithNewLine(String, int, boolean) with a value of
     * false for the boolean parameter.
     *
     * @deprecated use {@link #formatWithNewLine(String, int, boolean)
     * @param input unformatted text to add line feeds to
     * @param maxLineLength number of characters before adding a new line
     * @return the formated string
     */
    public static String formatWithNewLine(String input, int maxLineLength) {
        
        return formatWithNewLine(input, maxLineLength, false);
    }
    /**
     * Formats the input string to have line wraps at or before maxLineLength.
     * If possible the line will be broken on a space. Otherwise it will be
     * broken at maxLineLength.
     *
     * @param input the String to be formatted
     * @param maxLineLength the maximum character length for a line
     * @param useWindowsLineFeed if true the line feed is ASCII sequence 13 10 
     * otherwise Unix line feed is used: ASCII 10.
     * @return the wrapped String or null if the input string was null
     */
    public static String formatWithNewLine
            (String input, int maxLineLength, boolean useWindowsLineFeed) {
        
        // a help when text from properties are not found and are null
        if (input == null) {
            return null;
        }
        
        StringBuffer buffer = new StringBuffer();
        String currentLine = null;
        int currentIndex = 0;
        int lastSpaceIndex = 0;
        int endOfLineIndex = 0;
        int endIndex = input.length();
        
        while (currentIndex < endIndex) {
            endOfLineIndex = currentIndex + maxLineLength;
            
            // if the remaining characters are less than the beginning of the next
            // line we don't need any more line breaks.
            if (endIndex < endOfLineIndex) {
                
                buffer.append(input.substring(currentIndex));
                
                // we are done so advance current to the end
                currentIndex = endIndex;
            } else {
                currentLine = input.substring(currentIndex, endOfLineIndex);
                lastSpaceIndex = currentLine.lastIndexOf(" ");  // -1 if no space found
                if (lastSpaceIndex < 0) {
                    lastSpaceIndex = currentLine.length();
                } else {
                    lastSpaceIndex += 1;
                }
                
                buffer.append(input.substring
                        (currentIndex, currentIndex + lastSpaceIndex));
                
                if (useWindowsLineFeed) {
                    buffer.append(WINDOWS_NEW_LINE);    //new line char
                } else {
                    buffer.append(NEW_LINE);    //new line char
                }
                currentIndex += lastSpaceIndex;
            }
        }
        return buffer.toString();
    }
}
