package lykrast.bypowderandsteel.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BPASBlocks {
	public static final DeferredRegister<Block> REG = DeferredRegister.create(ForgeRegistries.BLOCKS, ByPowderAndSteel.MODID);
	public static RegistryObject<Block> gunsmithingTable;
	public static RegistryObject<Block> gunsteelScrapBlock, gunsteelBlock;
	public static RegistryObject<Block> gunsteelBricks, gunsteelChiseled, gunsteelPillar, gunsteelLamp;
	//forest
	public static RegistryObject<Block> caliberryVine, caliberrySack;
	public static RegistryObject<Block> livingHerbSack;
	public static RegistryObject<Block> livingHerb, lhFramed, lhCrossCardinal, lhCrossDiagonal, lhCrossCombined, lhLattice, lhLatticeDouble;
	public static RegistryObject<Block> lhPillarCardinal, lhPillarDiagonal, lhPillarCombined, lhPillarDiaCar, lhPillarComCar, lhPillarDiaCom;
	//desert
	public static RegistryObject<Block> cowbonesHornCrate;
	public static RegistryObject<Block> bovony, bovonyTilesBig, bovonyTiles, bovonyBricks, bovonyChiseled, bovonyPillar, bovonyLamp;
	//tundra
	public static RegistryObject<Block> milspecIceCrate;
	public static RegistryObject<Block> milspecIceBlock, milspecIceBricks, milspecIceTiles, milspecIceChiseled, milspecIcePillar;
	//ocean
	public static RegistryObject<Block> doubloonCrate;
	public static RegistryObject<Block> doubloonBricks, doubloonTiles, doubloonChiseled;
	//underground
	//nether
	//end
	public static RegistryObject<Block> gravioliumTank;

	public static List<RegistryObject<? extends Item>> orderedBlockItems = new ArrayList<>();
	
	static {
		gunsmithingTable = makeBlock("gunsmithing_table", () -> new GunsmithingTableBlock(Block.Properties.copy(Blocks.CRAFTING_TABLE)));
		
		gunsteelScrapBlock = makeBlock("gunsteel_scrap_block", () -> new Block(Block.Properties.copy(Blocks.RAW_IRON_BLOCK)));
		gunsteelBlock = makeBlock("gunsteel_block", () -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)));
		gunsteelBricks = makeBlock("gunsteel_bricks", () -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK).strength(2, 6))); //same hardness as bricks so easier to mine
		gunsteelChiseled = makeBlock("gunsteel_chiseled", () -> new Block(Block.Properties.copy(gunsteelBricks.get())));
		gunsteelPillar = makeBlock("gunsteel_pillar", () -> new OrientablePillarBlock(Block.Properties.copy(gunsteelBricks.get())));
		gunsteelLamp = makeBlock("gunsteel_lamp", () -> new Block(Block.Properties.copy(gunsteelBricks.get()).lightLevel((state) -> 15)));

		//like vines but not replaceable
		caliberryVine = makeBlock("caliberry_vine", () -> new CaliberryVineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().strength(0.2f).sound(SoundType.VINE).ignitedByLava().pushReaction(PushReaction.DESTROY)));
		caliberrySack = makeBlock("caliberry_sack", () -> new Block(Block.Properties.copy(Blocks.YELLOW_WOOL)));
		
		livingHerbSack = makeBlock("living_herb_sack", () -> new Block(Block.Properties.copy(Blocks.GREEN_WOOL)));
		livingHerb = makeBlock("living_herb_block", () -> new Block(Block.Properties.copy(Blocks.MOSS_BLOCK)));
		lhFramed = makeBlock("living_herb_framed", () -> new Block(Block.Properties.copy(livingHerb.get())));
		lhCrossCardinal = makeBlock("living_herb_cross_cardinal", () -> new Block(Block.Properties.copy(livingHerb.get())));
		lhCrossDiagonal = makeBlock("living_herb_cross_diagonal", () -> new Block(Block.Properties.copy(livingHerb.get())));
		lhCrossCombined = makeBlock("living_herb_cross_combined", () -> new Block(Block.Properties.copy(livingHerb.get())));
		lhLattice = makeBlock("living_herb_lattice", () -> new Block(Block.Properties.copy(livingHerb.get())));
		lhLatticeDouble = makeBlock("living_herb_lattice_double", () -> new Block(Block.Properties.copy(livingHerb.get())));
		lhPillarCardinal = makeBlock("living_herb_pillar_cardinal", () -> new RotatedPillarBlock(Block.Properties.copy(livingHerb.get())));
		lhPillarDiagonal = makeBlock("living_herb_pillar_diagonal", () -> new RotatedPillarBlock(Block.Properties.copy(livingHerb.get())));
		lhPillarCombined = makeBlock("living_herb_pillar_combined", () -> new RotatedPillarBlock(Block.Properties.copy(livingHerb.get())));
		lhPillarDiaCar = makeBlock("living_herb_pillar_diagonal_cardinal", () -> new OrientablePillarBlock(Block.Properties.copy(livingHerb.get())));
		lhPillarComCar = makeBlock("living_herb_pillar_combined_cardinal", () -> new OrientablePillarBlock(Block.Properties.copy(livingHerb.get())));
		lhPillarDiaCom = makeBlock("living_herb_pillar_diagonal_combined", () -> new OrientablePillarBlock(Block.Properties.copy(livingHerb.get())));
		
		cowbonesHornCrate = makeBlock("cowbones_horn_crate", () -> new Block(Block.Properties.copy(Blocks.ACACIA_PLANKS)));
		bovony = makeBlock("bovony", () -> new Block(Block.Properties.copy(Blocks.TUFF).mapColor(MapColor.TERRACOTTA_BLACK)));
		bovonyTilesBig = makeBlock("bovony_tiles_big", () -> new Block(Block.Properties.copy(bovony.get())));
		bovonyTiles = makeBlock("bovony_tiles", () -> new Block(Block.Properties.copy(bovony.get())));
		bovonyBricks = makeBlock("bovony_bricks", () -> new Block(Block.Properties.copy(bovony.get())));
		bovonyChiseled = makeBlock("bovony_chiseled", () -> new Block(Block.Properties.copy(bovony.get())));
		bovonyPillar = makeBlock("bovony_pillar", () -> new RotatedPillarBlock(Block.Properties.copy(bovony.get())));
		bovonyLamp = makeBlock("bovony_lamp", () -> new RotatedPillarBlock(Block.Properties.copy(bovony.get()).lightLevel((state) -> 15)));
		
		milspecIceCrate = makeBlock("milspec_ice_crate", () -> new Block(Block.Properties.copy(Blocks.DARK_OAK_PLANKS)));
		milspecIceBlock = makeBlock("milspec_ice_block", () -> new Block(Block.Properties.copy(Blocks.BRICKS).mapColor(MapColor.ICE).sound(SoundType.GLASS).instrument(NoteBlockInstrument.CHIME)));
		milspecIceBricks = makeBlock("milspec_ice_bricks", () -> new Block(Block.Properties.copy(milspecIceBlock.get())));
		milspecIceTiles = makeBlock("milspec_ice_tiles", () -> new Block(Block.Properties.copy(milspecIceBlock.get())));
		milspecIceChiseled = makeBlock("milspec_ice_chiseled", () -> new Block(Block.Properties.copy(milspecIceBlock.get())));
		milspecIcePillar = makeBlock("milspec_ice_pillar", () -> new RotatedPillarBlock(Block.Properties.copy(milspecIceBlock.get())));
		
		doubloonCrate = makeBlock("sunken_doubloon_crate", () -> new Block(Block.Properties.copy(Blocks.BIRCH_PLANKS)));
		doubloonBricks = makeBlock("doubloon_bricks", () -> new Block(Block.Properties.copy(Blocks.GOLD_BLOCK)));
		doubloonTiles = makeBlock("doubloon_tiles", () -> new Block(Block.Properties.copy(doubloonBricks.get())));
		doubloonChiseled = makeBlock("doubloon_chiseled", () -> new Block(Block.Properties.copy(doubloonBricks.get())));
		
		gravioliumTank = makeBlock("graviolium_tank", () -> new RotatedPillarBlock(Block.Properties.copy(Blocks.IRON_BLOCK)));
	}
	
	private static RegistryObject<Block> makeBlock(String name, Supplier<Block> block) {
		RegistryObject<Block> regged = REG.register(name, block);
		orderedBlockItems.add(BPASItems.REG.register(name, () -> new BlockItem(regged.get(), (new Item.Properties()))));
		return regged;
	}

}
	