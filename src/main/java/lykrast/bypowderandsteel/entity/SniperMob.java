package lykrast.bypowderandsteel.entity;

public interface SniperMob extends GunMob {
	//Well it's not all general but that's to cover for SnipeGoal
	int getAttackCooldown();
	void setAttackCooldown(int amount);
	void setActiveAttackTarget(int id);
}
