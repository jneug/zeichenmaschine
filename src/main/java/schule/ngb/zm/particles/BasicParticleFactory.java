package schule.ngb.zm.particles;

import schule.ngb.zm.Color;

public class BasicParticleFactory implements ParticleFactory {

	private final Color startColor;

	private final Color finalColor;

	private boolean fadeOut = true;

	public BasicParticleFactory() {
		this(null, null);
	}

	public BasicParticleFactory( Color startColor ) {
		this(startColor, null);
	}

	public BasicParticleFactory( Color startColor, Color finalColor ) {
		this.startColor = startColor;
		this.finalColor = finalColor;
	}

	public void setFadeOut( boolean pFadeOut ) {
		this.fadeOut = pFadeOut;
	}

	@Override
	public Particle createParticle() {
		Color finalClr = finalColor;
		if( fadeOut ) {
			if( finalColor != null ) {
				finalClr = new Color(finalColor, 0);
			} else if( startColor != null ) {
				finalClr = new Color(startColor, 0);
			}
		}
		return new BasicParticle(startColor, finalClr);
	}

}
