package de.adesso.ide.intellij.plugin.adrtoolsplugin.template;

import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagReplacementHandler extends ContentCreatorHandler {

    private final Map<String, Object> replacements = new HashMap<>();
    private int replacementDepth = 0;

    public TagReplacementHandler(List<String> tagsToOmit, Map<String, Object> replacements) {
        super(tagsToOmit);
        this.replacements.putAll(replacements);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        super.startElement(uri, localName, qName, attributes);
        if (replacements.containsKey(qName)) {
            content.append(replacements.get(qName));
            replacementDepth++;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        super.endElement(uri, localName, qName);
        if (replacements.containsKey(qName)) {
            replacementDepth--;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (replacementDepth == 0) {
            super.characters(ch, start, length);
        }
    }
}
