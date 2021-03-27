import yanwittmann.notification.BlurNotification;
import yanwittmann.types.Configuration;

import java.io.File;
import java.io.IOException;

public class Main {

    private final static boolean BUILD_SITE_FOR_WEB = true;

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration(new File("res/buildconfig.txt"));
        SiteBuilder siteBuilder = new SiteBuilder(configuration, BUILD_SITE_FOR_WEB);
        siteBuilder.clearOldSite();
        siteBuilder.buildSite();
        new BlurNotification("Generated site" + (BUILD_SITE_FOR_WEB ? "" : " for offline mode"));
    }
}
