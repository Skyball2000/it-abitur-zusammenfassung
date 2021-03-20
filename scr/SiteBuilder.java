import yanwittmann.*;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SiteBuilder {

    private final String siteOutDir;
    private final String sitePagesDir;
    private final String siteTemplateDir;
    private final String siteImagesDir;
    private final String templatePlaceTitle;
    private final String templatePlaceBody;
    private final String templatePlaceCss;
    private final String templatePlaceWebsiteTitle;
    private final String templatePlaceIcon;
    private final String templatePlaceMainPage;
    private final String templatePlaceContents;
    private final String templatePlaceSearchList;
    private final String templateInsert;
    private final String templateSubTitle;
    private final String templateMainTitle;
    private final String templateTextTitle;
    private final String templateTextParagraphIntro;
    private final String templateCss;
    private final String templateWebsiteTitle;
    private final String templateIcon;
    private final String templateSubTitleDefault;
    private final String templateMainCss;
    private final String templateInformationCss;
    private final String siteImageIcon;
    private final String siteTitleImage;
    private final String mainPageUrl;
    private final String regexLink;
    private final String regexLinkReplace;

    public SiteBuilder(Configuration configuration) {
        siteOutDir = configuration.getOrDefault("siteOutDir", "out\\site\\");
        sitePagesDir = configuration.getOrDefault("sitePagesDir", "res\\site\\pages\\");
        siteTemplateDir = configuration.getOrDefault("siteTemplateDir", "res\\site\\templates\\");
        siteImagesDir = configuration.getOrDefault("siteImagesDir", "res\\site\\img\\");

        templatePlaceTitle = configuration.getOrDefault("templatePlaceTitle", "BUILDER-PLACE-TITLE");
        templatePlaceBody = configuration.getOrDefault("templatePlaceBody", "BUILDER-PLACE-BODY");
        templatePlaceCss = configuration.getOrDefault("templatePlaceCss", "BUILDER-PLACE-CSS");
        templatePlaceWebsiteTitle = configuration.getOrDefault("templatePlaceWebsiteTitle", "BUILDER-PLACE-WEBSITE-TITLE");
        templatePlaceIcon = configuration.getOrDefault("templatePlaceIcon", "BUILDER-PLACE-ICON");
        templatePlaceMainPage = configuration.getOrDefault("templatePlaceMainPage", "BUILDER-PLACE-MAIN-PAGE");
        templatePlaceContents = configuration.getOrDefault("templatePlaceContents", "BUILDER-PLACE-CONTENTS");
        templatePlaceSearchList = configuration.getOrDefault("templatePlaceSearchList", "BUILDER-PLACE-SEARCH-LIST");
        templateInsert = configuration.getOrDefault("templateInsert", "INSERT");
        templateSubTitle = configuration.getOrDefault("templateSubTitle", "<h1>INSERT</h1>");
        templateMainTitle = configuration.getOrDefault("templateMainTitle", "<h1>INSERT</h1>");
        templateTextTitle = configuration.getOrDefault("templateTextTitle", "<h4>INSERT</h4>");
        templateTextParagraphIntro = configuration.getOrDefault("templateTextParagraphIntro", "<p>");
        templateCss = configuration.getOrDefault("templateCss", "<link rel=\"stylesheet\" href=\"INSERT\" media=\"screen\">");
        templateWebsiteTitle = configuration.getOrDefault("templateWebsiteTitle", "<title>INSERT</title>");
        templateIcon = configuration.getOrDefault("templateIcon", "<img src=\"INSERT\" class=\"u-logo-image u-logo-image-1\" data-image-width=\"80.4586\">");

        templateSubTitleDefault = configuration.getOrDefault("templateSubTitleDefault", "Hauptthema");
        templateMainCss = configuration.getOrDefault("templateMainCss", "nicepage.css");
        templateInformationCss = configuration.getOrDefault("templateInformationCss", "information.css");

        siteImageIcon = configuration.getOrDefault("siteImageIcon", "itabiicon.png");
        siteTitleImage = configuration.getOrDefault("siteTitleImage", "227231823-carl-bosch-schule-Lef1.jpg");
        mainPageUrl = configuration.getOrDefault("mainPageUrl", "http://yanwittmann.de");

        regexLink = configuration.getOrDefault("regexLink", "\\[\\[href=([^|]+)\\|([^]]+)]]");
        regexLinkReplace = configuration.getOrDefault("regexLinkReplace", "<a href=\"$1\">$2</a>");
    }

    public void clearOldSite() {
        FileUtils.deleteDirectory(new File(siteOutDir));
    }

    private ArrayList<String> orderedPages;
    private final PageTreeBuilder pageTreeBuilder = new PageTreeBuilder();

    public void buildSite() {
        ArrayList<File> files = FileUtils.getFiles(new File(sitePagesDir));
        files.forEach(this::registerPage);
        informationPages.forEach(pageTreeBuilder::add);
        pageTreeBuilder.finish();
        orderedPages = pageTreeBuilder.getOrderedPages();

        ArrayList<String> template = FileUtils.readFileToArrayList(new File(siteTemplateDir + "informationtemplate.html"));
        files.forEach(siteFile -> buildInformationPage(siteFile, template));

        FileUtils.copyFile(new File(siteTemplateDir + "information.css"), new File(siteOutDir + "information.css"));
        FileUtils.copyFile(new File(siteTemplateDir + "Hauptseite.css"), new File(siteOutDir + "Hauptseite.css"));
        FileUtils.copyFile(new File(siteTemplateDir + "nicepage.css"), new File(siteOutDir + "nicepage.css"));
        FileUtils.copyFile(new File(siteTemplateDir + "search.js"), new File(siteOutDir + "search.js"));
        FileUtils.copyFile(new File(siteImagesDir + siteImageIcon), new File(siteOutDir + "images\\" + siteImageIcon));
        FileUtils.copyFile(new File(siteImagesDir + siteTitleImage), new File(siteOutDir + "images\\" + siteTitleImage));
        buildMainPage();

        if (warnings.size() > 0) {
            System.out.println("---------------- warnings ----------------");
            warnings.stream().sorted().forEach(System.out::println);
            System.out.println("------------- warnings over --------------");
        }
    }

    private void buildMainPage() {
        ArrayList<String> template = FileUtils.readFileToArrayList(new File(siteTemplateDir + "Hauptseite.html"));

        HTMLListBuilder mainList = pageTreeBuilder.finish();
        String searchList = generateSearchList();

        LineBuilder generatedPage = new LineBuilder();
        for (String line : template) {
            String trimmedLine = line.trim();
            if (trimmedLine.equals(templatePlaceBody)) {
                generatedPage.append(generatedPage.toString());
            } else if (trimmedLine.equals(templatePlaceCss)) {
                generatedPage.append(templateCss.replace(templateInsert, templateMainCss));
                generatedPage.append(templateCss.replace(templateInsert, templateInformationCss));
            } else if (trimmedLine.equals(templatePlaceIcon)) {
                generatedPage.append(templateIcon.replace(templateInsert, "images\\" + siteImageIcon));
            } else if (line.contains(templatePlaceMainPage)) {
                generatedPage.append(line.replace(templatePlaceMainPage, mainPageUrl));
            } else if (line.contains(templatePlaceContents)) {
                generatedPage.append(line.replace(templatePlaceContents, mainList.toString()));
            } else if (line.contains(templatePlaceSearchList)) {
                generatedPage.append(line.replace(templatePlaceSearchList, searchList));
            } else {
                generatedPage.append(line);
            }
        }

        FileUtils.writeFile(new File(siteOutDir + "index.html"), optimizeGeneratedPage(generatedPage.toString()));
    }

    private String generateSearchList() {
        LineBuilder searches = new LineBuilder();
        informationPages.stream().map(InformationPage::generateSearchEntry).forEach(searches::append);
        return searches.toString();
    }

    private Pair<String, String> getSurroundingInfoPages(File page) {
        Pair<String, String> pair = new Pair<>();
        String lookingForPage = page.toString().replace(".txt", ".html").replace(sitePagesDir, "");
        for (int i = 0, orderedPagesSize = orderedPages.size(); i < orderedPagesSize; i++) {
            String orderedPage = orderedPages.get(i);
            if (orderedPage.contains(lookingForPage)) {
                if (i - 1 >= 0)
                    pair.setLeft(orderedPages.get(i - 1));
                if (i + 1 < orderedPagesSize)
                    pair.setRight(orderedPages.get(i + 1));
            }
        }
        if (pair.getLeft() == null)
            pair.setLeft("");
        if (pair.getRight() == null)
            pair.setRight("");
        return pair;
    }

    private void registerPage(File siteFile) {
        String path = prepareInformationPagePath(siteFile.getPath());
        String pageTitle = "Title";
        for (String line : FileUtils.readFileToArrayList(siteFile))
            if (line.startsWith("# ")) { //main title
                if (line.contains("!!")) line = line.replaceAll("!!.*", "");
                if (line.contains("!")) line = line.replaceAll("(.*)!.*", "$1");
                pageTitle = line.replace("# ", "");
                break;
            }
        pathToMainDirectory = IntStream.range(0, GeneralUtils.countOccurrences(path, "\\") + (path.equals(templateSubTitleDefault) ? 0 : 1))
                .mapToObj(i -> "..\\").collect(Collectors.joining());
        informationPages.add(new InformationPage(pageTitle, siteFile, path.replace(templateSubTitleDefault, "")));
    }

    private final ArrayList<String> warnings = new ArrayList<>();
    private final ArrayList<InformationPage> informationPages = new ArrayList<>();
    private String pathToMainDirectory;

    private void buildInformationPage(File siteFile, ArrayList<String> template) {
        String path = prepareInformationPagePath(siteFile.getPath());
        String pageSubtitle = path.replace("\\", " >> ");
        String pageTitle = "Title";
        pathToMainDirectory = IntStream.range(0, GeneralUtils.countOccurrences(path, "\\") + (path.equals(templateSubTitleDefault) ? 0 : 1))
                .mapToObj(i -> "..\\").collect(Collectors.joining());
        boolean pageErrorWarning = false, pageIncompleteWarning = false;
        String warningMessage = "";

        LineBuilder generatedBody = new LineBuilder();
        boolean isCurrentlyTextOrImage = false;
        ArrayList<String> readFileToArrayList = FileUtils.readFileToArrayList(siteFile);
        for (int i = 0; i < readFileToArrayList.size(); i++) {
            String line = readFileToArrayList.get(i);
            if (line.startsWith("# ")) { //main title
                if (line.contains("!!")) {
                    pageTitle = "<font style=\"color:red\">" + line.replace("# ", "").replaceAll("!!.*", "") + "</font>";
                    pageErrorWarning = true;
                    warningMessage = line.replaceAll(".+!!(.*)", "$1");
                } else if (line.contains("!")) {
                    pageTitle = "<font style=\"color:orange\">" + line.replace("# ", "").replaceAll("(.+)!.*", "$1") + "</font>";
                    pageIncompleteWarning = true;
                    warningMessage = line.replaceAll(".+!(.*)", "$1");
                } else
                    pageTitle = line.replace("# ", "");
            } else if (line.startsWith("## ")) { //title in text
                if (isCurrentlyTextOrImage) {
                    isCurrentlyTextOrImage = false;
                    generatedBody.append("</p>");
                }
                generatedBody.append(templateTextTitle.replace(templateInsert, line.replace("## ", "")));
            } else if (line.startsWith(">")) { //keywords, do nothing. They are evaluated somewhere else
            } else if (line.startsWith("img ")) { //image
                if (!isCurrentlyTextOrImage) {
                    isCurrentlyTextOrImage = true;
                    generatedBody.append(templateTextParagraphIntro);
                }
                generatedBody.append("<br>").append("<img style=\"margin-top:10px;max-width:100%;\" src=\"" + prepareImageLink(line.replace("img ", "")) + "\"/><br>");
            } else if (line.startsWith("-")) { //list
                if (isCurrentlyTextOrImage) {
                    isCurrentlyTextOrImage = false;
                    generatedBody.append("</p>");
                }
                generatedBody.append("<ul class=\"u-text u-text-2\">");
                int lastIndentCount = 1;
                do {
                    int indentCount = line.replaceAll("(-+) ?.+", "$1").length();
                    int storedIndentCount = indentCount;
                    while (indentCount != lastIndentCount)
                        if (indentCount > lastIndentCount) {
                            indentCount--;
                            generatedBody.append("<ul>");
                        } else {
                            indentCount++;
                            generatedBody.append("</ul>");
                        }
                    lastIndentCount = storedIndentCount;
                    generatedBody.append("<li>" + prepareBodyText(line.replaceAll("-+ ?(.+)", "$1"), pathToMainDirectory) + "</li>");
                    i++;
                    if (i >= readFileToArrayList.size()) break;
                    line = readFileToArrayList.get(i);
                } while (line.startsWith("-"));
                int indentCount = 0;
                while (indentCount != lastIndentCount) {
                    if (indentCount > lastIndentCount) {
                        indentCount--;
                        generatedBody.append("<ul>");
                    } else {
                        generatedBody.append("</ul>");
                        indentCount++;
                    }
                }
                i--;

            } else if (line.startsWith("~")) { //list
                if (isCurrentlyTextOrImage) {
                    isCurrentlyTextOrImage = false;
                    generatedBody.append("</p>");
                }
                generatedBody.append("<ol class=\"u-text u-text-2\">");
                int lastIndentCount = 1;
                do {
                    int indentCount = line.replaceAll("(~+) ?.+", "$1").length();
                    int storedIndentCount = indentCount;
                    while (indentCount != lastIndentCount)
                        if (indentCount > lastIndentCount) {
                            indentCount--;
                            generatedBody.append("<ol>");
                        } else {
                            indentCount++;
                            generatedBody.append("</ol>");
                        }
                    lastIndentCount = storedIndentCount;
                    generatedBody.append("<li>" + prepareBodyText(line.replaceAll("~+ ?(.+)", "$1"), pathToMainDirectory) + "</li>");
                    i++;
                    if (i >= readFileToArrayList.size()) break;
                    line = readFileToArrayList.get(i);
                } while (line.startsWith("~"));
                int indentCount = 0;
                while (indentCount != lastIndentCount) {
                    if (indentCount > lastIndentCount) {
                        indentCount--;
                        generatedBody.append("<ol>");
                    } else {
                        generatedBody.append("</ol>");
                        indentCount++;
                    }
                }
                i--;

            } else if (line.length() > 0) { //regular text
                if (!isCurrentlyTextOrImage) {
                    isCurrentlyTextOrImage = true;
                    generatedBody.append(templateTextParagraphIntro);
                }
                generatedBody.append(prepareBodyText(line, pathToMainDirectory));
                if (line.contains("</table>")) {
                    generatedBody.append("</p>");
                    generatedBody.append(templateTextParagraphIntro);
                }
            }
        }

        if (pageErrorWarning) {
            if (!isCurrentlyTextOrImage)
                generatedBody.append(templateTextParagraphIntro);
            generatedBody.append(templateTextTitle.replace(templateInsert, "<br><font style=\"color:red\"><b>Warnung:</b></font>"));
            generatedBody.append(templateTextParagraphIntro);
            generatedBody.append(prepareBodyText("Diese Seite könnte Fehler oder Ungenauigkeiten enthalten oder noch nicht fertig sein.<br>", pathToMainDirectory));
            if (warningMessage.length() > 0)
                generatedBody.append(prepareBodyText("Grund: <b>" + warningMessage + "</b><br>", pathToMainDirectory));
        }
        if (pageIncompleteWarning) {
            if (!isCurrentlyTextOrImage)
                generatedBody.append(templateTextParagraphIntro);
            generatedBody.append(templateTextTitle.replace(templateInsert, "<br><font style=\"color:orange\"><b>Warnung:</b></font>"));
            generatedBody.append(templateTextParagraphIntro);
            generatedBody.append(prepareBodyText("Diese Seite entspricht noch nicht den Ansprüchen, die wir für diese Website haben.<br>", pathToMainDirectory));
            if (warningMessage.length() > 0)
                generatedBody.append(prepareBodyText("Grund: <b>" + warningMessage + "</b><br>", pathToMainDirectory));
        }
        if (pageErrorWarning || pageIncompleteWarning) {
            generatedBody.append(prepareBodyText("Falls du einen Verbesserungsvorschlag hast, kontaktiere uns bitte über den Link `Hilf mit!` in der grauen Footer-Leiste direkt unten diesem Text:", pathToMainDirectory));
        }

        LineBuilder generatedPage = new LineBuilder();
        for (String line : template) {
            String trimmedLine = line.trim();
            if (trimmedLine.equals(templatePlaceTitle)) {
                Pair<String, String> beforeNext = getSurroundingInfoPages(siteFile);
                beforeNext.setLeft(beforeNext.getLeft().replace("href=\"", "href=\"" + pathToMainDirectory)
                        .replaceAll("(.*)>(.+)</a>", "$1 title=\"$2\">$2</a>")
                        .replaceAll(">(.+)</a>", ">&lt;</a>"));
                beforeNext.setRight(beforeNext.getRight().replace("href=\"", "href=\"" + pathToMainDirectory)
                        .replaceAll("(.*)>(.+)</a>", "$1 title=\"$2\">$2</a>")
                        .replaceAll(">(.+)</a>", ">&gt;</a>"));
                generatedPage.append(templateMainTitle.replace(templateInsert, beforeNext.getLeft() + " &#160&#160&#160 " + pageTitle + " &#160&#160&#160 " + beforeNext.getRight()));
                if (pageErrorWarning || pageIncompleteWarning)
                    generatedPage.append(templateSubTitle.replace(templateInsert, "Bitte Warnung ganz unten lesen!<br><br>" + pageSubtitle));
                else
                    generatedPage.append(templateSubTitle.replace(templateInsert, pageSubtitle));
            } else if (trimmedLine.equals(templatePlaceBody)) {
                generatedPage.append(generatedBody.toString());
            } else if (trimmedLine.equals(templatePlaceCss)) {
                generatedPage.append(templateCss.replace(templateInsert, pathToMainDirectory + templateMainCss));
                generatedPage.append(templateCss.replace(templateInsert, pathToMainDirectory + templateInformationCss));
            } else if (trimmedLine.equals(templatePlaceWebsiteTitle)) {
                generatedPage.append(templateWebsiteTitle.replace(templateInsert, pageTitle.replaceAll("<[^>]+>", "")));
            } else if (trimmedLine.equals(templatePlaceIcon)) {
                generatedPage.append(templateIcon.replace(templateInsert, pathToMainDirectory + "images\\" + siteImageIcon));
            } else if (line.contains(templatePlaceMainPage)) {
                generatedPage.append(line.replace(templatePlaceMainPage, pathToMainDirectory + mainPageUrl));
            } else {
                generatedPage.append(line);
            }
        }

        System.out.println("Generated " + pageSubtitle + " / " + pageTitle.replaceAll("<[^>]+>", "") +
                (pageErrorWarning ? " WARNING 1" : "") + (pageIncompleteWarning ? " WARNING 2" : ""));
        String warningMessageDisplay = warningMessage.length() > 0 ? " --> " + warningMessage : "";
        if (pageErrorWarning)
            warnings.add("WARNING 1: " + pageSubtitle + "/ " + pageTitle.replaceAll("<[^>]+>", "") +
                    warningMessageDisplay);
        if (pageIncompleteWarning)
            warnings.add("WARNING 2: " + pageSubtitle + "/ " + pageTitle.replaceAll("<[^>]+>", "") +
                    warningMessageDisplay);

        FileUtils.writeFile(new File(siteOutDir + path.replace(templateSubTitleDefault, "") + "\\" + siteFile.getName().replace(".txt", ".html")), optimizeGeneratedPage(generatedPage.toString()));
    }

    private String prepareImageLink(String link) {
        if (link.contains("http"))
            return link;
        File image = new File(siteImagesDir + link);
        if (image.exists()) {
            File destination = new File(siteOutDir + "\\images\\" + link);
            FileUtils.makeDirectories(destination.getPath().replace(image.getName(), ""));
            FileUtils.copyFile(image, destination);
        } else System.out.println("Image does not exist: " + image.getPath());
        return (pathToMainDirectory + "\\images\\" + link).replaceAll("^\\\\", "");
    }

    private String prepareBodyText(String text, String pathToMainDirectory) {
        text = text.replaceAll(regexLink, regexLinkReplace).replace("``", "`&nbsp`").replaceAll("^` ", "`&nbsp")
                .replaceAll("`([^`]+)`", "<code>$1</code>").replace("  ", "&nbsp;&nbsp;")
                .replace("<<", "&lt&lt").replace(">>", "&gt&gt")
                .replaceAll("\\^([^ <>]+)", "<sup>$1</sup>");
        if (text.matches(".*\\[([^]]+)].*")) {
            Pattern pattern = Pattern.compile("\\[([^]]+)]");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String found = matcher.group();
                text = text.replace(found, getMostLikelyLink(found.replace("[", "").replace("]", ""), pathToMainDirectory));
            }
        }
        return text;
    }

    private String getMostLikelyLink(String linkText, String pathToMainDirectory) {
        String[] splitted = linkText.split("\\|", 3);
        if (splitted.length == 1) {
            return "<font id=\"" + linkText + "\">" + linkText + "</font>";
        } else
            for (InformationPage informationPage : informationPages)
                if (informationPage.getDisplayName().contains(splitted[1])) {
                    String section;
                    if (splitted.length == 3) section = "#" + splitted[2];
                    else section = "";
                    return "<a href=\"" + pathToMainDirectory + informationPage.getPath() + "\\" +
                            informationPage.getFile().getName().replace(".txt", ".html") + section + "\">" + splitted[0] + "</a>";
                }
        return splitted[0];
    }

    private String prepareInformationPagePath(String path) {
        path = path.replaceAll(".*" + sitePagesDir.replace("\\", "\\\\") + "(.+)", "$1");
        return path.contains("\\") ? path.replaceAll("(.+)\\\\.+", "$1") : templateSubTitleDefault;
    }

    private String optimizeGeneratedPage(String generated) {
        return generated.replaceAll("\n<br>", "<br>");
    }
}
