package lykrast.bypowderandsteel.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.item.ContainerFoodItem;
import lykrast.bypowderandsteel.item.KnockbackBulletItem;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.item.GunItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class BPASItems {	
	public static final DeferredRegister<Item> REG = DeferredRegister.create(ForgeRegistries.ITEMS, ByPowderAndSteel.MODID);
	public static RegistryObject<GunItem> gunsteelGun;
	public static RegistryObject<BulletItem> gunsteelBullet, caliberry, caliberryLarge;
	public static RegistryObject<Item> gunsteelScrap, gunsteelIngot, gunsteelNugget;
	public static RegistryObject<Item> caliberryGrilled, caliberryHoney, caliberrySlice, caliberrySliceGrilled, caliberryIce;
	public static RegistryObject<Item> livingHerb;
	public static RegistryObject<Item> milspecIce, milspecIceGrilled;
	public static RegistryObject<Item> heptacle;
	
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
		gunsteelGun = initItem(() -> new GunItem(defP().durability(502), 0, 1, 16, 2, 14).repair(() -> Ingredient.of(Tags.Items.INGOTS_IRON)), "gunsteel_gun");
		
		//Bullets
		gunsteelBullet = initItem(() -> new BulletItem(defP(), 6), "gunsteel_bullet");
		caliberry = initItem(() -> new KnockbackBulletItem(defP().food(food(1, 0.6)), 4, 1), "caliberry");
		caliberryLarge = initItem(() -> new KnockbackBulletItem(defP().food(food(4, 0.6)), 6, 2), "caliberry_large");
		
		//Materials
		gunsteelScrap = initItem(() -> new Item(defP()), "gunsteel_scrap");
		gunsteelIngot = initItem(() -> new Item(defP()), "gunsteel_ingot");
		gunsteelNugget = initItem(() -> new Item(defP()), "gunsteel_nugget");

		caliberryGrilled = initItem(() -> new Item(defP().food(food(2, 0.6))), "caliberry_grilled");
		caliberryHoney = initItem(() -> new ContainerFoodItem(defP().food(food(10, 0.4)).craftRemainder(Items.STICK)), "honey_glazed_grilled_caliberry_stick");
		caliberrySlice = initItem(() -> new Item(defP().food(food(1, 0.6))), "caliberry_large_slice");
		caliberrySliceGrilled = initItem(() -> new Item(defP().food(food(2, 0.6))), "caliberry_large_slice_grilled");
		caliberryIce = initItem(() -> new ContainerFoodItem(defP().food(food(8, 0.4)).craftRemainder(Items.BOWL)), "caliberry_ice_cream");
		
		livingHerb = initItem(() -> new Item(defP()), "living_herb");
		
		milspecIce = initItem(() -> new Item(defP()), "milspec_ice");
		milspecIceGrilled = initItem(() -> new Item(defP().food(food(4, 0.4))), "milspec_ice_grilled");
		
		heptacle = initItem(() -> new Item(defP()), "heptacle");
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
