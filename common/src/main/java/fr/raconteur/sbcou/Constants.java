package fr.raconteur.sbcou;

public class Constants {

	public static final String MOD_ID = "sbcou";
	public static final String MOD_NAME = "SCBOU";
	public static final String VERSIONS_DIR = OptionsActionsUtils.getSbcouConfigDir() + "/versions";
	public static final String VERSIONS_FILE = OptionsActionsUtils.getSbcouConfigDir() + "/versions.json";
	public static final String CURRENT_VERSION_FILE = OptionsActionsUtils.getSbcouConfigDir() + "/current_version.txt";
	public static final String DEV_DIR = OptionsActionsUtils.getSbcouConfigDir() + "/dev";
	public static final String DEV_EDITOR_MEMORY_JSON = DEV_DIR + "/editor_memory.json";
	public static final String DEV_FULL_CONTEXT_DIR = DEV_DIR + "/full_context";
	public static final String IGNORED_CHANGE_FILE = DEV_DIR + "/ignored_change.json";
	public static final String IGNORED_OPTION_PATHS_FILE = OptionsActionsUtils.getSbcouConfigDir() + "/ignored_option_paths.json";
	public static final String FILE_EXTENSION_HANDLING_FILE = OptionsActionsUtils.getSbcouConfigDir() + "/file_extension_handling.json";
	public static final String SPECIFIC_FILE_HANDLING_FILE = OptionsActionsUtils.getSbcouConfigDir() + "/specific_file_handling.json";
	public static final String EXCLUDED_FILE_PATHS_FILE = OptionsActionsUtils.getSbcouConfigDir() + "/excluded_file_paths.json";
	public static final String UNPARSABLE_FILES_FILE = OptionsActionsUtils.getSbcouConfigDir() + "/unparsable_files.json";
}