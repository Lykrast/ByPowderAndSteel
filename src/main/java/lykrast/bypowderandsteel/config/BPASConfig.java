package lykrast.bypowderandsteel.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class BPASConfig {
	//just copying what I did for meet your fight that was copied from book wyrms
	public static final ForgeConfigSpec COMMON_SPEC;
	public static final BPASConfig COMMON;

	static {
		Pair<BPASConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(BPASConfig::new);
		COMMON = specPair.getLeft();
		COMMON_SPEC = specPair.getRight();
	}

	public final IntValue saberSentryMaxHeight, blasterSentryMaxHeight;
	public final BooleanValue kubejsArmor;

	public BPASConfig(ForgeConfigSpec.Builder builder) {
		builder.comment("Mobs");
		builder.push("mobs");
		saberSentryMaxHeight = intval(builder, "saberSentryMaxHeight", 8, -1024, 1024, "Maximum y level at which Saber Sentries can spawn",
				"They're intended to be found at deepslate layer but you can put them somewhere else if you want");
		blasterSentryMaxHeight = intval(builder, "blasterSentryMaxHeight", 8, -1024, 1024, "Maximum y level at which Blaster Sentries can spawn",
				"They're intended to be found at deepslate layer but you can put them somewhere else if you want");
		builder.pop();
		builder.comment("Specific debug and/or packdev related stuff");
		builder.comment("READ THESE CAREFULLY you probably don't need to touch them");
		builder.push("miscstuffyoushouldntmesswith");
		kubejsArmor = boolval(builder, "hackThatMakesArmorsModifiableByKubeJSButMessesWithTheirNormalAttributes", false, "Use the default attribute maps in the armors, which in practice does 2 things:",
				"1) allows KubeJS to actually modify the armor attributes (because it access transformers that map)",
				"2) remove the gun-related attribute from them so you gotta manually add them back with KubeJS (because that's why I went around that map in the first place)");
		builder.pop();
	}

	private IntValue intval(ForgeConfigSpec.Builder builder, String name, int def, int min, int max, String... comments) {
		return builder.translation(name).comment(comments).comment("Default: " + def).defineInRange(name, def, min, max);
	}

	@SuppressWarnings("unused")
	private DoubleValue doubleval(ForgeConfigSpec.Builder builder, String name, double def, double min, double max, String... comments) {
		return builder.translation(name).comment(comments).comment("Default: " + def).defineInRange(name, def, min, max);
	}

	private BooleanValue boolval(ForgeConfigSpec.Builder builder, String name, boolean def, String... comments) {
		return builder.translation(name).comment(comments).comment("Default: " + def).define(name, def);
	}

}
