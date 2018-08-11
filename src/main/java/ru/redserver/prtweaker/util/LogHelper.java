package ru.redserver.prtweaker.util;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import org.apache.logging.log4j.Level;

public final class LogHelper {

	private static final String PREFIX = "[PRTweaker] ";

	private LogHelper() {
	}

	public static void info(String message) {
		FMLRelaunchLog.info(PREFIX + message);
	}

	public static void warn(String message) {
		FMLRelaunchLog.warning(PREFIX + message);
	}

	public static void log(Level level, String message, Throwable throwable) {
		FMLRelaunchLog.log(level, throwable, PREFIX + message);
	}

}
