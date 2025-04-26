package lykrast.bypowderandsteel.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.item.*;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.item.GatlingItem;
import lykrast.gunswithoutroses.item.GunItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import lykrast.gunswithoutroses.registry.GWRSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class BPASItems {	
	public static final DeferredRegister<Item> REG = DeferredRegister.create(ForgeRegistries.ITEMS, ByPowderAndSteel.MODID);
	//guns
	public static RegistryObject<GunItem> gunsteelGun, peashooter, cornGatling, desertRevolver, desertShotgun, arcticPistol, arcticSniper, buccaneerFlintlock, buccaneerCannon, raygun;
	public static RegistryObject<GunItem> cornGatlingDiamond, desertShotgunDiamond, arcticSniperDiamond, buccaneerCannonDiamond;
	//bullets
	public static RegistryObject<BulletItem> gunsteelBullet, caliberry, caliberryLarge, phaseBullet, graviticBullet;
	//swords
	public static RegistryObject<SwordItem> buccaneerCutlass, phasesaber, phasesaberGravitic;
	//armor
	public static ArmorMaterial marauder, sentry;
	public static RegistryObject<ArmorItem> marauderHelmet, marauderChestplate, marauderLeggings, marauderBoots;
	public static RegistryObject<ArmorItem> sentryHelmet, sentryChestplate, sentryLeggings, sentryBoots;
	//spawn eggs
	public static RegistryObject<Item> gunomeEgg, shrubhulkEgg, shrubsnapperEgg, cowbonesPistoleroEgg, cowbonesBuckarooEgg,
		zombieSealEgg, sunkenPirateEgg, sabersentryEgg, blastersentryEgg, gunnubusCrimsonEgg, skybenderEgg;
	//materials and food
	//global
	public static RegistryObject<Item> gunsteelScrap, gunsteelIngot, gunsteelNugget, assemblyBasic, assemblyDiamond, gunsmithingTemplate;
	//forest
	public static RegistryObject<Item> caliberryGrilled, caliberrySlice, caliberrySliceGrilled, caliberryHoney, caliberryIce;
	public static RegistryObject<Item> livingHerb;
	//desert
	public static RegistryObject<Item> cowbonesHorn, marauderPatch;
	//tundra
	public static RegistryObject<Item> milspecIce, milspecIceGrilled;
	//ocean
	public static RegistryObject<Item> sunkenDoubloon;
	//underground
	public static RegistryObject<Item> damagedDevice, sentryPlating, batterySoda, bilkshake;
	//nether
	public static RegistryObject<Item> heptacle;
	//end
	public static RegistryObject<Item> gravioliumCell, graviticEngine;
	
	private static List<RegistryObject<? extends Item>> orderedItemsCreative = new ArrayList<>();
	
	public static void makeCreativeTab(RegisterEvent event) {
		event.register(Registries.CREATIVE_MODE_TAB, helper -> {
			helper.register(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ByPowderAndSteel.rl("bypowderandsteel")),
					CreativeModeTab.builder().title(Component.translatable("itemGroup.bypowderandsteel")).icon(() -> new ItemStack(desertShotgun.get()))
							.displayItems((parameters, output) -> {
								orderedItemsCreative.forEach(i -> output.accept(i.get()));
								BPASBlocks.orderedBlockItems.forEach(i -> output.accept(i.get()));
							}).build());
		});
	}
	
	static {
		//Guns
		//TODO sounds
		gunsteelGun = initItem(() -> new GunItem(defP().durability(502), 0, 1, 16, 2, 16).repair(() -> Ingredient.of(ItemTags.create(new ResourceLocation("forge", "ingots/gunsteel")))), "gunsteel_gun");
		peashooter = initItem(() -> new GunItem(defP().durability(924), 0, 1, 16, 2, 20).chanceFreeShot(0.25).repair(() -> Ingredient.of(livingHerb.get())), "peashooter");
		cornGatling = initItem(() -> new GatlingItem(defP().durability(924), 0, 0.5, 5, 5, 20).chanceFreeShot(0.5).repair(() -> Ingredient.of(livingHerb.get())), "corn_gatling");
		cornGatlingDiamond = initItem(() -> new GatlingItem(defP().durability(3739), 0, 0.5, 3.5, 5, 20).chanceFreeShot(0.5).repair(() -> Ingredient.of(Tags.Items.GEMS_DIAMOND)), "corn_gatling_diamond");
		desertRevolver = initItem(() -> new GunItem(defP().durability(753), 0, 1, 13, 4, 14).repair(() -> Ingredient.of(cowbonesHorn.get())), "desert_revolver");
		desertShotgun = initItem(() -> new GunItem(defP().durability(753), 0, 0.5, 18, 6, 14).projectiles(3).fireSound(GWRSounds.shotgun).repair(() -> Ingredient.of(cowbonesHorn.get())), "desert_shotgun");
		desertShotgunDiamond = initItem(() -> new GunItem(defP().durability(3047), 0, 0.6, 16, 6, 14).projectiles(3).fireSound(GWRSounds.shotgun).repair(() -> Ingredient.of(Tags.Items.GEMS_DIAMOND)), "desert_shotgun_diamond");
		arcticPistol = initItem(() -> new GunItem(defP().durability(630), 1, 1, 18, 1, 8).repair(() -> Ingredient.of(milspecIce.get())), "arctic_pistol");
		arcticSniper = initItem(() -> new GunItem(defP().durability(630), 2, 1, 24, 0, 8).headshotMult(1.5).fireSound(GWRSounds.sniper).repair(() -> Ingredient.of(milspecIce.get())), "arctic_sniper");
		arcticSniperDiamond = initItem(() -> new GunItem(defP().durability(2549), 3, 1, 20, 0, 8).headshotMult(1.5).fireSound(GWRSounds.sniper).repair(() -> Ingredient.of(Tags.Items.GEMS_DIAMOND)), "arctic_sniper_diamond");
		buccaneerFlintlock = initItem(() -> new GunItem(defP().durability(687), 2, 1.25, 25, 4, 12).repair(() -> Ingredient.of(cowbonesHorn.get())), "buccaneer_flintlock");
		buccaneerCannon = initItem(() -> new GunItem(defP().durability(687), 2, 2, 35, 6, 12).fireSound(GWRSounds.shotgun).repair(() -> Ingredient.of(cowbonesHorn.get())), "buccaneer_cannon");
		buccaneerCannonDiamond = initItem(() -> new GunItem(defP().durability(2780), 2, 3, 35, 6, 12).fireSound(GWRSounds.shotgun).repair(() -> Ingredient.of(Tags.Items.GEMS_DIAMOND)), "buccaneer_cannon_diamond");
		raygun = initItem(() -> new RaygunItem(defP().durability(1053), 2, 1, 20, 0.75, 10).repair(() -> Ingredient.of(damagedDevice.get())), "raygun");
		
		//Bullets
		gunsteelBullet = initItem(() -> new BulletItem(defP(), 6), "gunsteel_bullet");
		caliberry = initItem(() -> new KnockbackBulletItem(defP().food(food(1, 0.6)), 4, 1), "caliberry");
		caliberryLarge = initItem(() -> new KnockbackBulletItem(defP().food(food(4, 0.6)), 6, 2), "caliberry_large");
		phaseBullet = initItem(() -> new SlowBulletItem(defP(), 8), "phase_bullet");
		graviticBullet = initItem(() -> new GraviticBulletItem(defP(), 8), "gravitic_bullet");
		
		//Swords
		//iron but more durable
		Tier cutlass = new ForgeTier(2, 687, 6, 2, 14, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Tags.Items.INGOTS_IRON));
		buccaneerCutlass = initItem(() -> new SwordItem(cutlass, 3, -2.4F, defP()), "buccaneer_cutlass");
		//diamond but a lil more durable (will have different upgrades than netherite), and also star wars came out in 1977, neat uh?
		Tier phase = new ForgeTier(3, 1977, 8, 3, 10, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(Tags.Items.GEMS_DIAMOND));
		phasesaber = initItem(() -> new SwordItem(phase, 3, -2.4F, defP()), "phasesaber");
		phasesaberGravitic = initItem(() -> new GraviticSwordItem(phase, 3, -2.4F, defP()), "phasesaber_gravitic");
		
		//Armor
		//TODO sounds
		//similar to leather
		marauder = new CustomArmorMaterial("marauder", 7, Map.of(
				ArmorItem.Type.HELMET, 1,
				ArmorItem.Type.CHESTPLATE, 3,
				ArmorItem.Type.LEGGINGS, 2,
				ArmorItem.Type.BOOTS, 1), 20, () -> SoundEvents.ARMOR_EQUIP_LEATHER, () -> marauderPatch.get(), 0, 0);
		marauderHelmet = initItem(() -> new AttributeArmorItem(marauder, ArmorItem.Type.HELMET, defP())
				.attribute(GWRAttributes.dmgBase, new AttributeModifier(BPASUtils.armorUUID(ArmorItem.Type.HELMET), "gundmg", 0.5, AttributeModifier.Operation.ADDITION))
				.done(), "marauder_helmet");
		marauderChestplate = initItem(() -> new AttributeArmorItem(marauder, ArmorItem.Type.CHESTPLATE, defP())
				.attribute(GWRAttributes.dmgBase, new AttributeModifier(BPASUtils.armorUUID(ArmorItem.Type.CHESTPLATE), "gundmg", 1, AttributeModifier.Operation.ADDITION))
				.done(), "marauder_chestplate");
		marauderLeggings = initItem(() -> new AttributeArmorItem(marauder, ArmorItem.Type.LEGGINGS, defP())
				.attribute(GWRAttributes.dmgBase, new AttributeModifier(BPASUtils.armorUUID(ArmorItem.Type.LEGGINGS), "gundmg", 1, AttributeModifier.Operation.ADDITION))
				.done(), "marauder_leggings");
		marauderBoots = initItem(() -> new AttributeArmorItem(marauder, ArmorItem.Type.BOOTS, defP())
				.attribute(GWRAttributes.dmgBase, new AttributeModifier(BPASUtils.armorUUID(ArmorItem.Type.BOOTS), "gundmg", 0.5, AttributeModifier.Operation.ADDITION))
				.done(), "marauder_boots");
		//similar to iron
		sentry = new CustomArmorMaterial("sentry", 17, Map.of(
				ArmorItem.Type.HELMET, 2,
				ArmorItem.Type.CHESTPLATE, 6,
				ArmorItem.Type.LEGGINGS, 5,
				ArmorItem.Type.BOOTS, 2), 7, () -> SoundEvents.ARMOR_EQUIP_IRON, () -> sentryPlating.get(), 0, 0);
		sentryHelmet = initItem(() -> new AttributeArmorItem(sentry, ArmorItem.Type.HELMET, defP())
				.attribute(GWRAttributes.spread, new AttributeModifier(BPASUtils.armorUUID(ArmorItem.Type.HELMET), "gunspread", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL))
				.done(), "sentry_helmet");
		sentryChestplate = initItem(() -> new AttributeArmorItem(sentry, ArmorItem.Type.CHESTPLATE, defP())
				.attribute(GWRAttributes.chanceUseAmmo, new AttributeModifier(BPASUtils.armorUUID(ArmorItem.Type.CHESTPLATE), "ammosave", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL))
				.done(), "sentry_chestplate");
		sentryLeggings = initItem(() -> new AttributeArmorItem(sentry, ArmorItem.Type.LEGGINGS, defP())
				.attribute(GWRAttributes.fireDelay, new AttributeModifier(BPASUtils.armorUUID(ArmorItem.Type.LEGGINGS), "gunspd", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL))
				.done(), "sentry_leggings");
		sentryBoots = initItem(() -> new AttributeArmorItem(sentry, ArmorItem.Type.BOOTS, defP())
				.attribute(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(BPASUtils.armorUUID(ArmorItem.Type.BOOTS), "Armor knockback resistance", 0.3, AttributeModifier.Operation.ADDITION))
				.done(), "sentry_boots");
		
		//Spawn Eggs
		gunomeEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.gunome, 0x1F6878, 0xDB2F4C, defP()), "gunome_spawn_egg");
		shrubhulkEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.shrubhulk, 0x67A124, 0x4A381E, defP()), "shrubhulk_spawn_egg");
		shrubsnapperEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.shrubsnapper, 0x528919, 0x3F311D, defP()), "shrubsnapper_spawn_egg");
		cowbonesPistoleroEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.cowbonesPistolero, 0xEED9B3, 0x994141, defP()), "cowbones_pistolero_spawn_egg");
		cowbonesBuckarooEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.cowbonesBuckaroo, 0xEED9B3, 0xD7AB4D, defP()), "cowbones_buckaroo_spawn_egg");
		zombieSealEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.zombieSeal, 0x3E475B, 0x698E45, defP()), "zombie_seal_spawn_egg");
		sunkenPirateEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.sunkenPirate, 0x56847E, 0x65E0DD, defP()), "sunken_pirate_spawn_egg");
		sabersentryEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.sabersentry, 0xF4F4E1, 0x383635, defP()), "sabersentry_spawn_egg");
		blastersentryEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.blastersentry, 0xAAA7A6, 0xF74572, defP()), "blastersentry_spawn_egg");
		gunnubusCrimsonEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.gunnubusCrimson, 0xDB5952, 0x4F4A47, defP()), "gunnubus_crimson_spawn_egg");
		skybenderEgg = initItem(() -> new ForgeSpawnEggItem(BPASEntities.skybender, 0xAAA7A6, 0x71BBF0, defP()), "skybender_spawn_egg");
		
		//Materials
		gunsteelScrap = initItem(() -> new Item(defP()), "gunsteel_scrap");
		gunsteelIngot = initItem(() -> new Item(defP()), "gunsteel_ingot");
		gunsteelNugget = initItem(() -> new Item(defP()), "gunsteel_nugget");
		assemblyBasic = initItem(() -> new Item(defP()), "assembly_basic");
		assemblyDiamond = initItem(() -> new Item(defP()), "assembly_diamond");
		gunsmithingTemplate = initItem(() -> new Item(defP()), "gunsmithing_template");

		caliberryGrilled = initItem(() -> new Item(defP().food(food(2, 0.6))), "caliberry_grilled");
		caliberrySlice = initItem(() -> new Item(defP().food(food(1, 0.6))), "caliberry_large_slice");
		caliberrySliceGrilled = initItem(() -> new Item(defP().food(food(2, 0.6))), "caliberry_large_slice_grilled");
		caliberryHoney = initItem(() -> new ContainerFoodItem(defP().food(food(10, 0.4)).craftRemainder(Items.STICK)), "honey_glazed_grilled_caliberry_stick");
		caliberryIce = initItem(() -> new ContainerFoodItem(defP().food(food(8, 0.4)).craftRemainder(Items.BOWL)), "caliberry_ice_cream");
		
		livingHerb = initItem(() -> new Item(defP()), "living_herb");
		
		cowbonesHorn = initItem(() -> new Item(defP()), "cowbones_horn");
		marauderPatch = initItem(() -> new Item(defP()), "marauder_leather_patch");
		
		milspecIce = initItem(() -> new Item(defP()), "milspec_ice");
		milspecIceGrilled = initItem(() -> new Item(defP().food(food(4, 0.4))), "milspec_ice_grilled");
		
		sunkenDoubloon = initItem(() -> new Item(defP()), "sunken_doubloon");

		damagedDevice = initItem(() -> new Item(defP()), "damaged_device");
		sentryPlating = initItem(() -> new Item(defP()), "sentry_fiber_plating");
		batterySoda = initItem(() -> new DrinkItem(defP().food(food(4, 0.4))), "battery_soda");
		bilkshake = initItem(() -> new BilkshakeItem(defP().food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.4f).alwaysEat().build())), "bilkshake");
		
		heptacle = initItem(() -> new Item(defP()), "heptacle");
		
		gravioliumCell = initItem(() -> new Item(defP()), "graviolium_cell");
		graviticEngine = initItem(() -> new Item(defP()), "gravitic_engine");
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
