package ru.redserver.prtweaker;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import ru.redserver.prtweaker.asm.ClassTransformer;

/**
 * Главный класс мода
 * @author TheAndrey
 */
@Mod(modid = ModPRTweaker.MOD_ID, name = ModPRTweaker.MOD_NAME, version = ModPRTweaker.MOD_VERSION, acceptableRemoteVersions = "*")
public final class ModPRTweaker {

	public static final String MOD_ID = "prtweaker";
	public static final String MOD_NAME = "ProjectRed Tweaker";
	public static final String MOD_VERSION = "1.0";

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(!ClassTransformer.patched) throw new RuntimeException("PR Patch not installied!");
	}

}
