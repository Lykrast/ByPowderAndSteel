package lykrast.bypowderandsteel.item;

import java.util.UUID;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class AttributeSwordItem extends SwordItem {
	//Hey look this one is fucking private in the super too, so doing the same as with the armor
	private Multimap<Attribute, AttributeModifier> newAttributes;
	private ImmutableMultimap.Builder<Attribute, AttributeModifier> builder;
	//it's the same I was using for the range on the myf twilight's thorn, so shouldn't overlap as they're both mainhand things
	public static final UUID UUID1 = UUID.fromString("3080eef9-4ab7-4f8b-9a90-774208857fc9");

	public AttributeSwordItem(Tier tier, int damage, float speed, Properties props) {
		super(tier, damage, speed, props);
		builder = ImmutableMultimap.builder();
		//this should get us the sworditem map
		builder.putAll(super.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND));
	}

	public AttributeSwordItem attribute(Attribute attribute, AttributeModifier modifier) {
		if (builder != null) builder.put(attribute, modifier);
		return this;
	}

	public AttributeSwordItem attribute(Supplier<Attribute> attribute, AttributeModifier modifier) {
		if (builder != null) builder.put(attribute.get(), modifier);
		return this;
	}

	public AttributeSwordItem done() {
		newAttributes = builder.build();
		//let it be garbage collected
		builder = null;
		return this;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? newAttributes : super.getDefaultAttributeModifiers(slot);
	}

}
