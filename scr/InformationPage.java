import yanwittmann.FileUtils;

import java.io.File;

public class InformationPage {
    private final String displayName;
    private final String path;
    private final String[] pathTitles;
    private String[] keywords;
    private final File file;

    public InformationPage(String displayName, File file, String path) {
        this.displayName = displayName;
        this.file = file;
        this.path = path;
        this.pathTitles = path.split("\\\\+");
        keywords = FileUtils.readFileToArrayList(file).stream().filter(line -> line.startsWith(">")).findFirst().
                map(line -> line.replaceAll("> ?", "").split("[, ]")).orElse(keywords);
    }

    public File getFile() {
        return file;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPath() {
        return path;
    }

    public String[] getPathTitles() {
        return pathTitles;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String generateSearchEntry() {
        return "<li class=\"searchElement\"> " +
                "<a href=\"" + path + "\\" + file.getName().replace(".txt", ".html") + "\">" +
                path.replace("\\", " > ") + " > " + displayName +
                (keywords != null ? "<font style=\"display:none\">" + String.join(" ", keywords) + "</font>" : "") +"</a></li>";
    }

    @Override
    public String toString() {
        return "<a href=\"" + path + "\\" + file.getName().replace(".txt", ".html") + "\">" + displayName + "</a>";
    }
}
