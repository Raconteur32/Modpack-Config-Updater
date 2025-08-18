package fr.raconteur.sbcou.file;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import fr.raconteur.sbcou.Constants;
import fr.raconteur.sbcou.db.versions.DbForcedEncoding;
import fr.raconteur.sbcou.types.SbcouData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public abstract class AbstractConfigFileHandler {
    protected final File configFile;
    private final String basePath;
    private final String relativePath;

    public AbstractConfigFileHandler(String basePath, String relativePath) {
        String filePath = Paths.get(basePath, relativePath).toString();
        this.configFile = new File(filePath);
        this.basePath = basePath;
        this.relativePath = relativePath;
    }

    protected abstract String getDefaultEncoding();

    protected String getEncoding() {
        String encoding;
        Optional<String> detectedEncoding = detectEncoding();
        Optional<String> forcedEncoding = DbForcedEncoding.getForcedEncoding(relativePath);

        if (forcedEncoding.isPresent()) {
            encoding = forcedEncoding.get();
            if (detectedEncoding.isPresent() && !encoding.equals(detectedEncoding.get())) {
                Constants.LOG.warn("Detected encoding ({}) is not the same than the forced encoding ({}) for ({}) {}", detectedEncoding.get(), forcedEncoding.get(), basePath, relativePath);
            }
        } else if (detectedEncoding.isPresent()) {
            encoding = detectedEncoding.get();
        } else {
            encoding = getDefaultEncoding();
            Constants.LOG.warn("Detected encoding ({}) is empty, defaulting to {} for ({}) {}. It is recommended that you verify the encoding of this file and force it for reliability.", relativePath, getDefaultEncoding(), basePath, relativePath);
        }
        return encoding;
    }

    protected Optional<String> detectEncoding() {
        try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(this.configFile))) {
            CharsetDetector charsetDetector = new CharsetDetector();
            charsetDetector.setText(fileInputStream);
            CharsetMatch charsetMatch = charsetDetector.detect();
            return Optional.ofNullable(charsetMatch.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract SbcouData<?> read() throws Exception;

    public abstract void write(SbcouData<?> value) throws IOException;
}
