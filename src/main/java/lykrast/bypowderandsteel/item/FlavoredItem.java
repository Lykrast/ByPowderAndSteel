package lykrast.bypowderandsteel.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class FlavoredItem extends Item {
	//TODO using the illager font as a test
	private static final ResourceLocation ALT_FONT = new ResourceLocation("minecraft", "illageralt");
	private static final Style ROOT_STYLE = Style.EMPTY.withFont(ALT_FONT).withItalic(true).withColor(ChatFormatting.GRAY);

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
		tooltip.add(Component.literal("By Powder and Steel i carry Her Words").withStyle(ChatFormatting.GRAY).withStyle(ROOT_STYLE));
	}

}
