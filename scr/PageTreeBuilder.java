import org.json.JSONArray;
import org.json.JSONObject;
import yanwittmann.Pair;

import java.util.ArrayList;

public class PageTreeBuilder {

    private final JSONObject tree = new JSONObject();

    public void add(InformationPage page) {
        makeBranch(page);
        addInformationPage(page);
    }

    private void addInformationPage(InformationPage page) {
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

    public HTMLListBuilder finish() {
        HTMLListBuilder listBuilder = new HTMLListBuilder("", "class=\"u-text u-text-2\"");
        tree.keySet().stream().map(key -> getList(tree.getJSONObject(key), key)).forEach(listBuilder::add);
        return listBuilder;
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
            listBuilder.add(leaf);
        }
        sortBranches(branches);
        for (int i = branches.size() - 1; i >= 0; i--) {
            Pair<JSONObject, String> currentBranch = branches.get(i);
            listBuilder.add(getList(currentBranch.getLeft(), currentBranch.getRight()));
        }
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
        String k;
        for (int i = 0; i < leaves.size() - 1; i++) {
            if (leaves.get(i).replaceAll(".+>(.+)</a>", "$1").compareTo(leaves.get(i + 1).replaceAll(".+>(.+)</a>", "$1")) > 0)
                continue;
            k = leaves.get(i);
            leaves.set(i, leaves.get(i + 1));
            leaves.set(i + 1, k);
            sortLeaves(leaves);
        }
    }
}
