package lykrast.bypowderandsteel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lykrast.bypowderandsteel.config.BPASConfig;
import lykrast.bypowderandsteel.config.BPASConfigValues;
import lykrast.bypowderandsteel.registry.BPASBlocks;
import lykrast.bypowderandsteel.registry.BPASEffects;
import lykrast.bypowderandsteel.registry.BPASEntities;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.bypowderandsteel.registry.BPASSounds;
import lykrast.bypowderandsteel.registry.BPASVillagers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ByPowderAndSteel.MODID)
public class ByPowderAndSteel {
	public static final String MODID = "bypowderandsteel";
	public static final String GWR_MODID = "gunswithoutroses";

	public static final Logger LOG = LogManager.getLogger();

	public ByPowderAndSteel() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		BPASBlocks.REG.register(bus);
		BPASItems.REG.register(bus);
		bus.addListener(BPASItems::makeCreativeTab);
		BPASEntities.REG.register(bus);
		BPASSounds.REG.register(bus);
		BPASEffects.REG.register(bus);
		BPASVillagers.POI.register(bus);
		BPASVillagers.PROFESSION.register(bus);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BPASConfig.COMMON_SPEC);
		bus.addListener(this::onModConfigEvent);

		//TODO hero of the village loot table
		//The map is private, and while I see neoforge eventually makes it acessible, I don't think it does it yet in 1.20.1
		//and IE uses a mixin so bleh
	}

	public static ResourceLocation rl(String name) {
		return new ResourceLocation(MODID, name);
	}

	public void onModConfigEvent(final ModConfigEvent event) {
		ModConfig config = event.getConfig();
		// Recalculate the configs when they change
		if (config.getSpec() == BPASConfig.COMMON_SPEC) BPASConfigValues.refresh(config);
	}
}
