package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.AbeillonGPEmperorEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AbeillonGPEmperorRenderer extends MobRenderer<AbeillonGPEmperorEntity, AbeillonModel<AbeillonGPEmperorEntity>> {
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("abeillon_great_purple_emperor"), "main");
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/abeillon_great_purple_emperor.png");

	public AbeillonGPEmperorRenderer(EntityRendererProvider.Context context) {
		super(context, new AbeillonModel<>(context.bakeLayer(MODEL)), 0.8f);
	}

	@Override
	public ResourceLocation getTextureLocation(AbeillonGPEmperorEntity entity) {
		return TEXTURE;
	}

	@Override
	protected float getFlipDegrees(AbeillonGPEmperorEntity entity) {
		return 180;
	}

	@Override
	protected void scale(AbeillonGPEmperorEntity entity, PoseStack pose, float partialTick) {
		pose.scale(1.5f, 1.5f, 1.5f);
	}
}
