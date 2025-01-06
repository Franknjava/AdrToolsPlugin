package de.adesso.ide.intellij.plugin.adrtoolsplugin;

import java.util.ArrayList;
import java.util.List;

public class HtmlList {

    private String title;
    private List<String> items = new ArrayList<>();

    public HtmlList(String title, List<String> items) {
        this.title = title;
        this.items.addAll(items);
    }

    @Override
    public String toString() {
        StringBuilder bob = new StringBuilder();
        if (!items.isEmpty()) {
            bob.append("\n")
                    .append(title)
                    .append("\n\n");
        }
        for(String item : items) {
            bob.append("* [")
                    .append(item)
                    .append("](")
                    .append(item)
                    .append(")\n");
        }
        return bob.toString();
    }
}
