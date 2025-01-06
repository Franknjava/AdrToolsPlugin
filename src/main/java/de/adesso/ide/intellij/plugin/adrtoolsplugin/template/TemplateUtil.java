package de.adesso.ide.intellij.plugin.adrtoolsplugin.template;

import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemplateUtil {

    private static final String TAG_TEMPLATE = "TMPL";

    private TemplateUtil() {
    }

    public static String setPlaceholder(String template, String placeholder, String value) {
        return setPlaceholders(template, Map.of(placeholder, value), List.of());
    }

    public static String setPlaceholders(String template, Map<String, Object> replacements, List<String> omitTags) {
        List<String> tagList = new ArrayList<>(omitTags);
        tagList.add(TAG_TEMPLATE);
        TagReplacementHandler handler = new TagReplacementHandler(tagList, replacements);
        return updateTemplate(template, handler);
    }

    public static String addEntryToListPlaceholders(String template, String tag, String newEntry, String header) {
        TagListUpdateHandler handler = new TagListUpdateHandler(List.of(TAG_TEMPLATE), tag, newEntry, header);
        return updateTemplate(template, handler);
    }

    private static String updateTemplate(String template, ContentCreatorHandler handler) {
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(
                    new InputSource(
                            new StringReader("<" + TAG_TEMPLATE + ">" + template + "</" + TAG_TEMPLATE + ">")
                    ),
                    handler
            );
            return handler.getContent();
        } catch (Exception e) {
            return template;
        }
    }
}
