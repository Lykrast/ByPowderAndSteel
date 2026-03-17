package lykrast.bypowderandsteel.item;

import java.util.List;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class FlavoredItem extends Item {
	public static final ResourceLocation FONT = new ResourceLocation(ByPowderAndSteel.MODID, "izeret");
	public static final Style STYLE = Style.EMPTY.withFont(FONT).withItalic(true).withColor(ChatFormatting.GRAY);

	public FlavoredItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		//Plain text and not localized because it's not meant to be readable (at least not without decoding the cipher)
		//though the translation is right there I guess so you just got spoiled
		//and there's also many other ways to get spoiled (creative search, looking at the font file, finding font in commands...)
		//I just hope at least one person has fun deciphering it the intended way
		if (stack.getFoodProperties(null) != null) {
			//hacky way to distinguish the bread and insignia, will extract that properly like if I need to make more of these items
			//until now that'll do and mostly that hides the translated message to anyone peeking at the item registration
			//(I mean they can easily peek here too but y'know one more step, hopefully prevent unwanted spoilers)
			//"#" used as the libra symbol instead of "Her" as originally intended cause that makes the symbol meaning y'know?
			tooltip.add(Component.literal("To eat is to sustain our Life").withStyle(ChatFormatting.GRAY).withStyle(STYLE));
			tooltip.add(Component.literal("Without Life # Words are lost").withStyle(ChatFormatting.GRAY).withStyle(STYLE));
		}
		else {
			//"me" instead of "I" cause it looks nicer, esp next to "my"
			tooltip.add(Component.literal("By Powder and Steel me carry # Words").withStyle(ChatFormatting.GRAY).withStyle(STYLE));
		}
	}

}
