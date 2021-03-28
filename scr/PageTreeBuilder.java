import org.json.JSONArray;
import org.json.JSONObject;
import yanwittmann.types.Pair;
import yanwittmann.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class PageTreeBuilder {

    private final JSONObject tree = new JSONObject();
    private final ArrayList<InformationPage> informationPages = new ArrayList<>();

    public void add(InformationPage page) {
        makeBranch(page);
        addInformationPage(page);
    }

    private void addInformationPage(InformationPage page) {
        if (page.isHidden()) return;
        informationPages.add(page);
        JSONObject currentBranch = tree;
        for (String pathTitle : page.getPathTitles()) {
            if (currentBranch.has(pathTitle)) {
                currentBranch = currentBranch.getJSONObject(pathTitle);
            }
        }
        JSONArray array = currentBranch.getJSONArray("pages");
        JSONObject leaf = new JSONObject();
        leaf.put("pageInfo", page.toString());
        array.put(leaf);
    }

    private void makeBranch(InformationPage page) {
        JSONObject currentBranch = tree;
        for (String pathTitle : page.getPathTitles()) {
            if (currentBranch.has(pathTitle)) {
                currentBranch = currentBranch.getJSONObject(pathTitle);
            } else {
                JSONObject branch = new JSONObject();
                currentBranch.put(pathTitle, branch);
                currentBranch = branch;
            }
        }
        if (!currentBranch.has("pages"))
            currentBranch.put("pages", new JSONArray());
    }

    private HTMLListBuilder lastListBuilder = null;

    public HTMLListBuilder finish() {
        if (lastListBuilder == null) {
            HTMLListBuilder listBuilder = new HTMLListBuilder("", "class=\"u-text u-text-2\"");
            tree.keySet().stream().map(key -> getList(tree.getJSONObject(key), key)).forEach(listBuilder::add);
            lastListBuilder = listBuilder;
        }
        return lastListBuilder;
    }

    private final ArrayList<String> orderedPages = new ArrayList<>();

    /**
     * You need to finish() the PageTreeBuilder first!
     */
    public ArrayList<String> getOrderedPages() {
        return orderedPages;
    }

    public HTMLListBuilder getList(JSONObject branch, String oldKey) {
        HTMLListBuilder listBuilder = new HTMLListBuilder(oldKey, "style=\"margin-top:2px;\"");
        ArrayList<Pair<JSONObject, String>> branches = new ArrayList<>();
        ArrayList<String> leaves = new ArrayList<>();
        for (String key : branch.keySet()) {
            if (branch.get(key) instanceof JSONObject)
                branches.add(new Pair<>(branch.getJSONObject(key), key));
            else {
                JSONArray leaf = branch.getJSONArray(key);
                for (int i = 0; i < leaf.length(); i++)
                    leaves.add(leaf.getJSONObject(i).getString("pageInfo"));
            }
        }
        sortLeaves(leaves);
        for (int i = leaves.size() - 1; i >= 0; i--) {
            String leaf = leaves.get(i);
            if (GeneralUtils.countOccurrences(leaf, "\\") == 1)
                leaf = leaf.replaceAll("\"\\\\(.+)", "\"$1");
            listBuilder.add(leaf);
            sidebar.add(SIDEBAR_ENTRY + leaf);
            orderedPages.add(leaf);
        }
        sortBranches(branches);
        for (int i = branches.size() - 1; i >= 0; i--) {
            Pair<JSONObject, String> currentBranch = branches.get(i);
            sidebar.add(SIDEBAR_DROPDOWN + currentBranch.getRight());
            listBuilder.add(getList(currentBranch.getLeft(), currentBranch.getRight()));
        }
        sidebar.add(SIDEBAR_DROPDOWN_END);
        return listBuilder;
    }

    private void sortBranches(ArrayList<Pair<JSONObject, String>> branches) {
        Pair<JSONObject, String> k;
        for (int i = 0; i < branches.size() - 1; i++) {
            if (branches.get(i).getRight().compareTo(branches.get(i + 1).getRight()) > 0)
                continue;
            k = branches.get(i);
            branches.set(i, branches.get(i + 1));
            branches.set(i + 1, k);
            sortBranches(branches);
        }
    }

    private void sortLeaves(ArrayList<String> leaves) {
        Leaf[] sorted = new Leaf[leaves.size()];
        Arrays.setAll(sorted, i -> new Leaf(leaves.get(i)));
        Arrays.sort(sorted);
        leaves.clear();
        for (int i = sorted.length - 1; i >= 0; i--) {
            leaves.add(sorted[i].getLinkText());
        }
    }

    private final ArrayList<String> sidebar = new ArrayList<>();

    public ArrayList<String> getSidebarMenu() {
        return sidebar;
    }

    public final static String SIDEBAR_ENTRY = "-en-";
    public final static String SIDEBAR_DROPDOWN = "-dd-";
    public final static String SIDEBAR_DROPDOWN_END = "-de-";
}
