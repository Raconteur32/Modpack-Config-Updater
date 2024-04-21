package fr.paulbrancieq.modpackconfigupdater.version;

import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.options.context.OptionContext;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

import java.util.List;

public class VersionChange {
  private final OptionContext modifiedContext;
  private final OptionContext modificationSourceContext;
  private final OptionPath modifiedOptionsPath;
  private final OptionPath modificationSourceOptionsPath;
  private final VersionChangeFunction versionChangeFunction;

  private VersionChange(OptionContext modifiedContext, OptionContext modificationSourceContext,
                        OptionPath modifiedOptionsPath, OptionPath modificationSourceOptionsPath,
                        VersionChangeFunction versionChangeFunction) {
    this.modifiedContext = modifiedContext;
    this.modificationSourceContext = modificationSourceContext;
    this.modifiedOptionsPath = modifiedOptionsPath;
    this.modificationSourceOptionsPath = modificationSourceOptionsPath;
    this.versionChangeFunction = versionChangeFunction;
  }

  public void apply() {
    versionChangeFunction.apply(modifiedContext, modificationSourceContext, modifiedOptionsPath, modificationSourceOptionsPath);
  }

  private static class Builder {
    private OptionContext modifiedContext;
    private OptionContext modificationSourceContext;
    private OptionPath modifiedOptionsPath;
    private OptionPath modificationSourceOptionsPath;
    private VersionChangeFunction versionChangeFunction;

    public Builder setModifiedContext(OptionContext modifiedContext) {
      this.modifiedContext = modifiedContext;
      return this;
    }

    public Builder setModificationSourceContext(OptionContext modificationSourceContext) {
      this.modificationSourceContext = modificationSourceContext;
      return this;
    }

    public Builder setModifiedOptionsPath(OptionPath modifiedOptionsPath) {
      this.modifiedOptionsPath = modifiedOptionsPath;
      return this;
    }

    public Builder setModificationSourceOptionsPath(OptionPath modificationSourceOptionsPath) {
      this.modificationSourceOptionsPath = modificationSourceOptionsPath;
      return this;
    }

    public Builder setVersionChangeFunction(VersionChangeFunction versionChangeFunction) {
      this.versionChangeFunction = versionChangeFunction;
      return this;
    }

    public VersionChange build() {
      if (modifiedContext == null) {
        throw new IllegalStateException("Modified context must be set");
      }
      if (modificationSourceContext == null) {
        modificationSourceContext = modifiedContext;
      }
      if (modifiedOptionsPath == null) {
        throw new IllegalStateException("Modified options path must be set");
      }
      if (modificationSourceOptionsPath == null) {
        modificationSourceOptionsPath = modifiedOptionsPath;
      }
      if (versionChangeFunction == null) {
        throw new IllegalStateException("Version change function must be set");
      }
      return new VersionChange(modifiedContext, modificationSourceContext, modifiedOptionsPath, modificationSourceOptionsPath, versionChangeFunction);
    }
  }

  public static class MergeBuilder extends Builder {
    public MergeBuilder() {
      setVersionChangeFunction((modifiedContext, modificationSourceContext, modifiedOptionsPath, modificationSourceOptionsPath) -> {
        List<Option<?>> modifiedOptions = modifiedContext.getOptionsFromOptionPath(modifiedOptionsPath);
      });
    }
  }

  @FunctionalInterface
  public interface VersionChangeFunction {
    void apply(OptionContext modifiedContext, OptionContext modificationSourceContext, OptionPath modifiedOptionsPath, OptionPath modificationSourceOptionsPath);
  }
}
