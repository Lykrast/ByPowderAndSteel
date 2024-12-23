package lykrast.bypowderandsteel.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BPASBlocks {
	public static final DeferredRegister<Block> REG = DeferredRegister.create(ForgeRegistries.BLOCKS, ByPowderAndSteel.MODID);
	public static RegistryObject<Block> gunsteelScrapBlock, gunsteelBlock;
	public static RegistryObject<Block> gunsteelBricks;

	public static List<RegistryObject<? extends Item>> orderedBlockItems = new ArrayList<>();
	
	static {
		gunsteelScrapBlock = makeBlock("gunsteel_scrap_block", () -> new Block(Block.Properties.copy(Blocks.RAW_IRON_BLOCK)));
		gunsteelBlock = makeBlock("gunsteel_block", () -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)));
		gunsteelBricks = makeBlock("gunsteel_bricks", () -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)));
	}
	
	private static RegistryObject<Block> makeBlock(String name, Supplier<Block> block) {
		RegistryObject<Block> regged = REG.register(name, block);
		orderedBlockItems.add(BPASItems.REG.register(name, () -> new BlockItem(regged.get(), (new Item.Properties()))));
		return regged;
	}

}
