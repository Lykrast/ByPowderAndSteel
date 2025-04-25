package lykrast.bypowderandsteel.world;

import java.util.function.Consumer;

import com.mojang.datafixers.util.Pair;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;

public class BPASCaveRegion extends Region {

	public BPASCaveRegion(ResourceLocation name, RegionType type, int weight) {
		super(name, type, weight);
	}
	
	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
		//Base vanilla biomes with no changes
		addModifiedVanillaOverworldBiomes(consumer, nochanges -> {});
		addBiome(consumer, null, null);
	}
	
	public static void register(FMLCommonSetupEvent event) {
		//quark has weight 1 by default, yung cave is 3, rare BOP is 2
		//TODO configurable weight
		event.enqueueWork(() -> Regions.register(new BPASCaveRegion(ByPowderAndSteel.rl("caves"), RegionType.OVERWORLD, 2)));
	}

}
