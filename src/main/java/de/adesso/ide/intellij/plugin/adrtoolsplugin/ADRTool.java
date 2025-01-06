package de.adesso.ide.intellij.plugin.adrtoolsplugin;

import de.adesso.ide.intellij.plugin.adrtoolsplugin.template.TemplateUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ADRTool {

    private static final Logger LOGGER = Logger.getLogger(ADRTool.class.getName());

    private String projectPath;
    private String adrDirectory;
    private String template;

    public ADRTool(
            String projectPath,
            String adrDirectory,
            String template
    ) {
        this.projectPath = projectPath;
        this.adrDirectory = adrDirectory;
        this.template = template;
    }

    public boolean isADRToolInitialized() {
        return Files.exists(Path.of(projectPath, adrDirectory));
    }

    public boolean initializeADRTool() {
        try {
            Files.createDirectories(Path.of(projectPath, adrDirectory));
            createNewAdr(new AdrCreationData(
                    true,
                    false,
                    "Record Architecture Decisions",
                    null,
                    null,
                    "We need to record the architectural decisions made on this project.",
                    "We will use Architecture Decision Records, as [described by Michael Nygard](http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions).",
                    "See Michael Nygard's article, linked above."
            ));
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize ADR Tool: " + e.getMessage(), e);
            return false;
        }
    }

    public String createNewAdr(AdrCreationData data) {
        int maxId = getAdrFilenames().stream()
                .map(n -> n.substring(0,4))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        String number = String.format("%04d", maxId + 1);
        String filename = sanitizeFilename(number, data.title() ,".md");
        Map<String, Object> replacements = data.asMap();

        updateAdrItems(data.supersedes(), "STATUS", "SUPERSEDED");
        updateAdrLists(data.supersedes(), filename, "SUPERSEDED", "Superseded by:");
        updateAdrLists(data.links(), filename, "LINKS", "Links:");

        String adrText = TemplateUtil.setPlaceholders(template, replacements, List.of("TITLE", "CONTEXT", "DECISION", "CONSEQUENCES"));

        try {
            Files.writeString(Path.of(projectPath, adrDirectory, filename), adrText);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create ADR: " + e.getMessage(), e);
            return null;
        }

        return filename;
    }

    private void updateAdrLists(List<String> adrFilenames, String filename, String newEntry, String header) {
        if (adrFilenames != null) {
            adrFilenames.forEach(adrFilename -> updateAdrList(adrFilename, filename, newEntry, header));
        }
    }

    private void updateAdrList(String adrFilename, String filename, String newEntry, String header) {
        try {
            String adrText = Files.readString(Path.of(projectPath, adrDirectory, adrFilename));
            adrText = TemplateUtil.addEntryToListPlaceholders(adrText, newEntry, filename, header);
            Files.writeString(Path.of(projectPath, adrDirectory, adrFilename), adrText);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to update ADR: " + e.getMessage(), e);
        }
    }

    private void updateAdrItems(List<String> adrFilenames, String placeholder, String value) {
        if (adrFilenames != null) {
            adrFilenames.forEach(adrFilename -> updateAdrItem(adrFilename, placeholder, value));
        }
    }

    private void updateAdrItem(String adrFilename, String placeholder, String value) {
        try {
            String adrText = Files.readString(Path.of(projectPath, adrDirectory, adrFilename));
            adrText = TemplateUtil.setPlaceholder(adrText, placeholder, value);
            Files.writeString(Path.of(projectPath, adrDirectory, adrFilename), adrText);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to update ADR: " + e.getMessage(), e);
        }
    }

    public List<String> getAdrFilenames() {
        try {
            return Files.list(Path.of(projectPath, adrDirectory))
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.matches("^[0-9]{4}_.*\\.md$"))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve ADRs: " + e.getMessage(), e);
            return List.of();
        }
    }

    private  String sanitizeFilename(String number, String title, String extension) {
        String sanitized = title.replaceAll("[^a-zA-Z0-9-_\\.]", "-");
        sanitized = number + "_" + sanitized + extension;

        if (sanitized.length() > 255) {
            sanitized = sanitized.substring(0, 255);
        }

        try {
            Paths.get(sanitized);
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Invalid filename: " + sanitized, e);
        }

        return sanitized;
    }
}
