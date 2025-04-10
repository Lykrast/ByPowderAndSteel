package lykrast.bypowderandsteel.renderer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class SunkenPirateModel<T extends Mob> extends GunnubusModel<T> {
	public SunkenPirateModel(ModelPart modelpart) {
		super(modelpart);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		//Swimming from Drowned
		if (swimAmount > 0.0F) {
			rightArm.xRot = rotlerpRad(swimAmount, rightArm.xRot, -2.5132742F) + swimAmount * 0.35F * Mth.sin(0.1F * ageInTicks);
			leftArm.xRot = rotlerpRad(swimAmount, leftArm.xRot, -2.5132742F) - swimAmount * 0.35F * Mth.sin(0.1F * ageInTicks);
			rightArm.zRot = rotlerpRad(swimAmount, rightArm.zRot, -0.15F);
			leftArm.zRot = rotlerpRad(swimAmount, leftArm.zRot, 0.15F);
			leftLeg.xRot -= swimAmount * 0.55F * Mth.sin(0.1F * ageInTicks);
			rightLeg.xRot += swimAmount * 0.55F * Mth.sin(0.1F * ageInTicks);
			head.xRot = 0;
		}
	}

}
