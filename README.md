# ADR Tools Support

Supports using ADR commandline tools from within the IDE. Thus, making it easier to work with ADRs.
Note your decisions in a structured way and keep track of them without leaving your flow.

## What are Architecture Decision Records?

Although we all know how important it is to document our decisions, it is often hard to keep track of them.
Michael Nygard introduced the concept of Architecture Decision Records (ADRs) to help with this.
In his <a href="https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions">Blog post</a>
he explains how to document architecture decisions and why it is important.

## What is ADR Tools?

<a href="https://github.com/npryce/adr-tools?tab=readme-ov-file">ADR Tools</a>
is a commandline tool for working with a log of Architecture Decision Records (ADRs).

## How to use the plugin?

At any time you can create a new ADR by pressing <kbd>Shift + Meta + Alt + A</kbd>.
Enter a title for the ADR and the plugin will create a new ADR file for you.
If required, you can also link or supersede existing ADRs.
The new ADR will be opened in the editor and you can start documenting your decision.

<img width="50%" src="https://raw.githubusercontent.com/Franknjava/AdrToolsPlugin/refs/heads/main/src/main/resources/images/new-adr-dialog.png"/>

## How to configure the plugin?

You can configure the plugin in the settings dialog.
There you can specify the path where the ADRs are stored.
In addition you can also provide a custom template to use for ADRs.

<img width="100%" src="https://raw.githubusercontent.com/Franknjava/AdrToolsPlugin/refs/heads/main/src/main/resources/images/settings-dialog.png"/>
