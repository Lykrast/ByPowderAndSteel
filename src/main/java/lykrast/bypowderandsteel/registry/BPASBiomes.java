package lykrast.bypowderandsteel.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ByPowderAndSteel.MODID)
public class BPASBiomes {
	public static final DeferredRegister<Biome> REG = DeferredRegister.create(ForgeRegistries.BIOMES, ByPowderAndSteel.MODID);
	//copying yungs cave here, cause I don't think I need the actual biome objects anywhere
	public static ResourceKey<Biome> gungusGrove;
	
	//for the terrablender region
	public static List<Pair<ResourceKey<Biome>, Climate.ParameterPoint>> biomesToAdd = new ArrayList<>();
	
	//I don't fucking know, TODO make it work

}
