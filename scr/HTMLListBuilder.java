import yanwittmann.LineBuilder;

import java.util.ArrayList;

public class HTMLListBuilder {
    private final String elementName;
    private final ArrayList<Object> entries = new ArrayList<>();
    private String ulParameters = "";

    public HTMLListBuilder(String elementName) {
        this.elementName = elementName;
    }

    public HTMLListBuilder(String elementName, String ulParameters) {
        this.elementName = elementName;
        this.ulParameters = ulParameters;
    }

    public void add(Object entry) {
        entries.add(entry);
    }

    public void addAll(ArrayList<HTMLListBuilder> entry) {
        entries.addAll(entry);
    }

    public void setUlParameters(String ulParameters) {
        this.ulParameters = ulParameters;
    }

    public String getName() {
        return elementName;
    }

    public ArrayList<Object> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        LineBuilder list = new LineBuilder();
        list.append((elementName.length() > 0 ? "<li>" + elementName : "") + "<ul" + (ulParameters.length() > 0 ? " " + ulParameters : "") + ">");
        for (Object entry : entries) {
            if (entry instanceof HTMLListBuilder)
                list.append(entry.toString());
            else list.append("<li>" + entry + "</li>");
        }
        list.append("</ul>" + (elementName.length() > 0 ? "</li>" : ""));
        return list.toString();
    }
}