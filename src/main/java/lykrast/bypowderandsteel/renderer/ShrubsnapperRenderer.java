package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ShrubsnapperEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ShrubsnapperRenderer extends MobRenderer<ShrubsnapperEntity, ShrubsnapperModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/shrubsnapper.png"), GLOW = ByPowderAndSteel.rl("textures/entity/shrubsnapper_glow.png");

	public ShrubsnapperRenderer(EntityRendererProvider.Context context) {
		super(context, new ShrubsnapperModel(context.bakeLayer(ShrubsnapperModel.MODEL)), 0.5f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
	}

	@Override
	protected void scale(ShrubsnapperEntity entity, PoseStack pose, float partialTick) {
		pose.scale(1.25f, 1.25f, 1.25f);
	}

	@Override
	public ResourceLocation getTextureLocation(ShrubsnapperEntity entity) {
		return TEXTURE;
	}
}
