package de.adesso.ide.intellij.plugin.adrtoolsplugin.ui;

import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;

import java.util.List;

public class ScrollableListPane<T> extends JBScrollPane {

    JBList<T> list;

    public ScrollableListPane(List<T> items) {
        super();
        list = new JBList<>(items);
        setViewportView(list);
    }

    public T getSelectedItem() {
        return list.getSelectedValue();
    }

    public List<T> getSelectedItems() {
        return list.getSelectedValuesList();
    }
}
