package ru.redserver.prtweaker;

import java.io.File;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * Конфигурация
 * @author TheAndrey
 */
public final class TweakerConfig {

	public static boolean enableBlockUpdateHandler = true;

	private TweakerConfig() {
	}

	public static void load(File file) {
		if(file == null) throw new IllegalArgumentException("file is null!");
		Configuration config = new Configuration(file);
		config.load();

		Property prop;

		prop = config.get("tweaks", "EnableBlockUpdateHandler", enableBlockUpdateHandler);
		prop.comment = "Позволяет отключить BlockUpdateHandler. Данный компонент отвечает за генерацию замшелого булыжника. Создаёт достаточно большую нагрузку на сервер.";
		enableBlockUpdateHandler = prop.getBoolean();

		if(config.hasChanged()) config.save();
	}

}
