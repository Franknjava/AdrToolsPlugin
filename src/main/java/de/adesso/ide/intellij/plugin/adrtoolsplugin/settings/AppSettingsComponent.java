package de.adesso.ide.intellij.plugin.adrtoolsplugin.settings;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppSettingsComponent {

    private final JPanel myMainPanel;
    private final JBTextField myAdrDirectory = new JBTextField();
    private final JBTextArea myTemplate = new JBTextArea();

    public AppSettingsComponent() {
        myTemplate.setLineWrap(true);
        myTemplate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(JBColor.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("ADR directory:"), myAdrDirectory, 20, false)
                .addSeparator(20)
                .addComponent(myTemplate, 20)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return myAdrDirectory;
    }

    @NotNull
    public String getAdrDirectory() {
        return myAdrDirectory.getText();
    }

    public void setAdrDirectory(@NotNull String newText) {
        myAdrDirectory.setText(newText);
    }

    @NotNull
    public String getTemplate() {
        return myTemplate.getText();
    }

    public void setTemplate(@NotNull String newText) {
        myTemplate.setText(newText);
    }
}
