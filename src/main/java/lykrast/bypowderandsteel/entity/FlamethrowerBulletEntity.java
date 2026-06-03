package lykrast.bypowderandsteel.entity;

import lykrast.bypowderandsteel.registry.BPASEntities;
import lykrast.gunswithoutroses.entity.BulletEntity;
import lykrast.gunswithoutroses.entity.PiercingBulletEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FlamethrowerBulletEntity extends PiercingBulletEntity {
	public FlamethrowerBulletEntity(EntityType<? extends BulletEntity> type, Level level) {
		super(type, level);
		pierce = 25;
		pierceMult = 1;
		//this is a hack to make them always disappear after ~10 ticks, assuming the max lifetime isn't changed from 100
		ticksSinceFired = 100 - 11;
	}

	//uh whoops didn't foresee extending piercing bullet, so have to redo that constructor by hand
	public FlamethrowerBulletEntity(Level level, LivingEntity shooter) {
		this(BPASEntities.flamethrowerBullet.get(), level);
		setOwner(shooter);
		moveTo(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ(), shooter.getYRot(), shooter.getXRot());
		reapplyPosition();
	}

	@Override
	public void shoot(double dirx, double diry, double dirz, float speed, float spread) {
		//like slow bullets
		if (speed > 2) speed = 2;
		super.shoot(dirx, diry, dirz, speed, spread);
	}

	//putting those here so I can tweak easily
	private static final double FRICTION = 0.85, GRAVITY = -0.01;
	public static final float SCALE = 4;

	@Override
	public void tick() {
		super.tick();
		//copying the gravity bullet part
		Vec3 delta = getDeltaMovement();
		//saving one object allocation here by mathing direction instead of doing .mulitiply and stuff
		//I mean would that change much? not sure but it makes me feel better
		setDeltaMovement(new Vec3(delta.x*FRICTION, delta.y*FRICTION-GRAVITY, delta.z*FRICTION));
		//bullet get disappeared below total squared velocity of 0.01 and I didn't put an easy way to bypass that so a little manual check
		if (getDeltaMovement().lengthSqr() < 0.01) setDeltaMovement(getDeltaMovement().add(0, 0.2, 0));
	}

	@Override
	protected HitResult getHitResult() {
		//like the bullet one but we apply the scale
		Vec3 pos = position();
		Vec3 vel = getDeltaMovement();
		Vec3 nextpos = pos.add(vel);
		HitResult hitresult = level().clip(new ClipContext(pos, nextpos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
		//don't clip to blocks if we're a noclip bullet
		if (!noPhysics && hitresult.getType() != HitResult.Type.MISS) nextpos = hitresult.getLocation();

		//actually manually adjusting the size until it feels good
		HitResult hitresult1 = ProjectileUtil.getEntityHitResult(level(), this, pos, nextpos, getBoundingBox().expandTowards(vel).inflate(1), this::canHitEntity, 0.8f);
		if (hitresult1 != null) {
			hitresult = hitresult1;
		}

		return hitresult;
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.SOUL_FIRE_FLAME;
	}
}
