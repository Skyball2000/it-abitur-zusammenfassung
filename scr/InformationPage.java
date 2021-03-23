import yanwittmann.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InformationPage {
    private final String displayName;
    private final String path;
    private final String[] pathTitles;
    private final ArrayList<String> keywords = new ArrayList<>();
    private boolean isHidden = false;
    private final File file;

    public InformationPage(String displayName, File file, String path) {
        this.displayName = displayName;
        this.file = file;
        this.path = path;
        this.pathTitles = path.split("\\\\+");

        ArrayList<String> fileLines = FileUtils.readFileToArrayList(file);
        keywords.addAll(Arrays.asList(fileLines.stream().filter(line -> line.startsWith(">")).findFirst().
                map(line -> line.replaceAll("> ?", "").split("[, ]")).orElse(new String[]{})));
        for (String line : fileLines) {
            Pattern pattern = Pattern.compile("<b>([^<>]+)</b>");
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String found = matcher.group().replaceAll("<[^<>]+>", "");
                keywords.add(found);
            }
            if (line.startsWith("## ")) keywords.add(line.replace("##", ""));
        }

        for (int i = 0, keywordsLength = keywords.size(); i < keywordsLength; i++) {
            String keyword = keywords.get(i);
            if (keyword.equals("hidden")) {
                isHidden = true;
                keywords.remove(i);
                break;
            }
        }
    }

    public boolean isHidden() {
        return isHidden;
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
        return keywords.toArray(new String[0]);
    }

    public String generateSearchEntry() {
        return "<li class=\"searchElement\"> " +
                "<a href=\"" + path + "\\" + file.getName().replace(".txt", SiteBuilder.informationPageEnding) + "\">" +
                path.replace("\\", " > ") + " > " + displayName +
                (keywords.size() > 0 ? "<font style=\"display:none\">" + SiteBuilder.prepareSearchKeyword(String.join(" ", keywords)) + "</font>" : "") + "</a></li>";
    }

    @Override
    public String toString() {
        return "<a href=\"" + path + "\\" + file.getName().replace(".txt", SiteBuilder.informationPageEnding) + "\">" + displayName + "</a>";
    }
}
