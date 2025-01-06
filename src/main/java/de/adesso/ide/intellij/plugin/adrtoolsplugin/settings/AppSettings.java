package de.adesso.ide.intellij.plugin.adrtoolsplugin.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


@State(
        name = "de.adesso.ide.intellij.plugin.adrtoolsplugin.settings.AppSettings",
        storages = @Storage("AdrToolsSettingsPlugin.xml")
)
public final class AppSettings implements PersistentStateComponent<AppSettings.State> {

    public static class State {
        @NonNls
        public String adrDirectory = "doc/adr";
        public String adrTemplate = """
                [//]: # (Tags TITLE, DATE, STATUS, SUPERSEDES, SUPERSEDED, LINKS, CONTEXT, DECISION, CONSEQUENCES are not rendered but used as placeholders)
                
                # <TITLE></TITLE>
                
                Date: <DATE></DATE>
                
                ## Status
                
                <STATUS></STATUS>

                <SUPERSEDES></SUPERSEDES>

                <SUPERSEDED></SUPERSEDED>
                
                <LINKS></LINKS>
                
                ## Context
                
                <CONTEXT></CONTEXT>
                
                ## Decision
                
                <DECISION></DECISION>
                
                ## Consequences
                
                <CONSEQUENCES></CONSEQUENCES>
                
                """;
    }

    private State myState = new State();

    public static AppSettings getInstance() {
        return ApplicationManager.getApplication().getService(AppSettings.class);
    }

    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }
}
