package io.github.a5h73y.parkour.configuration;

import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.serialize.SimplixSerializer;
import io.github.a5h73y.parkour.configuration.impl.DefaultConfig;
import io.github.a5h73y.parkour.configuration.impl.StringsConfig;
import io.github.a5h73y.parkour.configuration.serializable.CourseSerializable;
import io.github.a5h73y.parkour.configuration.serializable.ItemStackArraySerializable;
import io.github.a5h73y.parkour.configuration.serializable.ItemStackSerializable;
import io.github.a5h73y.parkour.configuration.serializable.LocationSerializable;
import io.github.a5h73y.parkour.configuration.serializable.ParkourSessionSerializable;
import io.github.a5h73y.parkour.type.course.CourseConfig;
import io.github.a5h73y.parkour.type.course.autostart.AutoStartConfig;
import io.github.a5h73y.parkour.type.kit.ParkourKitConfig;
import io.github.a5h73y.parkour.type.lobby.LobbyConfig;
import io.github.a5h73y.parkour.type.player.PlayerConfig;
import io.github.a5h73y.parkour.type.player.completion.CourseCompletionConfig;
import io.github.a5h73y.parkour.type.player.quiet.QuietModeConfig;
import io.github.a5h73y.parkour.type.player.rank.ParkourRankConfig;
import io.github.a5h73y.parkour.utility.PluginUtils;
import io.github.a5h73y.parkour.utility.cache.GenericCache;
import java.io.File;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Parkour Configuration Manager.
 * Manages and stores references to each of the available Config files.
 */
public class ConfigManager {

	private final File dataFolder;

	// core
	private final DefaultConfig defaultConfig;
	private final StringsConfig stringsConfig;

	// others
	private final ParkourKitConfig parkourKitConfig;
	private final ParkourRankConfig parkourRankConfig;
	private final AutoStartConfig autoStartConfig;
	private final CourseCompletionConfig courseCompletionsConfig;
	private final QuietModeConfig quietModeConfig;
	private final LobbyConfig lobbyConfig;

	// cache
	private final GenericCache<UUID, PlayerConfig> playerConfigCache;
	private final GenericCache<String, CourseConfig> courseConfigCache;

	// directories
	private final File playersDir;
	private final File parkourSessionsDir;
	private final File coursesDir;
	private final File otherDir;

	// serializers
	private final ItemStackSerializable itemStackSerializable = new ItemStackSerializable();

	/**
	 * Initialise the Config Manager.
	 * Will invoke setup for each available config type.
	 *
	 * @param dataFolder where to store the configs
	 */
	public ConfigManager(File dataFolder) {
		this.dataFolder = dataFolder;
		playersDir = new File(dataFolder, "players");
		parkourSessionsDir = new File(dataFolder, "sessions");
		coursesDir = new File(dataFolder, "courses");
		otherDir = new File(dataFolder, "other");
		createParkourFolders();

		defaultConfig = new DefaultConfig(new File(dataFolder, "config.yml"));
		stringsConfig = new StringsConfig(new File(dataFolder, "strings.yml"));

		// everything else
		parkourKitConfig = new ParkourKitConfig(new File(otherDir, "parkour-kits.yml"));
		parkourRankConfig = new ParkourRankConfig(new File(otherDir, "parkour-ranks.yml"));
		autoStartConfig = new AutoStartConfig(new File(otherDir, "auto-starts.yml"));
		courseCompletionsConfig = new CourseCompletionConfig(new File(otherDir, "course-completions.yml"));
		quietModeConfig = new QuietModeConfig(new File(otherDir, "quiet-players.yml"));
		lobbyConfig = new LobbyConfig(new File(otherDir, "parkour-lobbies.yml"));

		this.playerConfigCache = new GenericCache<>(30L);
		this.courseConfigCache = new GenericCache<>(30L);

		SimplixSerializer.registerSerializable(itemStackSerializable);
		SimplixSerializer.registerSerializable(new ItemStackArraySerializable());
		SimplixSerializer.registerSerializable(new LocationSerializable());
		SimplixSerializer.registerSerializable(new CourseSerializable());
		SimplixSerializer.registerSerializable(new ParkourSessionSerializable());
	}

	/**
	 * Get the Player's JSON config file.
	 * Cached result will be retrieved, or fresh copy will be gathered otherwise.
	 *
	 * @param player offline player
	 * @return player's config
	 */
	@NotNull
	public PlayerConfig getPlayerConfig(@NotNull OfflinePlayer player) {
		UUID key = player.getUniqueId();
		if (!playerConfigCache.containsKey(key) || playerConfigCache.get(key).isEmpty()) {
			playerConfigCache.put(key, PlayerConfig.getConfig(player));
		}

		return playerConfigCache.get(key).orElse(PlayerConfig.getConfig(player));
	}

	/**
	 * Get the Course's JSON config file.
	 * Cached result will be retrieved, or fresh copy will be gathered otherwise.
	 *
	 * @param courseName course name
	 * @return course's config
	 */
	@NotNull
	public CourseConfig getCourseConfig(@NotNull String courseName) {
		String key = courseName.toLowerCase();
		if (!courseConfigCache.containsKey(key) || courseConfigCache.get(key).isEmpty()) {
			courseConfigCache.put(key, CourseConfig.getConfig(courseName));
		}

		return courseConfigCache.get(key).orElse(CourseConfig.getConfig(courseName));
	}

	/**
	 * Reload each of the configuration files.
	 */
	public void reloadConfigs() {
		for (FlatFile configs : getAllConfigs()) {
			configs.forceReload();
		}
	}

	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}

	public StringsConfig getStringsConfig() {
		return stringsConfig;
	}

	public ParkourKitConfig getParkourKitConfig() {
		return parkourKitConfig;
	}

	public ParkourRankConfig getParkourRankConfig() {
		return parkourRankConfig;
	}

	public AutoStartConfig getAutoStartConfig() {
		return autoStartConfig;
	}

	public CourseCompletionConfig getCourseCompletionsConfig() {
		return courseCompletionsConfig;
	}

	public QuietModeConfig getQuietModeConfig() {
		return quietModeConfig;
	}

	public LobbyConfig getLobbyConfig() {
		return lobbyConfig;
	}

	public File getPlayersDir() {
		return playersDir;
	}

	public File getParkourSessionsDir() {
		return parkourSessionsDir;
	}

	public File getCoursesDir() {
		return coursesDir;
	}

	public File getOtherDir() {
		return otherDir;
	}

	public ItemStackSerializable getItemStackSerializable() {
		return itemStackSerializable;
	}

	private void createParkourFolders() {
		File[] parkourFolders = {dataFolder, playersDir, parkourSessionsDir, coursesDir, otherDir};

		for (File folder : parkourFolders) {
			if (!folder.exists() && folder.mkdirs()) {
				PluginUtils.log("Created folder: " + folder.getName());
			}
		}
	}

	private FlatFile[] getAllConfigs() {
		return new FlatFile[]{
				defaultConfig,
				stringsConfig,
				parkourKitConfig,
				parkourRankConfig,
				autoStartConfig,
				courseCompletionsConfig,
				quietModeConfig,
				lobbyConfig
		};
	}
}
