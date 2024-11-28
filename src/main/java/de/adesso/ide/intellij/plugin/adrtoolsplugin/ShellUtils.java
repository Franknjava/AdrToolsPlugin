package de.adesso.ide.intellij.plugin.adrtoolsplugin;

import java.io.File;
import java.io.IOException;

public class ShellUtils {

    public static class ShellResult {
        private final int exitStatus;
        private final String output;

        public ShellResult(int exitStatus, String output) {
            this.exitStatus = exitStatus;
            this.output = output;
        }

        public int getExitStatus() {
            return exitStatus;
        }

        public String getOutput() {
            return output;
        }
    }

    private ShellUtils() {
    }

    public static ShellResult callShellCommand(String directory, String... command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(directory));
        try {
            Process p = pb.start();
            int exitStatus = p.waitFor();
            String output = new String(p.getInputStream().readAllBytes());
            return new ShellResult(exitStatus, output);
        }
        catch (InterruptedException | IOException x) {
            return null;
        }
    }
}
