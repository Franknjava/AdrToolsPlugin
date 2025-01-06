# ADR Support

Supports creating and modifying Nygard style ADRs from within the IDE. Thus, making it easier to work with ADRs.
Note your decisions in a structured way and keep track of them without leaving your flow.

## What are Architecture Decision Records?

Although we all know how important it is to document our decisions, it is often hard 
to keep track of them.
Michael Nygard introduced the concept of Architecture Decision Records (ADRs) 
to help with this.
In his <a href="https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions">Blog post</a>
he explains how to document architecture decisions and why it is important.

## How to use the plugin?

### Creating a new ADR

At any time you can create a new ADR by pressing <kbd>Shift + Meta + Alt + A</kbd>.
Enter a title for the ADR and the plugin will create a new ADR file for you.
If required, you can also link or supersede existing ADRs by selecting them.
The linked and/or superseded ADRs will be updated accordingly.
By default the status of the new ADR will be `ACCEPTED`. If you want to propose
a new decision, you can tik the proposed checkbox and the status will be set 
to `PROPOSED` instead.
The new ADR will be opened in the editor afterwards and you can start documenting 
your decision.

<img width="50%" src="https://raw.githubusercontent.com/Franknjava/AdrToolsPlugin/refs/heads/main/src/main/resources/images/new-adr-dialog.png"/>

### Modifying an existing ADR

There are two ways of modifying ADRs. You can open the Markdown file directly
in the editor and modify it. This works best for the textual content of the ADR
like context, decision and consequences.
For the metadata of the ADR like status, links, supersedes and superseded 
you can use the metadata editor. To open the Metadata editor, press <kbd>TBD</kbd> 
or use the context menu "TBD" in the tree view.

Although the ADR file format is Markdown, you will recognize HTML style tags in the
documents. DO NOT REMOVE THESE! They are used by the plugin to identify the metadata.

## How to configure the plugin?

You can configure the plugin in the settings dialog.
There you can specify the path where the ADRs are stored.
In addition you can also provide a custom template to use for ADRs.

<img width="100%" src="https://raw.githubusercontent.com/Franknjava/AdrToolsPlugin/refs/heads/main/src/main/resources/images/settings-dialog.png"/>

## TODO

TODO Show ADR icon in tree view
TODO Update Plugin screenshots
TODO Update Context menu icon
