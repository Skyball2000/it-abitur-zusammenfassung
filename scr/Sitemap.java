import yanwittmann.types.LineBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class Sitemap {

    private static final ArrayList<String> urls = new ArrayList<>();
    private static final String URL_PREFIX = "http://yanwittmann.de/schule/site/";

    public static void addWithPrefix(String url) {
        url = escapeURLCharacters(URL_PREFIX + url.replace("\\", "/").replace(".txt", ".php"));
        if (urls.contains(url)) return;
        urls.add(url);
    }

    public static void add(String url) {
        url = escapeURLCharacters(url.replace("\\", "/").replace(".txt", ".php"));
        if (urls.contains(url)) return;
        urls.add(url);
    }

    private static String escapeURLCharacters(String url) {
        return url.replace(" ", "%20").replace("!", "%21").replace("&", "%26");
    }

    public static String generate() {
        LineBuilder map = new LineBuilder();
        map.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(new Date());
        urls.forEach(url -> map.append("<url>")
                .append("    <loc>" + url + "</loc>")
                .append("    <lastmod>" + dateString + "</lastmod>")
                .append("    <changefreq>never</changefreq>")
                .append("    <priority>0.5</priority>")
                .append("</url>"));
        map.append("</urlset>");
        return map.toString();
    }
}
