package schule.ngb.zm.particles;

import schule.ngb.zm.Color;
import schule.ngb.zm.Vector;

import java.awt.Graphics2D;

public class BasicParticle extends Particle {

	protected Color color, startColor, finalColor;


	public BasicParticle() {
		super();
	}

	public BasicParticle(Color startColor) {
		this(0, startColor, null);
	}

	public BasicParticle(Color startColor, Color finalColor) {
		this(0, startColor, finalColor);
	}

	public BasicParticle( int pLifetime ) {
		super(pLifetime);
	}

	public BasicParticle( int pLifetime, Color startColor, Color finalColor ) {
		super(pLifetime);

		this.color = startColor;
		this.startColor = startColor;
		this.finalColor = finalColor;
	}

	public Color getColor() {
		return color;
	}

	public void setColor( Color pColor ) {
		this.color = pColor;
	}

	public Color getStartColor() {
		return startColor;
	}

	public void setStartColor( Color pStartColor ) {
		this.startColor = pStartColor;
	}

	public Color getFinalColor() {
		return finalColor;
	}

	public void setFinalColor( Color pFinalColor ) {
		this.finalColor = pFinalColor;
	}

	@Override
	public void spawn( Vector pPosition, Vector pVelocity ) {
		super.spawn(pPosition, pVelocity);
		this.color = this.startColor;
	}

	@Override
	public void update( double delta ) {
		super.update(delta);

		if( startColor != null && finalColor != null ) {
			double t = 1.0 - lifetime / maxLifetime;
			this.color = Color.interpolate(startColor, finalColor, t);
		}
	}

	@Override
	public void draw( Graphics2D graphics ) {
		if( this.color != null ) {
			graphics.setColor(this.color.getJavaColor());
			graphics.fillOval((int) position.x, (int) position.y, 6, 6);
		}
	}

}
