public class Warning implements Comparable<Warning> {

    private final String type;
    private final String message;

    public Warning(String message) {
        this.type = WARNING_DEFAULT;
        this.message = message;
    }

    public Warning(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public void print() {
        System.out.println(type + ": " + message);
    }

    @Override
    public int compareTo(Warning o) {
        return this.type.compareTo(o.type);
    }

    public final static String WARNING_DEFAULT = "[WARNING 00]";
    public final static String WARNING_PAGE_ERROR = "[WARNING 01] Page contains warning";
    public final static String WARNING_PAGE_WARNING = "[WARNING 02] Page contains serious warning";
    public final static String WARNING_MULTIPLE_LINES_CODE = "[WARNING 03] Multiple lines of code without code block detected";
    public final static String WARNING_CODE_BLOCK_BR = "[WARNING 04] Code block ends with <br>";
    public final static String WARNING_CODE_BLOCK_NO_LANGUAGE = "[WARNING 05] Code block without code language";
    public final static String WARNING_CLICK_COUNTER_TOO_SHORT = "[WARNING 06] Click counter name is too short";
    public final static String WARNING_CLICK_COUNTER_ALREADY_EXISTS = "[WARNING 07] Click counter already exists on other site";
    public final static String WARNING_BUILD_NOT_ONLINE_READY = "[WARNING 08] Site is built for offline mode";
    public final static String WARNING_UNESCAPED_SQUARE_BRACKETS_IN_CODE = "[WARNING 09] Code contains unescaped [square brackets]";
    public final static String WARNING_IMAGE_NOT_FOUND = "[WARNING 10] Image does not exist";
}
