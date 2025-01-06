package de.adesso.ide.intellij.plugin.adrtoolsplugin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record AdrCreationData(
    boolean ok,
    boolean proposed,
    String title,
    List<String> supersedes,
    List<String> links,
    String context,
    String decision,
    String consequences
) {
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        putNotNull(map, "STATUS", proposed ? ADRState.PROPOSED.name() : ADRState.ACCEPTED.name());
        putNotNull(map, "DATE", DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()));
        putNotNull(map, "TITLE", title);
        putNotNull(map, "SUPERSEDES", new HtmlList("Supersedes:", supersedes));
        putNotNull(map, "LINKS", new HtmlList("Links:", links));
        putNotNull(map, "CONTEXT", context);
        putNotNull(map, "DECISION", decision);
        putNotNull(map, "CONSEQUENCES", consequences);
        return map;
    }

    private void putNotNull(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
