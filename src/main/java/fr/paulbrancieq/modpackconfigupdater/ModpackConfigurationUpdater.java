package fr.paulbrancieq.modpackconfigupdater;

import fr.paulbrancieq.modpackconfigupdater.mcufiles.version.change.VersionChangesJsonFile;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class ModpackConfigurationUpdater implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("modpack-config-updater");
		public static final String MOD_ID = "modpack-config-updater";

	@Override
	public void onInitialize() {
		VersionChangesJsonFile versionChangeJsonFile = new VersionChangesJsonFile("D:\\Workspace\\minecraft\\mods\\modpack-configuration-updater\\run\\config\\version_test\\changes\\1.1.json");
		LOGGER.info("Hello Fabric world!");
	}

	public void backup() {
		Path configPath = FabricLoader.getInstance().getConfigDir();
		Backup backup = new Backup(configPath.toString(),
				"D:\\Workspace\\minecraft\\mods\\modpack-configuration-updater\\run\\config\\backup_test",
				"test");
		LOGGER.info("Done!");
		String path = "test/testdir/test3";
		backup.add(new OptionPath(path + ":mouseSensitivity"));
		backup.apply();
	}
}