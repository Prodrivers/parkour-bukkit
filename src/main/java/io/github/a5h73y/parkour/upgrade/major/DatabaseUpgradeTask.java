package io.github.a5h73y.parkour.upgrade.major;

import static io.github.a5h73y.parkour.utility.PluginUtils.readContentsOfResource;

import io.github.a5h73y.parkour.upgrade.ParkourUpgrader;
import io.github.a5h73y.parkour.upgrade.TimedUpgradeTask;
import io.github.a5h73y.parkour.utility.PluginUtils;
import java.io.IOException;
import java.sql.SQLException;
import pro.husk.Database;
import pro.husk.mysql.MySQL;

public class DatabaseUpgradeTask extends TimedUpgradeTask {

	public DatabaseUpgradeTask(ParkourUpgrader parkourUpgrader) {
		super(parkourUpgrader);
	}

	@Override
	protected String getTitle() {
		return "Database";
	}

	@Override
	protected boolean doWork() {
		return true;
	}
}
