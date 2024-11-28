package de.adesso.ide.intellij.plugin.adrtoolsplugin.settings;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppSettingsComponent {

    private final JPanel myMainPanel;
    private final JBTextField myAdrDirectory = new JBTextField();
    private final JBCheckBox myUseCustomTemplate = new JBCheckBox();
    private final JBTextArea myCustomTemplate = new JBTextArea();

    public AppSettingsComponent() {
        myCustomTemplate.setLineWrap(true);
        myCustomTemplate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(JBColor.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        myUseCustomTemplate.addActionListener(e -> myCustomTemplate.setEnabled(myUseCustomTemplate.isSelected()));
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("ADR directory:"), myAdrDirectory, 20, false)
                .addLabeledComponent(new JBLabel("Use custom template:"), myUseCustomTemplate, 20, false)
                .addComponent(new JBLabel("Custom template will be applied at initialization time only.", UIUtil.ComponentStyle.SMALL, UIUtil.FontColor.BRIGHTER),0)
                .addSeparator(20)
                .addComponent(myCustomTemplate, 20)
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

    public boolean getUseCustomTemplate() {
        return myUseCustomTemplate.isSelected();
    }

    public void setUseCustomTemplate(boolean newStatus) {
        myUseCustomTemplate.setSelected(newStatus);
        myCustomTemplate.setEnabled(newStatus);
    }

    @NotNull
    public String getCustomTemplate() {
        return myCustomTemplate.getText();
    }

    public void setCustomTemplate(@NotNull String newText) {
        myCustomTemplate.setText(newText);
    }
}