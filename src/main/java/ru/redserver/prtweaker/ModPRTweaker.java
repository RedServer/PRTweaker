package ru.redserver.prtweaker;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import java.io.File;
import net.minecraft.item.Item;
import ru.redserver.prtweaker.asm.handler.InstancedBlockHandler;
import ru.redserver.prtweaker.asm.handler.TransportationSPHHandler;
import ru.redserver.prtweaker.util.LogHelper;

/**
 * Главный класс мода
 * @author TheAndrey
 */
@Mod(
		modid = ModPRTweaker.MOD_ID,
		name = ModPRTweaker.MOD_NAME,
		version = ModPRTweaker.MOD_VERSION,
		dependencies = "required-after:ProjRed|Core",
		acceptableRemoteVersions = "*"
)
public final class ModPRTweaker {

	public static final String MOD_ID = "prtweaker";
	public static final String MOD_NAME = "ProjectRed Tweaker";
	public static final String MOD_VERSION = "@VERSION@";

	public static Item itemRoutingChip;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File file = new File(event.getModConfigurationDirectory(), "PRTweaker.cfg");
		TweakerConfig.load(file);
		LogHelper.info("Config loaded.");
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartedEvent event) {
		TransportationSPHHandler.check();
		InstancedBlockHandler.check();
	}

}
