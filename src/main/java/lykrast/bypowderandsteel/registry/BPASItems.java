package lykrast.bypowderandsteel.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.item.ContainerFoodItem;
import lykrast.bypowderandsteel.item.KnockbackBulletItem;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.item.GatlingItem;
import lykrast.gunswithoutroses.item.GunItem;
import lykrast.gunswithoutroses.registry.GWRSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class BPASItems {	
	public static final DeferredRegister<Item> REG = DeferredRegister.create(ForgeRegistries.ITEMS, ByPowderAndSteel.MODID);
	public static RegistryObject<GunItem> gunsteelGun, peashooter, cornGatling, desertRevolver, desertShotgun, arcticPistol, arcticSniper;
	public static RegistryObject<BulletItem> gunsteelBullet, caliberry, caliberryLarge;
	public static RegistryObject<Item> gunomeEgg, shrubhulkEgg, shrubsnapperEgg, cowbonesEgg, sabersentryEgg;
	public static RegistryObject<Item> gunsteelScrap, gunsteelIngot, gunsteelNugget;
	//forest
	public static RegistryObject<Item> caliberryGrilled, caliberrySlice, caliberrySliceGrilled, caliberryHoney, caliberryIce;
	public static RegistryObject<Item> livingHerb;
	//desert
	public static RegistryObject<Item> cowbonesHorn;
	//tundra
	public static RegistryObject<Item> milspecIce, milspecIceGrilled;
	//underground
	public static RegistryObject<Item> damagedDevice;
	//nether
	public static RegistryObject<Item> heptacle;
	//end
	public static RegistryObject<Item> gravioliumCell;
	
	private static List<RegistryObject<? extends Item>> orderedItemsCreative = new ArrayList<>();
	
	public static void makeCreativeTab(RegisterEvent event) {
		event.register(Registries.CREATIVE_MODE_TAB, helper -> {
			helper.register(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ByPowderAndSteel.rl("bypowderandsteel")),
					CreativeModeTab.builder().title(Component.translatable("itemGroup.bypowderandsteel")).icon(() -> new ItemStack(gunsteelGun.get()))
							.displayItems((parameters, output) -> {
								orderedItemsCreative.forEach(i -> output.accept(i.get()));
								BPASBlocks.orderedBlockItems.forEach(i -> output.accept(i.get()));
							}).build());
		});
	}
	
	static {
		//Guns
		gunsteelGun = initItem(() -> new GunItem(defP().durability(502), 0, 1, 16, 2, 16).repair(() -> Ingredient.of(ItemTags.create(new ResourceLocation("forge", "ingots/gunsteel")))), "gunsteel_gun");
		peashooter = initItem(() -> new GunItem(defP().durability(924), 0, 1, 16, 2, 20).chanceFreeShot(0.25).repair(() -> Ingredient.of(livingHerb.get())), "peashooter");
		cornGatling = initItem(() -> new GatlingItem(defP().durability(924), 0, 0.6, 5, 4, 20).chanceFreeShot(0.5).repair(() -> Ingredient.of(livingHerb.get())), "corn_gatling");
		desertRevolver = initItem(() -> new GunItem(defP().durability(753), 0, 1, 13, 4, 14).repair(() -> Ingredient.of(cowbonesHorn.get())), "desert_revolver");
		desertShotgun = initItem(() -> new GunItem(defP().durability(753), 0, 0.5, 18, 6, 14).projectiles(3).fireSound(GWRSounds.shotgun).repair(() -> Ingredient.of(cowbonesHorn.get())), "desert_shotgun");
		arcticPistol = initItem(() -> new GunItem(defP().durability(630), 1, 1, 18, 1, 8).repair(() -> Ingredient.of(milspecIce.get())), "arctic_pistol");
		arcticSniper = initItem(() -> new GunItem(defP().durability(630), 2, 1, 24, 0, 8).headshotMult(1.5).fireSound(GWRSounds.sniper).repair(() -> Ingredient.of(milspecIce.get())), "arctic_sniper");
		
		//Bullets
		gunsteelBullet = initItem(() -> new BulletItem(defP(), 6), "gunsteel_bullet");
		caliberry = initItem(() -> new KnockbackBulletItem(defP().food(food(1, 0.6)), 4, 1), "caliberry");
		caliberryLarge = initItem(() -> new KnockbackBulletItem(defP().food(food(4, 0.6)), 6, 2), "caliberry_large");
		
		//Spawn Eggs
		gunomeEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.gunome, 0x1F6878, 0xDB2F4C, defP()), "gunome_spawn_egg");
		shrubhulkEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.shrubhulk, 0x67A124, 0x4A381E, defP()), "shrubhulk_spawn_egg");
		shrubsnapperEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.shrubsnapper, 0x528919, 0x3F311D, defP()), "shrubsnapper_spawn_egg");
		cowbonesEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.cowbones, 0xEED9B3, 0x994141, defP()), "cowbones_spawn_egg");
		sabersentryEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.sabersentry, 0xF4F4E1, 0x383635, defP()), "sabersentry_spawn_egg");
		
		//Materials
		gunsteelScrap = initItem(() -> new Item(defP()), "gunsteel_scrap");
		gunsteelIngot = initItem(() -> new Item(defP()), "gunsteel_ingot");
		gunsteelNugget = initItem(() -> new Item(defP()), "gunsteel_nugget");

		caliberryGrilled = initItem(() -> new Item(defP().food(food(2, 0.6))), "caliberry_grilled");
		caliberrySlice = initItem(() -> new Item(defP().food(food(1, 0.6))), "caliberry_large_slice");
		caliberrySliceGrilled = initItem(() -> new Item(defP().food(food(2, 0.6))), "caliberry_large_slice_grilled");
		caliberryHoney = initItem(() -> new ContainerFoodItem(defP().food(food(10, 0.4)).craftRemainder(Items.STICK)), "honey_glazed_grilled_caliberry_stick");
		caliberryIce = initItem(() -> new ContainerFoodItem(defP().food(food(8, 0.4)).craftRemainder(Items.BOWL)), "caliberry_ice_cream");
		
		livingHerb = initItem(() -> new Item(defP()), "living_herb");
		
		cowbonesHorn = initItem(() -> new Item(defP()), "cowbones_horn");
		
		milspecIce = initItem(() -> new Item(defP()), "milspec_ice");
		milspecIceGrilled = initItem(() -> new Item(defP().food(food(4, 0.4))), "milspec_ice_grilled");

		damagedDevice = initItem(() -> new Item(defP()), "damaged_device");
		
		heptacle = initItem(() -> new Item(defP()), "heptacle");
		
		gravioliumCell = initItem(() -> new Item(defP()), "graviolium_cell");
	}

	public static Item.Properties defP() {
		return new Item.Properties();
	}

	public static Item.Properties noStack() {
		return new Item.Properties().stacksTo(1);
	}
	
	public static FoodProperties food(int hunger, double saturation) {
		return (new FoodProperties.Builder()).nutrition(hunger).saturationMod((float)saturation).build();
	}

	public static <I extends Item> RegistryObject<I> initItem(Supplier<I> item, String name) {
		REG.register(name, item);
		RegistryObject<I> rego = RegistryObject.create(ByPowderAndSteel.rl(name), ForgeRegistries.ITEMS);
		orderedItemsCreative.add(rego);
		return rego;
	}
}
