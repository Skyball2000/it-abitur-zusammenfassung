
import yanwittmann.api.CountApi;
import yanwittmann.types.Configuration;
import yanwittmann.types.LineBuilder;
import yanwittmann.types.Pair;
import yanwittmann.utils.FileUtils;
import yanwittmann.utils.GeneralUtils;

import java.io.File;
import java.io.IOException;
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
    private final String templatePlaceSearchRandomSite;
    private final String templatePlaceScript;
    private final String templatePlaceClickCounter;
    private final String templatePlaceSidebar;
    private final String templatePlaceAmountPages;
    private final String templateInsert;
    private final String templateInsert1;
    private final String templateInsert2;
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
    public static String informationPageEnding;
    public static String informationPageEndingForLink;
    private final String regexLink;
    private final String regexLinkReplace;

    public SiteBuilder(Configuration configuration, boolean buildSiteForWeb) {
        if (!buildSiteForWeb)
            warnings.add(new Warning(Warning.WARNING_BUILD_NOT_ONLINE_READY, "Make sure to set BUILD_SITE_FOR_WEB to true before uploading to server!"));

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
        templatePlaceSearchRandomSite = configuration.getOrDefault("templatePlaceSearchRandomSite", "BUILDER-PLACE-SEARCH-RANDOM-SITE");
        templatePlaceScript = configuration.getOrDefault("templatePlaceScript", "BUILDER-PLACE-SCRIPT");
        templatePlaceClickCounter = configuration.getOrDefault("templatePlaceClickCounter", "BUILDER-PLACE-CLICK-COUNTER");
        templatePlaceSidebar = configuration.getOrDefault("templatePlaceSidebar", "BUILDER-PLACE-SIDEBAR");
        templatePlaceAmountPages = configuration.getOrDefault("templatePlaceAmountPages", "BUILDER-PLACE-AMOUNT-PAGES");
        templateInsert = configuration.getOrDefault("templateInsert", "INSERT");
        templateInsert1 = configuration.getOrDefault("templateInsert1", "INSERT-1");
        templateInsert2 = configuration.getOrDefault("templateInsert2", "INSERT-2");
        templateSubTitle = configuration.getOrDefault("templateSubTitle", "<h1>INSERT</h1>");
        templateMainTitle = configuration.getOrDefault("templateMainTitle", "<h1>INSERT</h1>");
        templateTextTitle = configuration.getOrDefault("templateTextTitle", "<h4>INSERT</h4>");
        templateTextParagraphIntro = configuration.getOrDefault("templateTextParagraphIntro", "<p>");
        templateCss = configuration.getOrDefault("templateCss", "<link rel=\"stylesheet\" href=\"INSERT\" media=\"screen\">");
        templateWebsiteTitle = configuration.getOrDefault("templateWebsiteTitle", "<title>INSERT</title>");
        templateIcon = configuration.getOrDefault("templateIcon", "<img src=\"INSERT\" class=\"u-logo-image u-logo-image-1\" data-image-width=\"80.4586\">");

        templateSubTitleDefault = configuration.getOrDefault("templateSubTitleDefault", "Hauptthema");
        templateMainCss = configuration.getOrDefault("templateMainCss", "nicepage.css");
        templateInformationCss = configuration.getOrDefault("templateInformationCss", "info.css");

        siteImageIcon = configuration.getOrDefault("siteImageIcon", "itabiicon.png");
        siteTitleImage = configuration.getOrDefault("siteTitleImage", "227231823-carl-bosch-schule-Lef1.jpg");
        mainPageUrl = configuration.getOrDefault("mainPageUrl", "http://yanwittmann.de");

        informationPageEnding = configuration.getOrDefault("informationPageEnding", ".html");
        if (buildSiteForWeb) {
            informationPageEndingForLink = "";
        } else {
            informationPageEnding = ".html";
            informationPageEndingForLink = informationPageEnding;
        }

        regexLink = configuration.getOrDefault("regexLink", "\\[\\[href=([^|]+)\\|([^]]+)]]");
        regexLinkReplace = configuration.getOrDefault("regexLinkReplace", "<a href=\"$1\">$2</a>");

        experiment();
    }

    private void experiment() {

    }

    public void clearOldSite() {
        FileUtils.deleteDirectory(new File(siteOutDir));
    }

    private ArrayList<String> orderedPages;
    private final PageTreeBuilder pageTreeBuilder = new PageTreeBuilder();

    public void buildSite() throws IOException {
        ArrayList<File> files = FileUtils.getFiles(new File(sitePagesDir));
        for (File file : files) registerPage(file);
        informationPages.forEach(pageTreeBuilder::add);
        pageTreeBuilder.finish();
        orderedPages = pageTreeBuilder.getOrderedPages();

        ArrayList<String> template = FileUtils.readFileToArrayList(new File(siteTemplateDir + "informationtemplate.html"));
        System.out.println("Generating pages...");
        numberOfPages = informationPages.size();
        files.forEach(siteFile -> {
            try {
                buildInformationPage(siteFile, template);
            } catch (IOException e) {
                System.out.println("Unable to build page!");
                e.printStackTrace();
            }
        });
        System.out.println("\n" + informationPages.size() + " pages generated");

        FileUtils.copyFile(new File(siteTemplateDir + "information.css"), new File(siteOutDir + templateInformationCss));
        FileUtils.copyFile(new File(siteTemplateDir + "Hauptseite.css"), new File(siteOutDir + "mainpage.css"));
        FileUtils.copyFile(new File(siteTemplateDir + "nicepage.css"), new File(siteOutDir + templateMainCss));
        FileUtils.copyFile(new File(siteTemplateDir + "information.js"), new File(siteOutDir + "information.js"));
        FileUtils.copyFile(new File(siteImagesDir + siteImageIcon), new File(siteOutDir + "images\\" + siteImageIcon));
        FileUtils.copyFile(new File(siteImagesDir + siteTitleImage), new File(siteOutDir + "images\\" + siteTitleImage));
        FileUtils.copyFile(new File(siteImagesDir + "yangifsmall.gif"), new File(siteOutDir + "images\\yangifsmall.gif"));
        FileUtils.copyFile(new File(siteImagesDir + "arrow-cursor.svg"), new File(siteOutDir + "images\\arrow-cursor.svg"));

        String[] searchJs = FileUtils.readFileToStringArray(new File(siteTemplateDir + "search.js"));
        IntStream.range(0, searchJs.length).filter(i -> searchJs[i].equals(templatePlaceSearchRandomSite)).forEach(i -> searchJs[i] = generateSearchRandomKeywords());
        FileUtils.writeFile(new File(siteOutDir + "search.js"), searchJs);

        buildMainPage();

        if (warnings.size() > 0) {
            System.out.println("---------------- warnings ----------------");
            warnings.stream().sorted().forEach(Warning::print);
            System.out.println("------------- warnings over --------------");
        }
    }

    private void buildMainPage() throws IOException {
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
            } else if (line.contains(templatePlaceSearchRandomSite)) {
                generatedPage.append(line.replace(templatePlaceSearchRandomSite, generateSearchRandomKeywords()));
            } else if (line.contains(templatePlaceSidebar)) {
                generatedPage.append(line.replace(templatePlaceSidebar, generateSidebarMenu("")));
            } else if (line.contains(templatePlaceContents)) {
                generatedPage.append(line.replace(templatePlaceContents, mainList.toString()));
            } else if (line.contains(templatePlaceSearchList)) {
                generatedPage.append(line.replace(templatePlaceSearchList, searchList));
            } else if (line.contains(templatePlaceAmountPages)) {
                generatedPage.append(line.replace(templatePlaceAmountPages, "" + informationPages.size()));
            } else {
                generatedPage.append(line);
            }
        }

        FileUtils.writeFile(new File(siteOutDir + "index.html"), optimizeGeneratedPage(generatedPage.toString()));
    }

    private String generateSearchRandomKeywords() {
        ArrayList<String> searchTerms = new ArrayList<>();
        for (InformationPage page : informationPages) {
            String toAdd = prepareSearchKeyword(page.getDisplayName());
            if (!searchTerms.contains(toAdd) && toAdd.length() > 0)
                searchTerms.add(toAdd);
            for (String keyword : page.getKeywords()) {
                toAdd = prepareSearchKeyword(keyword);
                if (!searchTerms.contains(toAdd) && toAdd.length() > 2)
                    searchTerms.add(toAdd);
            }
        }
        return "\"" + String.join("\", \"", searchTerms) + "\"";
    }

    public static String prepareSearchKeyword(String keyword) {
        return capitalize(keyword.replaceAll("<[^<>]+>", "").replaceAll("[\\d.()\\[\\]!\"§$%&/=?+#*'\\-_:,;]", "")
                .replaceAll("([a-z]{2,10})([A-Z])", "$1 $2").trim().replace("  ", " "));
    }

    public static String capitalize(String str) {
        if (str == null) return null;
        if (str.length() == 0) return str;
        if (str.length() == 1) return str.toUpperCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String generateSearchList() {
        LineBuilder searches = new LineBuilder();
        informationPages.stream().map(InformationPage::generateSearchEntry).forEach(searches::append);
        return searches.toString();
    }

    private Pair<String, String> getSurroundingInfoPages(File page) { // keywords for ctrl+f: neighboring, next to, around
        Pair<String, String> pair = new Pair<>();
        String lookingForPage = page.toString()
                .replace(".txt", informationPageEndingForLink.length() == 0 ? "<" :informationPageEndingForLink).replace(sitePagesDir, "")
                .replaceAll(".+[\\\\/]([^\\\\/]+)", "$1");
        for (int i = 0, orderedPagesSize = orderedPages.size(); i < orderedPagesSize; i++) {
            String orderedPage = orderedPages.get(i);
            System.out.println(orderedPage + " - " + lookingForPage);
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

    private void registerPage(File siteFile) throws IOException {
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
                .mapToObj(i -> "../").collect(Collectors.joining());
        informationPages.add(new InformationPage(pageTitle, siteFile, path.replace(templateSubTitleDefault, "")));
    }

    private String generateSidebarMenu(String pathToMainDirectory) {
        LineBuilder sidebar = new LineBuilder();
        sidebar.append(prepareInformationPageLink("<a href=\"" + pathToMainDirectory + "/index.html\">Hauptseite</a>"));
        sidebar.append("<a href=\"http://yanwittmann.de\">Zu yanwittmann.de</a>");
        sidebar.append("<br style=\"margin: 1em; display: block; font-size: 24%;\">");

        for (String entry : pageTreeBuilder.getSidebarMenu()) {
            entry = SiteBuilder.prepareInformationPageLink(entry);
            if (entry.startsWith(PageTreeBuilder.SIDEBAR_DROPDOWN)) {
                sidebar.append("<button class=\"dropdown-btn\">" + entry.replace(PageTreeBuilder.SIDEBAR_DROPDOWN, ""));
                sidebar.append("<i class=\"fa fa-caret-down\"></i>");
                sidebar.append("</button>");
                sidebar.append("<div class=\"dropdown-container\">");
            } else if (entry.startsWith(PageTreeBuilder.SIDEBAR_ENTRY)) {
                sidebar.append(entry.replace(PageTreeBuilder.SIDEBAR_ENTRY, "").replace("href=\"", "href=\"" + pathToMainDirectory));
            } else if (entry.equals(PageTreeBuilder.SIDEBAR_DROPDOWN_END)) {
                sidebar.append("</div>");
            }
        }

        return sidebar.toString();
    }

    private final ArrayList<Warning> warnings = new ArrayList<>();
    private final ArrayList<InformationPage> informationPages = new ArrayList<>();
    private String pathToMainDirectory;

    private void buildInformationPage(File siteFile, ArrayList<String> template) throws IOException {
        String path = prepareInformationPagePath(siteFile.getPath());
        String pageSubtitle = path.replace("\\", " >> ");
        String pageTitle = "Title";
        pathToMainDirectory = IntStream.range(0, GeneralUtils.countOccurrences(path, "\\") + (path.equals(templateSubTitleDefault) ? 0 : 1))
                .mapToObj(i -> "../").collect(Collectors.joining());
        boolean pageErrorWarning = false, pageIncompleteWarning = false;
        String warningMessage = "";
        currentPage = "no page";
        isCurrentlyInTextBlock = false;
        isCurrentlyInCodeBlock = false;
        firstCodeBlockLine = true;
        evenCodeBlockLine = false;
        codeBlockMaxLength = 0;

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
                currentPage = pageSubtitle + "/ " + pageTitle.replaceAll("<[^>]+>", "");
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
                if ((i + 1 < readFileToArrayList.size() && !readFileToArrayList.get(i + 1).startsWith("img ")) ||
                        (i - 1 >= 0 && !readFileToArrayList.get(i - 1).startsWith("-") && !readFileToArrayList.get(i - 1).startsWith("~")))
                    generatedBody.append("<br>");
                generatedBody.append("<img style=\"margin-top:10px;max-width:100%;\" src=\"" + prepareImageLink(line.replace("img ", "")) + "\"/><br>");
            } else if (line.startsWith("-") && !line.startsWith("-->")) { //list
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
                String trimmedLine = line.trim();
                if (trimmedLine.matches("`{1,2}(?:[^`].+)?")) multilineCodeWarning++;
                else multilineCodeWarning = 0;
                if (multilineCodeWarning >= 2 && !isCurrentlyInTextBlock) {
                    warnings.add(new Warning(Warning.WARNING_MULTIPLE_LINES_CODE, pageSubtitle + "/ " + pageTitle.replaceAll("<[^>]+>", "") + ": " + trimmedLine));
                }
                if (isCurrentlyInCodeBlock && trimmedLine.endsWith("<br>")) {
                    warnings.add(new Warning(Warning.WARNING_CODE_BLOCK_BR, pageSubtitle + "/ " + pageTitle.replaceAll("<[^>]+>", "") + ": " + trimmedLine));
                }
                if (isCurrentlyInCodeBlock && trimmedLine.matches(".*(?<!\\\\)[\\[\\]].*")) {
                    warnings.add(new Warning(Warning.WARNING_UNESCAPED_SQUARE_BRACKETS_IN_CODE, pageSubtitle + "/ " + pageTitle.replaceAll("<[^>]+>", "") + ": " + trimmedLine));
                }
                if (!isCurrentlyInCodeBlock && trimmedLine.matches(".*`.*(?<!\\\\)[\\[\\]].*`.*")) {
                    warnings.add(new Warning(Warning.WARNING_UNESCAPED_SQUARE_BRACKETS_IN_CODE, pageSubtitle + "/ " + pageTitle.replaceAll("<[^>]+>", "") + ": " + trimmedLine));
                }
                generatedBody.append(prepareBodyText(line, pathToMainDirectory));
                if (line.contains("</table>") || line.contains("</center>") || line.equals("$$$")) {
                    generatedBody.append("</p>");
                    generatedBody.append(templateTextParagraphIntro);
                }
            } else if (isCurrentlyInCodeBlock) {
                generatedBody.append(prepareBodyText("", pathToMainDirectory));
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
            generatedBody.append(prepareBodyText("Falls du eine Idee hast, wie man dieses Problem beheben könnte, kontaktiere uns bitte über den Link `Hilf mit!` in der grauen Footer-Leiste unten.", pathToMainDirectory));
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
                generatedPage.append(templateMainTitle.replace(templateInsert, beforeNext.getLeft() + " &nbsp;&nbsp;&nbsp; " + pageTitle + " &nbsp;&nbsp;&nbsp; " + beforeNext.getRight()));
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
            } else if (line.contains(templatePlaceScript)) {
                generatedPage.append(line.replace(templatePlaceScript, "<script src=\"" + pathToMainDirectory + "information.js\"></script>"));
            } else if (line.contains(templatePlaceSidebar)) {
                generatedPage.append(line.replace(templatePlaceSidebar, generateSidebarMenu(pathToMainDirectory)));
            } else if (line.contains(templatePlaceClickCounter)) {
                generatedPage.append(line.replace(templatePlaceClickCounter, generateClickCounter(pathToMainDirectory, pageTitle)));
            } else {
                generatedPage.append(line);
            }
        }

        String warningMessageDisplay = warningMessage.length() > 0 ? " --> " + warningMessage : "";
        if (pageErrorWarning)
            warnings.add(new Warning(Warning.WARNING_PAGE_WARNING, pageSubtitle + "/ " + pageTitle.replaceAll("<[^>]+>", "") +
                    warningMessageDisplay));
        if (pageIncompleteWarning)
            warnings.add(new Warning(Warning.WARNING_PAGE_ERROR, pageSubtitle + "/ " + pageTitle.replaceAll("<[^>]+>", "") +
                    warningMessageDisplay));

        currentPageNumber++;
        printProgressBar(currentPageNumber, numberOfPages);

        FileUtils.writeFile(new File(SiteBuilder.prepareInformationPageLink(siteOutDir + path.replace(templateSubTitleDefault, "") + "/" + siteFile.getName().replace(".txt", SiteBuilder.informationPageEnding))), optimizeGeneratedPage(generatedPage.toString()));
    }

    private final ArrayList<String> clickCounters = new ArrayList<>();
    private final static boolean CREATE_COUNTERS = true;

    private String generateClickCounter(String pathToMainDirectory, String pageTitle) {
        if (informationPageEndingForLink.length() > 0) return "";
        String counterKey = pageTitle.replaceAll("<[^>]+>", "").toLowerCase().replaceAll("[^a-z]", "");
        if (counterKey.length() == 0) {
            warnings.add(new Warning(Warning.WARNING_CLICK_COUNTER_TOO_SHORT, pageTitle + ": " + counterKey));
            return "";
        } else if (counterKey.length() == 1) counterKey += "aa";
        else if (counterKey.length() == 2) counterKey += "a";
        else if (counterKey.length() > 25) counterKey = counterKey.substring(0, 25);
        if (clickCounters.contains(counterKey)) {
            warnings.add(new Warning(Warning.WARNING_CLICK_COUNTER_ALREADY_EXISTS, pageTitle + ": " + counterKey));
            return "";
        }
        clickCounters.add(counterKey);

        CountApi countApi = new CountApi("itabisite", counterKey);
        if (CREATE_COUNTERS)
            new Thread(() -> countApi.create(false)).start();

        LineBuilder lineBuilder = new LineBuilder();
        lineBuilder.append("<div class=\"clickCounter\">");
        lineBuilder.append("<span id=\"counterInsert\">");
        lineBuilder.append("<?php");
        lineBuilder.append("$agent = $_SERVER['HTTP_USER_AGENT'];");
        lineBuilder.append("if (strpos($agent, 'Firefox') !== false or strpos($agent, 'Chrome') !== false) {");
        if (CREATE_COUNTERS)
            lineBuilder.append("  $url = \"http://yanwittmann.de/projects/countapi/hit.php?key=" + countApi.getKey() + "&namespace=" + countApi.getNamespace() + "\";");
        else lineBuilder.append("  $url = \"http://yanwittmann.de/projects/countapi/hit.php?key=test&namespace=yan\";");
        lineBuilder.append("  $result = file_get_contents($url);");
        lineBuilder.append("  echo $result;");
        lineBuilder.append("  echo '</span>';");
        lineBuilder.append("  echo '<img width=\"20\" src=\"" + pathToMainDirectory + "images/arrow-cursor.svg\"/>';");
        lineBuilder.append("}");
        lineBuilder.append("?>");
        lineBuilder.append("</div>");
        return lineBuilder.toString();
    }

    private int currentPageNumber = 0;
    private int numberOfPages = 0;

    private String prepareImageLink(String link) throws IOException {
        if (link.contains("http"))
            return link;
        File image = new File(siteImagesDir + link);
        if (image.exists()) {
            File destination = new File(siteOutDir + "/images/" + link);
            FileUtils.makeDirectories(destination.getPath().replace(image.getName(), ""));
            FileUtils.copyFile(image, destination);
        } else warnings.add(new Warning(Warning.WARNING_IMAGE_NOT_FOUND, ": " + image.getPath()));
        return (pathToMainDirectory + "/images/" + link).replaceAll("[\\\\/]{2}", "/").replaceAll("[\\\\/]{2}", "/");
    }

    private boolean isCurrentlyInTextBlock = false;
    private boolean isCurrentlyInCodeBlock = false;
    private boolean firstCodeBlockLine = true;
    private boolean evenCodeBlockLine = false;
    private int multilineCodeWarning = 0;
    private int codeBlockMaxLength = 0;
    private String currentPage = "";
    private String currentCodeBlockID = "";
    private String currentCodeBlockLanguage = "";

    private String prepareBodyText(String text, String pathToMainDirectory) {
        text = text.replace("\\[", "ESCAPEDSQUAREBRACKETSOPEN").replace("\\]", "ESCAPEDSQUAREBRACKETSCLOSE")
                .replace("\\^", "ESCAPEDCARROT").replace("\\$", "ESCAPEDDOLLAR").replace("\\>", "ESCAPEDSMALLERTHAN")
                .replace("\\<", "ESCAPEDLARGERTHAN").replace("-->", "\uD83E\uDC1A");
        if (!firstCodeBlockLine && isCurrentlyInCodeBlock && !text.endsWith("<br>") && !text.trim().equals("````") && isCurrentlyInTextBlock)
            text = "<br>" + text;
        if (isCurrentlyInCodeBlock && text.length() == 0) text = "<br>";
        if (isCurrentlyInCodeBlock && firstCodeBlockLine) firstCodeBlockLine = false;
        if (text.matches("\\$\\$\\$ .+")) {
            isCurrentlyInTextBlock = true;
            String spoilerBoxId = "spoiler-" + GeneralUtils.randomNumber(100, 999999);
            String spoilerBoxLabel = text.replace("$$$", "").replaceAll("`([^`]+)`", "<code>$1</code>");
            return "<button class=\"spoilerButton\" title=\"Klick mich!\" type=\"button\" onclick=\"if(document.getElementById('" + spoilerBoxId + "').style.display=='none')" +
                    "{document.getElementById('" + spoilerBoxId + "') .style.display=''}else{document.getElementById('" + spoilerBoxId + "') .style.display='none'}\">" + spoilerBoxLabel + "</button>\n" +
                    "<div class=\"spoilerContents\" id=\"" + spoilerBoxId + "\" style=\"display:none\">" + templateTextParagraphIntro;
        } else if (text.matches("\\$\\$ ?([^$:]+):([^$]+)->([^$]+)")) {
            isCurrentlyInTextBlock = true;
            String spoilerBoxId = "spoiler-" + GeneralUtils.randomNumber(100, 999999);
            String spoilerBoxTitle = text.replaceAll("\\$\\$ ?([^$:]+):([^$]+)->([^$]+)", "$1").replaceAll("`([^`]+)`", "<code>$1</code>");
            String spoilerBoxLabel = text.replaceAll("\\$\\$ ?([^$:]+):([^$]+)->([^$]+)", "$2").replaceAll("`([^`]+)`", "<code>$1</code>");
            String spoilerBoxHoverText = text.replaceAll("\\$\\$ ?([^$:]+):([^$]+)->([^$]+)", "$3");
            return "<span class=\"boxTextTitle\">&nbsp;&nbsp;&nbsp;&nbsp;" + spoilerBoxTitle + "</span><br>" +
                    "<span class=\"boxTextSpoiler\" title=\"" + spoilerBoxHoverText + "\" onclick=\"if(document.getElementById('" + spoilerBoxId + "').style.display=='none')" +
                    "{document.getElementById('" + spoilerBoxId + "') .style.display=''}else{document.getElementById('" + spoilerBoxId + "') .style.display='none'}\">" +
                    spoilerBoxLabel + "</span>" +
                    "<div class=\"spoilerContents\" id=\"" + spoilerBoxId + "\" style=\"display:none\">" + templateTextParagraphIntro;
        } else if (text.matches("\\$\\$ ?([^$]+)->([^$]+)")) {
            isCurrentlyInTextBlock = true;
            String spoilerBoxId = "spoiler-" + GeneralUtils.randomNumber(100, 999999);
            String spoilerBoxLabel = text.replaceAll("\\$\\$ ([^$]+)->([^$]+)", "$1").replaceAll("`([^`]+)`", "<code>$1</code>");
            String spoilerBoxHoverText = text.replaceAll("\\$\\$ ([^$]+)->([^$]+)", "$2");
            return "<span class=\"boxTextSpoiler\" title=\"" + spoilerBoxHoverText + "\" onclick=\"if(document.getElementById('" + spoilerBoxId + "').style.display=='none')" +
                    "{document.getElementById('" + spoilerBoxId + "') .style.display=''}else{document.getElementById('" + spoilerBoxId + "') .style.display='none'}\">" +
                    spoilerBoxLabel + "</span>" +
                    "<div class=\"spoilerContents\" id=\"" + spoilerBoxId + "\" style=\"display:none\">" + templateTextParagraphIntro;
        } else if (text.equals("$$$")) {
            isCurrentlyInTextBlock = false;
            return "</p></div>";
        } else if (text.equals("$$$$") && !isCurrentlyInTextBlock) {
            isCurrentlyInTextBlock = true;
            return "<div class=\"spoilerContents\">";
        } else if (text.equals("$$$$")) {
            isCurrentlyInTextBlock = false;
            return "";
            //return "</p></div>";
        }
        if (text.trim().startsWith("````")) {
            isCurrentlyInCodeBlock = !isCurrentlyInCodeBlock;
            codeBlockMaxLength = 0;
            if (isCurrentlyInCodeBlock) {
                currentCodeBlockID = "code_block_" + GeneralUtils.randomNumber(100, 99999);
                if (text.matches(".*````.+")) {
                    currentCodeBlockLanguage = text.replaceAll(".*````(.+)", "$1");
                    text = "````";
                } else {
                    currentCodeBlockLanguage = "";
                    warnings.add(new Warning(Warning.WARNING_CODE_BLOCK_NO_LANGUAGE, currentPage));
                }
            }
            firstCodeBlockLine = true;
            evenCodeBlockLine = false;
            if (isCurrentlyInTextBlock)
                return "";
        }

        if (isCurrentlyInCodeBlock && isCurrentlyInTextBlock) text = "`" + text + "`";
        int actualLength = text.replace("ESCAPEDSQUAREBRACKETSOPEN", " ").replace("ESCAPEDSQUAREBRACKETSCLOSE", " ").length();
        if (isCurrentlyInCodeBlock && !isCurrentlyInTextBlock && !text.contains("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") && actualLength > 1 &&
                codeBlockMaxLength < actualLength && !text.equals("````")) {
            codeBlockMaxLength = actualLength;
            text += "<span id=\"nocopy\">&nbsp;" + "&nbsp;".repeat(currentCodeBlockLanguage.length()) + "</span>";
        }
        text = text.replaceAll(regexLink, regexLinkReplace).replace("  ", "&nbsp;&nbsp;")
                .replace("<<", "&lt&lt").replace(">>", "&gt&gt")
                .replaceAll("\\^([^ <>]+)", "<sup>$1</sup>")
                .replaceAll("\\$\\$([^$]+)\\$\\$", "<span class=\"spoiler\">$1</span>")
                .replaceAll("\\$([^$]+)\\$", "<span class=\"spoiler2\">$1</span>")
                .replaceAll("\\$\\$ ?([^$:]+):([^$]+)", "<span class=\"boxTextTitle\">&nbsp;&nbsp;&nbsp;&nbsp;$1</span><br><span class=\"boxText\">$2</span><br><br>")
                .replaceAll("\\$\\$ ?([^$]+)", "<span class=\"boxText\">$1</span><br><br>")
                .replaceAll("````", !isCurrentlyInTextBlock ? (
                        !isCurrentlyInCodeBlock ? "</div><button id=\"" + currentCodeBlockID + "_button\" class=\"copyClipboardButton\" onclick=\"copyToClipboard('" + currentCodeBlockID + "', '" + currentCodeBlockID + "_button', '" + currentCodeBlockID + "_language')\">\uD83D\uDCCB</button>" +
                                "<div id=\"" + currentCodeBlockID + "_language\" class=\"multilineCodeTitle\" >" + currentCodeBlockLanguage + "</div></div>" +
                                templateTextParagraphIntro : "</p><div class=\"multilineCode\">" + "<div id=\"" + currentCodeBlockID + "\">") : "")
                .replace("``", "`&nbsp`").replaceAll("^` ", "`&nbsp")
                .replaceAll("`([^`]+)`", "<code>$1</code>");
        if (text.matches(".*\\[([^]]+)].*")) {
            Pattern pattern = Pattern.compile("\\[([^]]+)]");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String found = matcher.group();
                text = text.replace(found, getMostLikelyLink(found.replace("[", "").replace("]", ""), pathToMainDirectory));
            }
        }
        if (isCurrentlyInCodeBlock) evenCodeBlockLine = !evenCodeBlockLine;
        if (!isCurrentlyInTextBlock && isCurrentlyInCodeBlock && !firstCodeBlockLine && evenCodeBlockLine) {
            text = "<div class=\"multilineCodeEven\">" + text + "</div>";
        }
        if (isCurrentlyInTextBlock && text.startsWith("<code><br>"))
            text = "<br><code>" + text.replace("<code><br>", "");
        return text.replace("ESCAPEDSQUAREBRACKETSOPEN", "[").replace("ESCAPEDSQUAREBRACKETSCLOSE", "]")
                .replace("ESCAPEDCARROT", "^").replace("ESCAPEDDOLLAR", "$").replace("ESCAPEDSMALLERTHAN", ">")
                .replace("ESCAPEDLARGERTHAN", "<");
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
                    return "<a class=\"link\" href=\"" + pathToMainDirectory + informationPage.getPath().replace("\\", "/") + "/" +
                            informationPage.getFile().getName().replace(".txt", informationPageEndingForLink) + section + "\">" + splitted[0] + "</a>";
                }
        return splitted[0];
    }

    private String prepareInformationPagePath(String path) {
        path = path.replaceAll(".*" + sitePagesDir.replace("\\", "\\\\") + "(.+)", "$1");
        return path.contains("\\") ? path.replaceAll("(.+)\\\\.+", "$1") : templateSubTitleDefault;
    }

    static String prepareInformationPageLink(String name) {
        return name.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue")
                .replaceAll("[\\\\/]{2}", "/");
    }

    private String optimizeGeneratedPage(String generated) {
        return generated.replaceAll("\n<br>", "<br>");
    }

    private void printProgressBar(int currentValue, int maxValue) {
        System.out.printf("\r%s", generateProgressBar(currentValue, maxValue));
    }

    public static String generateProgressBar(int currentValue, int maxValue) {
        int progressBarLength = 33;
        int currentProgressBarIndex = (int) Math.ceil(((double) progressBarLength / maxValue) * currentValue);
        String formattedPercent = String.format(" %5.1f %% ", (100 * currentProgressBarIndex) / (double) progressBarLength);
        int percentStartIndex = ((progressBarLength - formattedPercent.length()) / 2);

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int progressBarIndex = 0; progressBarIndex < progressBarLength; progressBarIndex++) {
            if (progressBarIndex <= percentStartIndex - 1
                    || progressBarIndex >= percentStartIndex + formattedPercent.length()) {
                sb.append(currentProgressBarIndex <= progressBarIndex ? " " : "=");
            } else if (progressBarIndex == percentStartIndex) {
                sb.append(formattedPercent);
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
