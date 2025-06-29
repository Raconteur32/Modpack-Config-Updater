# SBCOU (Simple but Complete Options Updater)

**Warning**: This showcase is structured to cater to different levels of technical understanding. The initial sections are designed for a general audience, providing an overview of the mod's purpose. As you progress, the content becomes more technical, targeting modpack developers and those seeking in-depth information.

## What does the mod do?

SBCOU is a utility mod for modpacks, offering tools to simplify their development. It introduces a custom format for defining default configuration settings, even when configuration files already exist in the user's setup. Additionally, it allows for selective option overrides within files.

## What does that mean exactly?

Modpack creators typically include configuration files to customize the game and mods, enhancing the user experience. However, distributing these files directly can reset user configurations with each modpack update. To address this, modpack makers often employ mods like YOSBR (Your Options Should Be Respected) for Fabric, which apply default options from a separate directory before the game launch.

While YOSBR and similar mods are excellent for simple modpacks, they operate on basic principles, such as creating files only if they don't exist. This approach has limitations:

1. It doesn't allow adding default options to existing user configuration files.
2. It lacks the ability to selectively override specific options.

These limitations can cause issues, especially when critical options need updating to prevent crashes in new modpack versions. Traditional methods might force overwriting entire configuration files, potentially erasing users' custom settings. Moreover, the way Minecraft modpack updates are typically handled makes it impossible to ensure that users won't skip a version update, potentially missing crucial overrides and experiencing crashes and other problems as a result.

SBCOU addresses these challenges, providing a more flexible and robust solution for managing modpack configurations.

## How does it solve the problems?

1. **Versatile Format Support**: SBCOU can handle most common configuration file formats (JSON, properties, YAML, XML, etc.). It parses these files, breaking them down into individual options for precise comparison and modification.

2. **Granular Control**: By working with individual options, SBCOU can compare user settings with default configurations and selectively update files, preserving user customizations while applying necessary changes.

3. **Version-Specific Contexts**: Instead of a single default options directory, SBCOU uses version-specific contexts. Each context contains default values and overrides for that particular version. This approach ensures that configuration changes are applied sequentially, even if users skip intermediate updates.

## Modpack Developer Tools

SBCOU provides a GUI for modpack developers, streamlining the process of managing default values and overrides without manual copy-pasting. Screenshots of the tool (in early development) will be included here:

[Screenshots to be added later]

## Development Status

Most of the described features are already functional. The mod is currently in a polishing phase, with potential for additional features before the initial release. Future enhancements may include tools like a configuration changelog generator to further assist modpack developers.

With that in mind, I think I should be able to publish an alpha in the coming days / week.
