import yanwittmann.notification.BlurNotification;
import yanwittmann.types.Configuration;
import yanwittmann.types.File;

import java.io.IOException;

public class Main {

    private final static boolean BUILD_SITE_FOR_WEB = true;
    private final static File sitemap =  new File("out/site/sitemap.xml");

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration(new File("res/buildconfig.txt"));
        SiteBuilder siteBuilder = new SiteBuilder(configuration, BUILD_SITE_FOR_WEB);
        siteBuilder.clearOldSite();
        siteBuilder.buildSite();
        sitemap.delete();
        sitemap.write(Sitemap.generate());
        new BlurNotification("Generated site" + (BUILD_SITE_FOR_WEB ? "" : " for offline mode"));
    }
}
