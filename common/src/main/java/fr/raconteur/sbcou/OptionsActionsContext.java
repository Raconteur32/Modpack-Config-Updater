package fr.raconteur.sbcou;

import fr.raconteur.sbcou.exceptions.OptionsActionsContextException;
import fr.raconteur.sbcou.exceptions.SbcouException;
import fr.raconteur.sbcou.flatobject.FlatObject;
import fr.raconteur.sbcou.platform.Services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OptionsActionsContext {
  private static ConfigContext mainContext;
  private static FlatObject mainContextFlatObject;
  private static ConfigContext devFullContext;
  private static FlatObject devFullContextFlatObject;
  private static Map<String, OptionChange> optionChangeNatureMap;
  private static Map<String, String> stateMap;
  private static boolean newModpack = false;

  public static void baseRefresh() throws SbcouException {
    Version.reloadListVersions();
    mainContext = OptionsActionsUtils.createMainContextObject();
  }

  public static void fullRefresh() throws SbcouException {
    baseRefresh();
    devFullContext = OptionsActionsUtils.createDevFullContext();
    OptionsActionsUtils.generateFullContextFromVersions(devFullContext);
    devFullContextFlatObject = devFullContext.generateContextFlatObject();
    mainContextFlatObject = mainContext.generateContextFlatObject();
    lightRefresh();
  }

  public static void lightRefresh() throws SbcouException {
    optionChangeNatureMap = devFullContextFlatObject.getChangeTo(mainContextFlatObject);
    stateMap = OptionsActionsUtils.getEditorMemory();
  }

  public static FlatObject getMainContextFlatObject() {
    return mainContextFlatObject;
  }

  public static Map<String, OptionChange> getOptionChangeNatureMap() {
    return optionChangeNatureMap;
  }

  public static Map<String, String> getStateMap() {
    return stateMap;
  }

  public static void setState(String key, OptionHandlingState state) throws
          OptionsActionsContextException {
    if (state == OptionHandlingState.UNKNOWN) {
      stateMap.remove(key);
    } else {
      stateMap.put(key, state.getValue());
    }
    try {
      OptionsActionsUtils.saveEditorMemory(stateMap);
    } catch (IOException e) {
      throw new OptionsActionsContextException("Failed to save editor memory", e);
    }
  }

  public static void verifySbcouConfigDir() throws OptionsActionsContextException {
    File configDir = new File(OptionsActionsUtils.getSbcouConfigDir());
    if (!configDir.exists()) {
      boolean created = configDir.mkdirs();
      if (!created) {
        throw new OptionsActionsContextException("Failed to create sbcou config directory");
      }
      newModpack = true;
    }
  }

  public static boolean isNewModpack() {
    return newModpack;
  }

  public static void createVersion(String versionId) throws SbcouException {
    try {
      if (Version.listVersionsString().contains(versionId)) {
        throw new OptionsActionsContextException("A version with the same id already exists");
      }
      fullRefresh();
      Version newVersion = Version.createVersion(versionId);
      FlatObject newVersionContext = new FlatObject();
      List<String> overrideList = new ArrayList<>();
      List<String> deleteList = new ArrayList<>();
      for (Map.Entry<String, String> entry : stateMap.entrySet()) {
        String key = entry.getKey();
        OptionHandlingState state = OptionHandlingState.valueOf(entry.getValue().toUpperCase());
        OptionChange nature = optionChangeNatureMap.get(key);
        if (nature == OptionChange.DELETED) {
          if (state == OptionHandlingState.OVERRIDE) {
            deleteList.add(key);
          }
        } else {
          if (state == OptionHandlingState.DEFAULT || state == OptionHandlingState.OVERRIDE) {
            newVersionContext.put(key, mainContextFlatObject.get(key));
          }
          if (state == OptionHandlingState.OVERRIDE) {
            overrideList.add(key);
          }
        }
      }
      newVersion.getContext().writeFromContextMap(newVersionContext);
      newVersion.setOverrides(overrideList);
      newVersion.setDeletes(deleteList);
      Version.setCurrent(newVersion.getId());
      fullRefresh();
      OptionsActionsUtils.resetEditorMemory();
    } catch (IOException e) {
      throw new OptionsActionsContextException("Failed to create version", e);
    }
  }

  public static void applyUpdates() throws SbcouException {
    baseRefresh();
    try {
      Version currentVersion = Version.getCurrent();
      List<String> neededUpdates = currentVersion.getNeededUpdates();

      if (neededUpdates.isEmpty()) {
        Services.PLATFORM.logInfo("No updates needed.");
        return;
      }

      Services.PLATFORM.logInfo("Applying updates...");

      for (String updateVersion : neededUpdates) {
        applyUpdate(updateVersion, mainContext);
      }

      String latestAppliedVersion = neededUpdates.getLast();
      Version.setCurrent(latestAppliedVersion);
      Services.PLATFORM.logInfo("All updates applied successfully.");
    } catch (IOException | Version.VersionNotFoundException e) {
      Services.PLATFORM.logError("Error during update process", e);
    }
  }

  private static void applyUpdate(String updateVersionId, ConfigContext mainContext) {
    Services.PLATFORM.logInfo("Applying update for version: " + updateVersionId);
    try {
      Version updateVersionObj = Version.get(updateVersionId);
      mainContext.update(updateVersionObj);
      Services.PLATFORM.logInfo("Successfully applied update for version: " + updateVersionId);
    } catch (Exception e) {
      Services.PLATFORM.logError("Error applying update for version: " + updateVersionId, e);
    }
  }
}
