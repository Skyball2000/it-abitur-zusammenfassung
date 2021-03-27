import yanwittmann.types.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PageList {

    public HashMap<String, ArrayList<String>> pages = new HashMap<>();

    public void add(String path, String name) {
        if (pages.containsKey(path)) {
            pages.get(path).add(name);
        } else {
            ArrayList<String> files = new ArrayList<>();
            files.add(name);
            pages.put(path, files);
        }
    }

    public ArrayList<Pair<String, ArrayList<String>>> getOrdered() {
        ArrayList<Pair<String, ArrayList<String>>> orderedPages = new ArrayList<>();

        String[] sortedPaths = new ArrayList<>(pages.keySet()).toArray(new String[0]);
        Arrays.sort(sortedPaths);

        for (String sortedPath : sortedPaths) {
            int foundIndex = -1;
            for (int i = 0, orderedPagesSize = orderedPages.size(); i < orderedPagesSize; i++) {
                Pair<String, ArrayList<String>> orderedPage = orderedPages.get(i);
                if (orderedPage.getLeft().equals(sortedPath)) {
                    foundIndex = i;
                    break;
                }
            }
            if (foundIndex == -1) {
                orderedPages.add(new Pair<>(sortedPath, new ArrayList<>()));
                foundIndex = orderedPages.size() - 1;
            }
            for (Map.Entry<String, ArrayList<String>> page : pages.entrySet()) {
                if (page.getKey().equals(sortedPath))
                    for (String name : page.getValue()) {
                        orderedPages.get(foundIndex).getRight().add(name);
                    }
            }
        }

        for (Pair<String, ArrayList<String>> orderedPage : orderedPages) {
            String[] pages = orderedPage.getRight().toArray(new String[0]);
            Arrays.sort(pages);
            orderedPage.setRight(new ArrayList<>(Arrays.asList(pages)));
        }

        return orderedPages;
    }
}
