package fr.paulbrancieq.modpackconfigupdater.remake.option.containers;

import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.option.OrphanException;
import fr.paulbrancieq.modpackconfigupdater.remake.path.InFileOptionPath;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.UniqueOptionPathPart;

public interface OptionContainer {
  Option<?> getOption();

  Option<?> getParentOption() throws OrphanException.CantGetParentOptionFromContainer;

  void makeOrphan();

  UniqueOptionPathPart getPathPart();

  InFileOptionPath getPath() throws OrphanException.CantGetParentOptionFromContainer;
}
