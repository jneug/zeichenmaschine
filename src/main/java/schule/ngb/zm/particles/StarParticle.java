package schule.ngb.zm.particles;

import schule.ngb.zm.Color;

import java.awt.Graphics2D;

public class StarParticle extends BasicParticle {

	public StarParticle() {
		super();
		this.startColor = Color.PURE_GREEN;
	}

	public StarParticle( Color startColor ) {
		this(startColor, null);
	}

	public StarParticle( Color startColor, Color finalColor ) {
		super();

		this.color = startColor;
		this.startColor = startColor;
		this.finalColor = finalColor;
	}

	@Override
	public void draw( Graphics2D graphics ) {
		if( isActive() && this.color != null ) {
			graphics.setColor(this.color.getJavaColor());
			graphics.drawLine((int) position.x - 3, (int) position.y - 3, (int) position.x + 3, (int) position.y + 3);
			graphics.drawLine((int) position.x + 3, (int) position.y - 3, (int) position.x - 3, (int) position.y + 3);
		}
	}

}
