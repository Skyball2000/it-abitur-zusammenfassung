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

    private final int generateType = 1;

    public String toString(int type) {
        String br = "<br style=\"display: block; margin: 1px 0;\">";
        LineBuilder list = new LineBuilder();
        if (type == 0) {
            list.append((elementName.length() > 0 ? "<li>" + elementName + "\n" : "") + "<ul" + (ulParameters.length() > 0 ? " " + ulParameters : "") + ">");
            for (Object entry : entries) {
                if (entry instanceof HTMLListBuilder)
                    list.append(entry.toString());
                else list.append("<li>" + entry + "</li>");
            }
            list.append("</ul>" + (elementName.length() > 0 ? "</li>" : ""));
        } else if (type == 1) {
            if (elementName.length() > 0) {
                list.append("<button type=\"button\" class=\"collapsible\">" + elementName + "</button>\n");
                list.append("<div class=\"content\">");
            }
            list.append(br);
            boolean lastWasLink = false;
            for (Object entry : entries) {
                if (entry instanceof HTMLListBuilder) {
                    if (lastWasLink) {
                        lastWasLink = false;
                        list.append(br);
                    }
                    list.append(entry.toString());
                } else {
                    lastWasLink = true;
                    list.append(entry + "<br>");
                }
            }
            list.append(br);
            if (elementName.length() > 0)
                list.append("</div>");
        }
        return list.toString().replace(br + "\n</div>\n" + br, "</div>\n" +br);
    }

    @Override
    public String toString() {
        return toString(generateType);
    }
}