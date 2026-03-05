package lykrast.bypowderandsteel.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class InquisitorialMarksbladeItem extends MarksbladeItem {
	//TODO figure out what I want to do with it gameplay-wise

	public InquisitorialMarksbladeItem(Tier tier, int damage, float speed, int markedLevel, Properties props) {
		super(tier, damage, speed, markedLevel, props);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		//Plain text and not localized because it's not meant to be readable (at least not without decoding the cipher)
		//though the translation is right there I guess so you just got spoiled
		//and there's also many other ways to get spoiled (creative search, looking at the font file, finding font in commands...)
		//I just hope at least one person has fun deciphering it the intended way
		tooltip.add(Component.literal("By my blade me carry # Steel").withStyle(ChatFormatting.GRAY).withStyle(FlavoredItem.STYLE));
	}

}
