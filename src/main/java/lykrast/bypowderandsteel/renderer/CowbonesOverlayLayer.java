package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class CowbonesOverlayLayer<T extends Mob & RangedAttackMob, M extends EntityModel<T>> extends RenderLayer<T, M> {
	//copied from the stray
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/cowbones_overlay.png");
	private final SkeletonModel<T> layerModel;

	public CowbonesOverlayLayer(RenderLayerParent<T, M> layer, EntityModelSet modelset) {
		super(layer);
		layerModel = new SkeletonModel<>(modelset.bakeLayer(CowbonesModel.OVERLAY));
	}

	@Override
	public void render(PoseStack posestack, MultiBufferSource buffer, int p_117555_, T p_117556_, float p_117557_, float p_117558_, float p_117559_, float p_117560_, float p_117561_,
			float p_117562_) {
		coloredCutoutModelCopyLayerRender(this.getParentModel(), layerModel, TEXTURE, posestack, buffer, p_117555_, p_117556_, p_117557_, p_117558_, p_117560_, p_117561_,
				p_117562_, p_117559_, 1.0F, 1.0F, 1.0F);
	}
}
