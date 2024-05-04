package fr.paulbrancieq.modpackconfigupdater.remake.option.containers;

import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.option.OrphanException;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPath;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.SimpleOptionPathPart;

public interface OptionContainer {
  Option<?> getOption();

  Option<?> getParentOption() throws OrphanException.CantGetParentOptionFromContainer;

  void makeOrphan();

  SimpleOptionPathPart getPathPart();

  OptionPath getPath() throws OrphanException.CantGetParentOptionFromContainer;
}
