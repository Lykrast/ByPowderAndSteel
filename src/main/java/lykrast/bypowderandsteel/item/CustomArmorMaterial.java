package lykrast.bypowderandsteel.item;

import java.util.Map;
import java.util.function.Supplier;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public class CustomArmorMaterial implements ArmorMaterial {
	//doing it like vanilla but also a bit like botania
	private final String name;
	private final int durabilityMult;
	private final Map<ArmorItem.Type, Integer> armor;
	private final int enchatability;
	private final Supplier<SoundEvent> equipSound;
	private final float toughness;
	private final float kbResist;
	private final Supplier<Item> repairItem;

	public CustomArmorMaterial(String name, int durabilityMult, Map<Type, Integer> armor, int enchatability, Supplier<SoundEvent> equipSound, Supplier<Item> repairItem, float toughness, float kbResist) {
		this.name = ByPowderAndSteel.MODID + ":" + name;
		this.durabilityMult = durabilityMult;
		this.armor = armor;
		this.enchatability = enchatability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.kbResist = kbResist;
		this.repairItem = repairItem;
	}

	@Override
	public int getDurabilityForType(Type slot) {
		//Same multipliers are vanilla (which are in a private map)
		switch (slot) {
			case HELMET:
				return 11*durabilityMult;
			case CHESTPLATE:
				return 16*durabilityMult;
			case LEGGINGS:
				return 15*durabilityMult;
			case BOOTS:
				return 13*durabilityMult;
		}
		return durabilityMult;
	}

	@Override
	public int getDefenseForType(Type slot) {
		return armor.get(slot);
	}

	@Override
	public int getEnchantmentValue() {
		return enchatability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return equipSound.get();
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.of(repairItem.get());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return kbResist;
	}

}
