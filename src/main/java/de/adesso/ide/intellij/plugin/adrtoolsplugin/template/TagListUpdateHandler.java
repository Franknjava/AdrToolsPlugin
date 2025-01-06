package de.adesso.ide.intellij.plugin.adrtoolsplugin.template;

import org.xml.sax.Attributes;

import java.util.List;

public class TagListUpdateHandler extends ContentCreatorHandler {

    private final String tagName;
    private final String newEntry;
    private final String header;

    boolean isTagList = false;
    boolean doesContentExist = false;

    public TagListUpdateHandler(List<String> tagsToOmit, String tagName, String newEntry, String header) {
        super(tagsToOmit);
        this.tagName = tagName;
        this.newEntry = newEntry;
        this.header = header;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals(tagName)) {
            isTagList = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (isTagList) {
            if (!doesContentExist) {
                content.append("\n\n").append(header).append("\n\n");
            }
            content.append("\n* [").append(newEntry).append("](").append(newEntry).append(")");
        }
        isTagList = false;
        super.endElement(uri, localName, qName);
    }


    @Override
    public void characters(char[] ch, int start, int length) {
        if (isTagList) {
            doesContentExist = true;
        }
        super.characters(ch, start, length);
    }
}
