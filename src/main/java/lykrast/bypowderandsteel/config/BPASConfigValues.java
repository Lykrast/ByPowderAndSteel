package lykrast.bypowderandsteel.config;

import net.minecraftforge.fml.config.ModConfig;

public class BPASConfigValues {
	public static int SABER_SENTRY_MAX_HEIGHT = 8, BLASTER_SENTRY_MAX_HEIGHT = 8;
	public static boolean KUBEJS_ARMOR = false;

	public static void refresh(ModConfig config) {
		SABER_SENTRY_MAX_HEIGHT = BPASConfig.COMMON.saberSentryMaxHeight.get();
		BLASTER_SENTRY_MAX_HEIGHT = BPASConfig.COMMON.blasterSentryMaxHeight.get();
		KUBEJS_ARMOR = BPASConfig.COMMON.kubejsArmor.get();
	}
}
