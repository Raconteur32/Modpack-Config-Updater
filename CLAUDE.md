# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SBCOU (Simple But Complete Config Updater) is a Minecraft mod that provides a complete solution to update configurations and other text files in a modpack. The project uses a MultiLoader template architecture, allowing it to compile for multiple Minecraft mod loaders (Fabric, Forge, NeoForge) from a shared common codebase.

## Build System

This project uses Gradle with a multi-module setup:

### Common Commands
- `./gradlew build` - Build all modules
- `./gradlew runClient` - Run the Fabric client (from fabric/ directory)  
- `./gradlew runServer` - Run the Fabric server (from fabric/ directory)

### Project Structure
- `common/` - Shared code compatible with all mod loaders (vanilla + libraries only)
- `fabric/` - Fabric-specific implementation and bootstrap code
- `neoforge/` - NeoForge implementation (currently commented out in settings.gradle)
- `forge/` - Forge implementation (currently commented out in settings.gradle)

Currently only Fabric is enabled in the build configuration.

## Architecture

### Core Components

**Data Type System** (`fr.raconteur.sbcou.types`):
- Abstract base class `SbcouData<T>` for all data types
- Primitive types: `SbcouInteger`, `SbcouReal`, `SbcouBoolean`, `SbcouString`, `SbcouNull`, `SbcouNaN`, `SbcouInfinity`
- Nested types: `SbcouList`, `SbcouObject`
- All data is persisted to SQLite database with automatic deduplication

**Database Layer** (`fr.raconteur.sbcou.db.versions`):
- `SbcouDataBase` - Main database connection and initialization
- `DbDataValues` - Core data value storage and retrieval
- `DbExtensionMimic`, `DbForcedEncoding`, `DbForcedExtension` - Configuration tables
- Uses SQLite with database file at `config/sbcou.db`

**File Handling** (`fr.raconteur.sbcou.file`):
- `AbstractConfigFileHandler` - Base class for file processors
- `JsonConfigFileHandler` - JSON file processing implementation
- Automatic encoding detection using ICU4J library

**Flat Object System** (`fr.raconteur.sbcou.flatobject`):
- `FlatObject` - Converts nested structures to flat key-value representations
- `FlatKey` - Handles hierarchical key paths with proper escaping
- `FlatObjectDiff` - Computes differences between flat objects
- `FlatPatchPreparation` - Prepares patches for configuration updates

**Platform Abstraction** (`fr.raconteur.sbcou.platform`):
- `Services` - Service loader pattern for platform-specific implementations
- `IPlatformHelper` - Interface for platform-specific operations
- Separate implementations for each mod loader (Fabric, Forge, NeoForge)

### Dependencies

Key external dependencies:
- SQLite JDBC driver (`org.xerial:sqlite-jdbc:3.50.2.0`)
- ICU4J for encoding detection (`com.ibm.icu:icu4j:68.1`)
- Apache Commons IO (`commons-io:commons-io:2.17.0`)
- Mixin framework for Minecraft code injection

## Code Conventions

- Package structure follows `fr.raconteur.sbcou.*` pattern
- All mod loaders bootstrap through `CommonClass.init()`
- Database operations use connection from `SbcouDataBase.getLatestInstance()`
- Constants defined in `Constants.java` (note: missing semicolon on line 18)
- Logging uses SLF4J through `Constants.LOG`

## Configuration

Project properties are defined in `gradle.properties` and expanded through the build system. Currently targeting Minecraft 1.21.7 with Java 21 requirement.