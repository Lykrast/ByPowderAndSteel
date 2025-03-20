package lykrast.bypowderandsteel.item;

import java.util.function.Supplier;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class AttributeArmorItem extends ArmorItem {
	//so the attribute map is private, final, and immutable with no hook to add stuff to it
	//so we do a lil duplication so we can add our attributes
	private Multimap<Attribute, AttributeModifier> newAttributes;
	private ImmutableMultimap.Builder<Attribute, AttributeModifier> builder;

	public AttributeArmorItem(ArmorMaterial material, Type type, Properties properties) {
		super(material, type, properties);
		builder = ImmutableMultimap.builder();
		//this should get us the armoritem map
		builder.putAll(super.getDefaultAttributeModifiers(type.getSlot()));
	}

	public AttributeArmorItem attribute(Attribute attribute, AttributeModifier modifier) {
		if (builder != null) builder.put(attribute, modifier);
		return this;
	}

	public AttributeArmorItem attribute(Supplier<Attribute> attribute, AttributeModifier modifier) {
		if (builder != null) builder.put(attribute.get(), modifier);
		return this;
	}

	public AttributeArmorItem done() {
		newAttributes = builder.build();
		//let it be garbage collected
		builder = null;
		return this;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == type.getSlot() ? newAttributes : super.getDefaultAttributeModifiers(slot);
	}

}
