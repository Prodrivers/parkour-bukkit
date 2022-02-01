package io.github.a5h73y.parkour.commands;

import static io.github.a5h73y.parkour.utility.permission.PermissionUtils.WILDCARD;

import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.utility.TranslationUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CommandUsage {

	public static final String ARRAY_OPEN = "[";
	public static final String ARRAY_CLOSE = "]";
	public static final String SUBSTITUTION_OPEN = "(";
	public static final String SUBSTITUTION_CLOSE = ")";
	public static final String FORMULA_OPEN = "{";
	public static final String FORMULA_CLOSE = "}";

	private static final String COMMA = ",";
	private static final String SPACE = " ";

	private String command;
	private String title;
	private String arguments;
	private String example;
	private String description;
	private String permission;
	private String commandGroup;
	private String consoleSyntax;
	private String autoTabSyntax;

	/**
	 * Display Help Information for the Command.
	 * Includes usage information and a description of the command.
	 * Will display appropriate information based on the type of sender.
	 *
	 * @param commandSender command sender
	 */
	public void displayHelpInformation(CommandSender commandSender) {
		TranslationUtils.sendHeading(title, commandSender);

		if (commandSender instanceof ConsoleCommandSender) {
			TranslationUtils.sendValueTranslation("Help.ConsoleCommandSyntax", consoleSyntax, false, commandSender);

		} else {
			String commandSyntax = arguments != null ? command + SPACE + arguments : command;
			TranslationUtils.sendValueTranslation("Help.CommandSyntax", commandSyntax, false, commandSender);
		}
		TranslationUtils.sendValueTranslation("Help.CommandExample", example, false, commandSender);
		TranslationUtils.sendHeading("Description", commandSender);
		commandSender.sendMessage(description);
	}

	/**
	 * Display Command Usage.
	 * Formats the information to display command syntax and brief command title.
	 * @param commandSender command sender
	 */
	public void displayCommandUsage(CommandSender commandSender) {
		commandSender.sendMessage(TranslationUtils.getTranslation("Help.CommandUsage", false)
				.replace("%COMMAND%", command)
				.replace("%ARGUMENTS%", arguments != null ? SPACE + arguments : "")
				.replace("%TITLE%", title));
	}

	/**
	 * Display invalid syntax error.
	 *
	 * @param commandSender command sender
	 */
	public void sendInvalidSyntax(CommandSender commandSender) {
		commandSender.sendMessage(TranslationUtils.getTranslation("Error.Syntax")
				.replace("%COMMAND%", getCommand())
				.replace("%ARGUMENTS%", getArguments()));
	}

	public String[] getAutoTabArraySelection(String input) {
		return StringUtils.substringBetween(input, ARRAY_OPEN, ARRAY_CLOSE).split(COMMA);
	}

	public String[] getAutoTabSyntaxArgs() {
		return autoTabSyntax.split(SPACE);
	}

	public String getCommand() {
		return command;
	}

	public String getTitle() {
		return title;
	}

	public String getArguments() {
		return arguments;
	}

	public String getExample() {
		return example;
	}

	public String getDescription() {
		return description;
	}

	public String getPermission() {
		return permission;
	}

	public String getCommandGroup() {
		return commandGroup;
	}

	public String getConsoleSyntax() {
		return consoleSyntax;
	}

	public String getAutoTabSyntax() {
		return autoTabSyntax;
	}

	public String resolveFormulaValue(String input, String[] args) {
		String[] possibleReplacements = StringUtils.substringBetween(input, FORMULA_OPEN, FORMULA_CLOSE).split(COMMA);
		for (String replacement : possibleReplacements) {
			String[] assignmentSplit = replacement.split("=");
			String[] indexValueSplit = assignmentSplit[0].split(":");

			// wildcard - match any
			if (indexValueSplit[0].equals(WILDCARD)) {
				return assignmentSplit[1];
			} else {
				int index = Integer.parseInt(indexValueSplit[0]);

				if (args[index].equals(indexValueSplit[1])) {
					return assignmentSplit[1];
				}
			}
		}
		return "";
	}
}
