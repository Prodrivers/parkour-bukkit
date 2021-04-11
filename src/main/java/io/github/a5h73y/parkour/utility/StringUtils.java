package io.github.a5h73y.parkour.utility;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;

/**
 * String / Message related utility methods.
 */
public class StringUtils {

	/**
	 * Translate colour codes of provided message.
	 *
	 * @param message message
	 * @return colourised message
	 */
	public static String colour(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * Format and standardize text to a constant case.
	 * Will transform "hElLO" into "Hello".
	 *
	 * @param text message
	 * @return standardized input
	 */
	public static String standardizeText(String text) {
		return !ValidationUtils.isStringValid(text) ? text
				: text.substring(0, 1).toUpperCase()
				.concat(text.substring(1).toLowerCase())
				.replace("_", " ");
	}

	public static String extractMessageFromArgs(String[] args, int startIndex) {
		return extractMessageFromArgs(Arrays.asList(args), startIndex);
	}

	public static String extractMessageFromArgs(List<String> args, int startIndex) {
		return String.join(" ", args.subList(startIndex, args.size()));
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	/**
	 * Return the hexadecimal representation of a byte array.
	 *
	 * @param bytes byte array
	 * @return hexadecimal representation of byte array, stored as a string
	 */
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}
}
