package io.github.a5h73y.parkour.upgrade.major;

import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.database.ParkourDatabase;
import io.github.a5h73y.parkour.database.SQLite;
import io.github.a5h73y.parkour.database.TimeEntry;
import io.github.a5h73y.parkour.upgrade.ParkourUpgrader;
import io.github.a5h73y.parkour.upgrade.TimedUpgradeTask;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import pro.husk.Database;
import pro.husk.mysql.MySQL;

public class DatabaseUpgradeTask extends TimedUpgradeTask {

	public DatabaseUpgradeTask(ParkourUpgrader parkourUpgrader) {
		super(parkourUpgrader);
	}

	private final Map<Integer, String> courseIdToName = new HashMap<>();
	private final Map<String, List<TimeEntry>> playerNameToTimes = new HashMap<>();

	@Override
	protected String getTitle() {
		return "Database";
	}

	@Override
	protected boolean doWork() {
		return true;
	}

	/**
	 * Proceed to do the additional Database work required.
	 * Reinserts the times and cleans up the temporary tables.
	 *
	 * @return success
	 */
	public boolean doMoreWork() {
		return true;
	}
}
