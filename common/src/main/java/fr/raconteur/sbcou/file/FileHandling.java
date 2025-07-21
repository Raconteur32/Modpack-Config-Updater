package fr.raconteur.sbcou.file;

import fr.raconteur.sbcou.db.DbExtensionMimic;
import fr.raconteur.sbcou.db.DbForcedExtension;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FileHandling {
    public static Map<String, AbstractConfigFileHandler> handlers = new HashMap<>();

    public static void registerConfigFileHandler(String extension, AbstractConfigFileHandler handler) {
        handlers.put(extension, handler);
    }

    private static Optional<AbstractConfigFileHandler> getHandlerFromImplemented(String extension) {
        return Optional.ofNullable(handlers.get(extension));
    }

    @Nullable
    private static AbstractConfigFileHandler getHandler(String filePath) {
        String normalizedPath = Paths.get(filePath).normalize().toString();
        // Get the forced extension for this file if there is one, else get the default extension
        String extension = DbForcedExtension.getForcedExtension(normalizedPath).orElse(FilenameUtils.getExtension(normalizedPath));
        // Get the target extension if the extension should mimic another
        extension = DbExtensionMimic.getMimicTarget(extension).orElse(extension);

        return getHandlerFromImplemented(extension).orElse(null);
    }
}
