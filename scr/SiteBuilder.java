import yanwittmann.Configuration;
import yanwittmann.FileUtils;
import yanwittmann.LineBuilder;

import java.io.File;
import java.util.ArrayList;

public class SiteBuilder {

    private final String siteOutDir;
    private final String sitePagesDir;
    private final String siteTemplateDir;
    private final String templatePlaceTitle;
    private final String templatePlaceBody;
    private final String templateInsert;
    private final String templateMainTitle;
    private final String templateSubTitle;
    private final String templateTextTitle;
    private final String templateTextParagraphIntro;
    private final String templateSubTitleDefault;

    public SiteBuilder(Configuration configuration) {
        siteOutDir = configuration.getOrDefault("siteOutDir", "out\\site\\");
        sitePagesDir = configuration.getOrDefault("sitePagesDir", "res\\site\\pages\\");
        siteTemplateDir = configuration.getOrDefault("siteTemplateDir", "res\\site\\templates\\");

        templatePlaceTitle = configuration.getOrDefault("templatePlaceTitle", "BUILDER-PLACE-TITLE");
        templatePlaceBody = configuration.getOrDefault("templatePlaceBody", "BUILDER-PLACE-BODY");
        templateInsert = configuration.getOrDefault("templateInsert", "INSERT");
        templateMainTitle = configuration.getOrDefault("templateMainTitle", "<h1>INSERT</h1>");
        templateSubTitle = configuration.getOrDefault("templateSubTitle", "<h1>INSERT</h1>");
        templateTextTitle = configuration.getOrDefault("templateTextTitle", "<h4>INSERT</h4>");
        templateTextParagraphIntro = configuration.getOrDefault("templateTextParagraphIntro", "<p>");

        templateSubTitleDefault = configuration.getOrDefault("templateSubTitleDefault", "Hauptthema");
    }

    public void clearOldSite() {
        FileUtils.deleteFilesInDirectory(new File(siteOutDir));
    }

    public void buildSite() {
        ArrayList<String> template = FileUtils.readFileToArrayList(new File(siteTemplateDir + "informationtemplate.html"));
        FileUtils.getFiles(new File(sitePagesDir)).forEach(siteFile -> buildInformationPage(siteFile, template));
        FileUtils.copyFile(new File(siteTemplateDir + "information.css"), new File(siteOutDir + "information.css"));
        FileUtils.copyFile(new File(siteTemplateDir + "nicepage.css"), new File(siteOutDir + "nicepage.css"));
    }

    private void buildInformationPage(File siteFile, ArrayList<String> template) {
        String sitePath = prepareInformationPagePath(siteFile.getPath());
        String pageTitle = "Title";

        System.out.println("Generating " + sitePath);

        LineBuilder generatedBody = new LineBuilder();
        boolean isCurrentlyTextOrImage = false;
        for (String line : FileUtils.readFileToArrayList(siteFile)) {
            if (line.startsWith("# ")) {
                pageTitle = line.replace("# ", "");
            } else if (line.startsWith("## ")) {
                if(isCurrentlyTextOrImage) {
                    isCurrentlyTextOrImage = false;
                    generatedBody.append("</p>");
                }
                generatedBody.append(templateTextTitle.replace(templateInsert, line.replace("## ", "")));
            } else if (line.startsWith("img ")) {
                if(!isCurrentlyTextOrImage) {
                    isCurrentlyTextOrImage = true;
                    generatedBody.append(templateTextParagraphIntro);
                }
                generatedBody.append("<br>").append("<img style=\"margin-top:10px;\" src=\"" + line.replace("img ", "") + "\"/><br>");
            } else if (line.length() > 0) {
                if(!isCurrentlyTextOrImage) {
                    isCurrentlyTextOrImage = true;
                    generatedBody.append(templateTextParagraphIntro);
                }
                generatedBody.append(line);
            }
        }

        LineBuilder generatedPage = new LineBuilder();
        for (String line : template) {
            if (line.trim().equals(templatePlaceTitle)) {
                generatedPage.append(templateSubTitle.replace(templateInsert, pageTitle));
                generatedPage.append(templateMainTitle.replace(templateInsert, sitePath));
            } else if (line.trim().equals(templatePlaceBody)) {
                generatedPage.append(generatedBody.toString());
            } else {
                generatedPage.append(line);
            }
        }

        FileUtils.writeFile(new File(siteOutDir + siteFile.getName().replace(".txt", ".html")), optimizeGenerated(generatedPage.toString()));
    }

    private String prepareInformationPagePath(String path) {
        path = path.replaceAll(".*" + sitePagesDir.replace("\\", "\\\\") + "(.+)", "$1");
        return path.contains("\\") ? path.replaceAll("(.+)\\\\.+", "$1") : templateSubTitleDefault;
    }

    private String optimizeGenerated(String generated) {
        return generated.replaceAll("\n<br>", "<br>");
    }
}
