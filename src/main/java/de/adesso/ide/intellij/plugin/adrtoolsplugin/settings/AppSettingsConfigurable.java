package de.adesso.ide.intellij.plugin.adrtoolsplugin.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public final class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "ADR Tools: Application Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        return !mySettingsComponent.getAdrDirectory().equals(state.adrDirectory) ||
                mySettingsComponent.getUseCustomTemplate() != state.useCustomTemplate ||
                mySettingsComponent.getCustomTemplate() != state.customTemplate;
    }

    @Override
    public void apply() {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        state.adrDirectory = mySettingsComponent.getAdrDirectory();
        state.useCustomTemplate = mySettingsComponent.getUseCustomTemplate();
        state.customTemplate = mySettingsComponent.getCustomTemplate();
    }

    @Override
    public void reset() {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        mySettingsComponent.setAdrDirectory(state.adrDirectory);
        mySettingsComponent.setUseCustomTemplate(state.useCustomTemplate);
        mySettingsComponent.setCustomTemplate(state.customTemplate);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
