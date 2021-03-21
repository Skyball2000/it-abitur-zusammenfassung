public class Leaf implements Comparable<Leaf> {
    private final String text;
    private final String linkText;

    public Leaf(String text) {
        this.linkText = text;
        this.text = text.replaceAll(".+>(.+)</a>", "$1");
    }

    public String getText() {
        return text;
    }

    public String getLinkText() {
        return linkText;
    }

    @Override
    public int compareTo(Leaf leaf) {
        String compareText = leaf.getText().replaceAll(".+>(.+)</a>", "$1");
        if (text.matches("\\d+.*") && compareText.matches("\\d+.*")) {
            int myNumber = Integer.parseInt(text.replaceAll("(\\d+).*", "$1"));
            int compareNumber = Integer.parseInt(compareText.replaceAll("(\\d+).*", "$1"));
            return Integer.compare(myNumber, compareNumber);
        }
        return text.compareTo(compareText);
    }
}
