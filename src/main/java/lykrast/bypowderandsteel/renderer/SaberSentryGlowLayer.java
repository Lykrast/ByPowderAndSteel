package lykrast.bypowderandsteel.renderer;

import java.util.Locale;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.SaberSentryEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SaberSentryGlowLayer extends RenderLayer<SaberSentryEntity, SaberSentryModel> {
	private final RenderType[] renders;
	private final RenderType olivia, aika;

	public SaberSentryGlowLayer(RenderLayerParent<SaberSentryEntity, SaberSentryModel> parent, ResourceLocation baseTexture, String cosmeticPrefix) {
		super(parent);
		//don't forget to go update that constant when adding a new one
		renders = new RenderType[SaberSentryEntity.COSMETICS + 1]; //+1 for the base glow
		renders[0] = RenderType.entityTranslucentEmissive(baseTexture, false);
		renders[1] = rl(cosmeticPrefix, "aroace");
		renders[2] = rl(cosmeticPrefix, "aromantic");
		renders[3] = rl(cosmeticPrefix, "asexual");
		renders[4] = rl(cosmeticPrefix, "bisexual");
		renders[5] = rl(cosmeticPrefix, "disability");
		renders[6] = rl(cosmeticPrefix, "genderfluid");
		renders[7] = rl(cosmeticPrefix, "genderqueer");
		renders[8] = rl(cosmeticPrefix, "intersex");
		renders[9] = rl(cosmeticPrefix, "lesbian");
		renders[10] = rl(cosmeticPrefix, "nonbinary");
		renders[11] = rl(cosmeticPrefix, "omnisexual");
		renders[12] = rl(cosmeticPrefix, "pansexual");
		renders[13] = rl(cosmeticPrefix, "polyamorous");
		renders[14] = rl(cosmeticPrefix, "polysexual");
		renders[15] = rl(cosmeticPrefix, "pride");
		renders[16] = rl(cosmeticPrefix, "progresspride");
		renders[17] = rl(cosmeticPrefix, "transgender"); //I hardly knew her
		renders[18] = rl(cosmeticPrefix, "brazil");
		renders[19] = rl(cosmeticPrefix, "riftnecrodancer"); //it's the stripey thing in the ui
		renders[20] = rl(cosmeticPrefix, "pluraljourneysimple"); //apparently there's not 1 plural flag but friend used this one and it looked neat
		renders[21] = rl(cosmeticPrefix, "panafrican");
		renders[22] = rl(cosmeticPrefix, "ukraine");
		renders[23] = rl(cosmeticPrefix, "neapolitan"); //colors from Papa's Freezeria Deluxe
		renders[24] = rl(cosmeticPrefix, "lesbian2"); //it looked pretty https://archive.is/20181102205136/https://medium.com/@lydiandragon/a-lesbian-flag-for-everyone-cef397b89459
		renders[25] = rl(cosmeticPrefix, "spumoni"); //colors also from Papa's Freezeria Deluxe
		renders[26] = rl(cosmeticPrefix, "palestine");
		renders[27] = rl(cosmeticPrefix, "voidlesbian"); //"/void(girl|doll)? ?lesbian/" by 40796c19-532f-4e42-9c39-74c9b15f572a https://void.lgbt/objects/41d856da-f6cd-4d21-b697-0851beee20da
		//no agender because it's 7 stripes and I only have 6 pixels :(
		olivia = rl(cosmeticPrefix, "olivia"); //friend
		aika = rl(cosmeticPrefix, "aika"); //from Pretty Pretty Please I Don't Want to be a Magical Girl
	}
	
	private RenderType rl(String prefix, String add) {
		return RenderType.entityTranslucentEmissive(ByPowderAndSteel.rl(prefix + add + ".png"), false);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource p_116984_, int p_116985_, SaberSentryEntity entity, float p_116987_, float p_116988_, float p_116989_, float p_116990_, float p_116991_,
			float p_116992_) {
		String name = ChatFormatting.stripFormatting(entity.getName().getString()).toLowerCase(Locale.ROOT);
		VertexConsumer vertexconsumer = p_116984_.getBuffer("olivia".equals(name) ? olivia : "aika".equals(name) ? aika : renders[entity.getCosmetic()]);
		this.getParentModel().renderToBuffer(pose, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}

}
