package de.adesso.ide.intellij.plugin.adrtoolsplugin.template;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class ContentCreatorHandler extends DefaultHandler {

    protected final StringBuilder content = new StringBuilder();
    private final List<String> tagsToOmit = new ArrayList<>();

    protected ContentCreatorHandler(List<String> tagsToOmit) {
        this.tagsToOmit.addAll(tagsToOmit);
    }

    public String getContent() {
        return content.toString();
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (!tagsToOmit.contains(qName)) {
            content.append("<").append(qName).append(">");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (!tagsToOmit.contains(qName)) {
            content.append("</").append(qName).append(">");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        content.append(new String(ch, start, length));
    }
}
