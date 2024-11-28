package de.adesso.ide.intellij.plugin.adrtoolsplugin;

class AdrCreationData {

    private final boolean ok;
    private final String title;
    private final String supersedes;
    private final String links;
    private String filename;

    public AdrCreationData(boolean ok, String title, String supersedes, String links) {
        this.ok = ok;
        this.title = title;
        this.supersedes = supersedes;
        this.links = links;
    }

    public boolean ok() {
        return ok;
    }
    public String title() {
        return title;
    }
    public String supersedes() {
        return supersedes;
    }
    public String links() {
        return links;
    }
    public String filename() {
        return filename;
    }

    public void filename(String filename) {
        this.filename = filename;
    }
}
