package lykrast.bypowderandsteel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lykrast.bypowderandsteel.registry.BPASBlocks;
import lykrast.bypowderandsteel.registry.BPASEntities;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.bypowderandsteel.registry.BPASSounds;
import lykrast.bypowderandsteel.registry.BPASVillagers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
		BPASVillagers.POI.register(bus);
		BPASVillagers.PROFESSION.register(bus);
		
		//TODO hero of the village stuff
	}
	
	public static ResourceLocation rl(String name) {
		return new ResourceLocation(MODID, name);
	}
}
