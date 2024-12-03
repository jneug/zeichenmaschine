package schule.ngb.zm.particles;

import schule.ngb.zm.Color;

public class BasicParticleFactory implements ParticleFactory {


	private final Color startColor;

	private final Color finalColor;

	private final int maxLifetime = 50;

	public BasicParticleFactory() {
		this.startColor = new Color(128, 128, 129);
		this.finalColor = new Color(128, 128, 129, 0);
	}

	public BasicParticleFactory( Color startColor, Color finalColor ) {
		this.startColor = startColor;
		this.finalColor = finalColor;
	}

	public int getMaxLifetime() {
		return maxLifetime;
	}

	@Override
	public Particle createParticle() {
		return new BasicParticle(maxLifetime, startColor, finalColor);
	}

}
