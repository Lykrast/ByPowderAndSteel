package lykrast.bypowderandsteel.registry;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.misc.EnchantedItemTrade;
import lykrast.bypowderandsteel.misc.VillagerTradeBuilder;
import lykrast.gunswithoutroses.registry.GWRItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ByPowderAndSteel.MODID)
public class BPASVillagers {
	//copied all that stuff from pnc:r and ie
	public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, ByPowderAndSteel.MODID);
	public static final DeferredRegister<VillagerProfession> PROFESSION = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ByPowderAndSteel.MODID);
	public static RegistryObject<PoiType> gunsmithPOI;
	public static RegistryObject<VillagerProfession> gunsmith;
	
	static {
		gunsmithPOI = POI.register("gunsmithing_table", () -> new PoiType(ImmutableSet.copyOf(BPASBlocks.gunsmithingTable.get().getStateDefinition().getPossibleStates()), 1, 1));
		gunsmith = PROFESSION.register("gunsmith", () -> profession(ByPowderAndSteel.rl("gunsmith"), gunsmithPOI, SoundEvents.VILLAGER_WORK_WEAPONSMITH));
	}
	
	//both ie and pnc have the same helper function for that, hmmm well let's join em
	//I think it's copied from the vanilla method
	private static VillagerProfession profession(ResourceLocation name, RegistryObject<PoiType> workstation, SoundEvent sound) {
		ResourceKey<PoiType> poiName = Objects.requireNonNull(workstation.getKey());
		return new VillagerProfession(name.toString(), holder -> holder.is(poiName), holder -> holder.is(poiName), ImmutableSet.of(), ImmutableSet.of(), sound);
	}

	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event) {
		Int2ObjectMap<List<ItemListing>> trades = event.getTrades();
		//THE FUCKING VANILLA TRADES ARE PACKAGE PRIVATE CLASSES AAAAAAAAA
		if (event.getType() == gunsmith.get()) {
			//TODO redo all that when I get the artisan guns and bullets in
			trades.get(1).add(new VillagerTradeBuilder(16, 2, 0.05).buyManyForOne(Items.GUNPOWDER, 12, 16).build());
			trades.get(1).add(new VillagerTradeBuilder(16, 2, 0.05).buyManyForOne(BPASItems.gunsteelIngot, 8, 12).build());
			trades.get(1).add(new VillagerTradeBuilder(12, 1, 0.05).sell(BPASItems.gunsteelBullet, 12, 20).emeraldPrice(1, 1).build());
			
			trades.get(2).add(new VillagerTradeBuilder(12, 10, 0.05).buyManyForOne(Items.IRON_INGOT, 3, 5).build());
			trades.get(2).add(new VillagerTradeBuilder(12, 5, 0.05).sell(BPASItems.gunsteelGun, 1, 1).emeraldPrice(2, 4).build());
			trades.get(2).add(new EnchantedItemTrade(GWRItems.goldGun, 1, 5, 19, 3, 5, 0.05));

			trades.get(3).add(new VillagerTradeBuilder(12, 10, 0.05).sell(GWRItems.amethystBullet, 8, 12).emeraldPrice(1, 1).build());
			trades.get(3).add(new VillagerTradeBuilder(12, 10, 0.05).sell(GWRItems.explosiveBullet, 8, 12).emeraldPrice(1, 1).build());
			trades.get(3).add(new VillagerTradeBuilder(12, 10, 0.05).sell(GWRItems.prismarineBullet, 8, 12).emeraldPrice(1, 1).build());
			trades.get(3).add(new VillagerTradeBuilder(12, 10, 0.05).sell(BPASItems.caliberry, 8, 12).emeraldPrice(1, 1).build());
			
			trades.get(4).add(new VillagerTradeBuilder(12, 30, 0.05).buyManyForOne(Items.DIAMOND, 1, 1).build());
			trades.get(4).add(new VillagerTradeBuilder(12, 30, 0.05).buyManyForOne(BPASItems.damagedDevice, 8, 12).build());

			trades.get(5).add(new EnchantedItemTrade(GWRItems.diamondShotgun, 20, 5, 19, 3, 30, 0.2));
			trades.get(5).add(new EnchantedItemTrade(GWRItems.diamondSniper, 20, 5, 19, 3, 30, 0.2));
			trades.get(5).add(new EnchantedItemTrade(GWRItems.diamondGatling, 20, 5, 19, 3, 30, 0.2));
		}
	}

}
