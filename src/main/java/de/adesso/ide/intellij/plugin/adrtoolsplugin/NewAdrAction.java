package de.adesso.ide.intellij.plugin.adrtoolsplugin;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.FormBuilder;
import de.adesso.ide.intellij.plugin.adrtoolsplugin.settings.AppSettings;
import de.adesso.ide.intellij.plugin.adrtoolsplugin.ui.ScrollableListPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

// TODO Add ADR management actions (Add/Remove Links, Supersedes, etc.)
public class NewAdrAction extends AnAction {

    private static final Dimension INPUT_SIZE = new Dimension(300, 30);
    private static final Dimension LIST_SIZE = new Dimension(300, 90);

    @Override
    public void update(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        event.getPresentation().setEnabledAndVisible(project != null);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = Objects.requireNonNull(event.getProject());
        AppSettings.State appSettings = AppSettings.getInstance().getState();
        ADRTool adrTool = new ADRTool(project.getBasePath(), appSettings.adrDirectory, appSettings.adrTemplate);

        if (!adrTool.isADRToolInitialized() && !adrTool.initializeADRTool()) {
            Messages.showErrorDialog(
                    "The ADR directory could not be created. Please check the permissions.",
                    "ADR Tools Not Initializable."
            );
            return;
        }

        AdrCreationData data = showAdrCreationDialog(adrTool.getAdrFilenames());
        if (!data.ok()) {
            return;
        }

        String adrFilename = adrTool.createNewAdr(data);
        if (adrFilename == null) {
            Messages.showErrorDialog("The Decision Record could not be created.", "ADR Creation Failed.");
            return;
        }

        if (!openAdrInEditor(project, appSettings.adrDirectory, adrFilename)) {
            Messages.showErrorDialog("The Decision Record could not be opened.", "ADR Open Failed.");
        }

        LocalFileSystem.getInstance().refresh(true);
    }

    private AdrCreationData showAdrCreationDialog(List<String> adrNames) {
        ScrollableListPane<String> adrSupersedes = createList(adrNames);
        ScrollableListPane<String> adrLinks = createList(adrNames);
        JCheckBox proposed = new JCheckBox("Proposed");
        JTextField adrTitle = new JTextField();
        adrTitle.setPreferredSize(INPUT_SIZE);
        boolean ok = new DialogBuilder()
                .title("New ADR")
                .centerPanel(
                        FormBuilder.createFormBuilder()
                                .addLabeledComponent("Supersed", adrSupersedes)
                                .addLabeledComponent("Link", adrLinks)
                                .addLabeledComponent("Proposed", proposed)
                                .addLabeledComponent("Title", adrTitle)
                                .getPanel()
                )
                .showAndGet();
        return new AdrCreationData(
                ok,
                proposed.isSelected(),
                adrTitle.getText(),
                adrSupersedes.getSelectedItems(),
                adrLinks.getSelectedItems(),
                "The issue motivating this decision, and any context that influences or constrains the decision.",
                "The change that we're proposing or have agreed to implement.",
                "What becomes easier or more difficult to do and any risks introduced by the change that will need to be mitigated."
        );
    }

    private ScrollableListPane<String> createList(List<String> items) {
        ScrollableListPane<String> pane = new ScrollableListPane<>(items);
        pane.setPreferredSize(LIST_SIZE);
        return pane;
    }

    private boolean openAdrInEditor(@NotNull Project project, String adrDirectory, String adrFilename) {
        File adrFile = Path.of(project.getBasePath(), adrDirectory, adrFilename).toFile();
        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(adrFile);
        if (virtualFile == null) {
            return false;
        }
        FileEditorManager.getInstance(project).openTextEditor(
                new OpenFileDescriptor(project, virtualFile),
                true
        );
        return true;
    }
}
