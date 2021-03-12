import yanwittmann.Configuration;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration(new File("res/buildconfig.txt"));
        SiteBuilder siteBuilder = new SiteBuilder(configuration);
        siteBuilder.clearOldSite();
        siteBuilder.buildSite();
    }
}
