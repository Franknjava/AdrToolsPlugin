package de.adesso.ide.intellij.plugin.adrtoolsplugin;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.FormBuilder;
import de.adesso.ide.intellij.plugin.adrtoolsplugin.settings.AppSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewAdrAction extends AnAction {

    private static final Dimension INPUT_SIZE = new Dimension(300, 30);
    private static final String NONE_ITEM = "None";

    @Override
    public void update(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        event.getPresentation().setEnabledAndVisible(project != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        String projectPath = Objects.requireNonNull(event.getProject()).getBasePath();
        if (!isAdrToolsAvailable(projectPath)) {
            Messages.showErrorDialog(
                    "ADR tools not found. Probably they are not installed. " +
                    "You can find installation instructions <a href=\"https://github.com/npryce/adr-tools\">here</a>.",
                    "ADR Tools Not Available."
            );
            return;
        }
        if (!createCustomTemplate(projectPath)) {
            Messages.showErrorDialog(
                    "The custom template could not be created. Please check the permissions.",
                    "Custom Template Creation Failed."
            );
            return;
        }
        if (!isAdrInitialized(projectPath) && !initializeAdr(projectPath)) {
            Messages.showErrorDialog(
                    "The ADR directory could not be created. Please check the permissions.",
                    "ADR Tools Not Initializable."
            );
            return;
        }
        AdrCreationData data = showAdrCreationDialog(projectPath);
        if (!data.ok()) {
            return;
        }
        if (!createNewAdr(projectPath, data)) {
            Messages.showErrorDialog("The Decision Record could not be created.", "ADR Creation Failed.");
            return;
        }
        if (!openAdrInEditor(event.getProject(), data)) {
            Messages.showErrorDialog("The Decision Record could not be opened.", "ADR Open Failed.");
        }
        LocalFileSystem.getInstance().refresh(true);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }

    private String getAdrDirectory() {
        return Objects.requireNonNull(AppSettings.getInstance().getState()).adrDirectory;
    }

    private boolean isAdrToolsAvailable(String projectPath) {
        ShellUtils.ShellResult result = ShellUtils.callShellCommand(projectPath, "adr", "help");
        return (result != null && result.getExitStatus() == 0);
    }

    private boolean isAdrInitialized(String projectPath) {
        ShellUtils.ShellResult result = ShellUtils.callShellCommand(projectPath, "adr", "list");
        return (result != null && result.getExitStatus() == 0 && result.getOutput().lines().findAny().isPresent());
    }

    private boolean initializeAdr(String projectPath) {
        ShellUtils.ShellResult result = ShellUtils.callShellCommand(projectPath, "adr", "init", getAdrDirectory());
        return (result != null && result.getExitStatus() == 0);
    }

    private boolean createCustomTemplate(String projectPath) {
        AppSettings.State state = AppSettings.getInstance().getState();
        if (state != null && state.useCustomTemplate) {
            String template = state.customTemplate;
            if (template != null && !template.isEmpty()) {
                File templateFile = new File(projectPath + "/" + getAdrDirectory() + "/templates/template.md");
                if (!templateFile.exists()) {
                    templateFile.getParentFile().mkdirs();
                    try (FileWriter writer = new FileWriter(templateFile)) {
                        writer.write(template);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private AdrCreationData showAdrCreationDialog(String projectPath) {
        String[] adrNames = getAdrNames(projectPath);
        JComboBox<String> adrSupersedes = new ComboBox<>(adrNames);
        JComboBox<String> adrLinks = new ComboBox<>(adrNames);
        JTextField adrTitle = new JTextField();
        adrSupersedes.setPreferredSize(INPUT_SIZE);
        adrLinks.setPreferredSize(INPUT_SIZE);
        adrTitle.setPreferredSize(INPUT_SIZE);
        boolean ok = new DialogBuilder()
                .title("New ADR")
                .centerPanel(
                        FormBuilder.createFormBuilder()
                                .addLabeledComponent("Supersed", adrSupersedes)
                                .addLabeledComponent("Link", adrLinks)
                                .addLabeledComponent("Title", adrTitle)
                                .getPanel()
                )
                .showAndGet();
        return new AdrCreationData(
                ok,
                adrTitle.getText(),
                getSelectedNumber(adrSupersedes.getSelectedItem()),
                getSelectedNumber(adrLinks.getSelectedItem()));
    }

    private boolean createNewAdr(String projectPath, AdrCreationData data) {
        List<String> command = new ArrayList<>();
        command.add("adr");
        command.add("new");
        if (!data.supersedes().equals(NONE_ITEM)) {
            command.add("-s");
            command.add(data.supersedes());
        }
        if (!data.links().equals(NONE_ITEM)) {
            command.add("-l");
            command.add(data.links());
        }
        command.add(data.title());
        ShellUtils.ShellResult result = ShellUtils.callShellCommand(projectPath, command.toArray(new String[0]));
        if (result != null && result.getExitStatus() == 0) {
            data.filename(result.getOutput().lines().findFirst().orElse(null));
            return true;
        }
        return false;
    }

    private boolean openAdrInEditor(@NotNull Project project, AdrCreationData data) {
        File adrFile = new File(project.getBasePath() + "/" + data.filename());
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

    private String[] getAdrNames(String projectPath) {
        ShellUtils.ShellResult result = ShellUtils.callShellCommand(projectPath, "adr", "list");
        if (result != null && result.getExitStatus() == 0) {
            List<String> names = new ArrayList<>();
            names.add(NONE_ITEM);
            result.getOutput().lines()
                    .map(line -> line.split("/"))
                    .map(elements -> elements[elements.length - 1])
                    .map(name -> name.substring(0, name.length() - 3))
                    .peek(System.out::println)
                    .forEach(names::add);
            return names.toArray(new String[0]);
        }
        return new String[] {NONE_ITEM};
    }

    private String getSelectedNumber(Object selectedItem) {
        return (selectedItem instanceof String) ? ((String) selectedItem).split("-")[0] : NONE_ITEM;
    }
}
