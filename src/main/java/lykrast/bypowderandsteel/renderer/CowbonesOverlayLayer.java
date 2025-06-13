package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class CowbonesOverlayLayer<T extends Mob & RangedAttackMob, M extends EntityModel<T>> extends RenderLayer<T, M> {
	//copied from the stray
	private final EntityModel<T> layerModel;
	private final ResourceLocation texture;

	public CowbonesOverlayLayer(RenderLayerParent<T, M> layer, EntityModel<T> model, ResourceLocation texture) {
		super(layer);
		layerModel = model;
		this.texture = texture;
	}

	@Override
	public void render(PoseStack posestack, MultiBufferSource buffer, int p_117555_, T p_117556_, float p_117557_, float p_117558_, float p_117559_, float p_117560_, float p_117561_,
			float p_117562_) {
		coloredCutoutModelCopyLayerRender(this.getParentModel(), layerModel, texture, posestack, buffer, p_117555_, p_117556_, p_117557_, p_117558_, p_117560_, p_117561_,
				p_117562_, p_117559_, 1.0F, 1.0F, 1.0F);
	}
}
