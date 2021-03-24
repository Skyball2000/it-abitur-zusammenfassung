import yanwittmann.Configuration;
import yanwittmann.GoogleTranslate;

import java.io.File;
import java.io.IOException;

public class Main {

    private final static boolean BUILD_SITE_FOR_WEB = false;

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration(new File("res/buildconfig.txt"));
        SiteBuilder siteBuilder = new SiteBuilder(configuration, BUILD_SITE_FOR_WEB);
        siteBuilder.clearOldSite();
        siteBuilder.buildSite();
    }
}
