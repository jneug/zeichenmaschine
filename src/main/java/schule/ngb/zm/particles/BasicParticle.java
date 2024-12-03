package schule.ngb.zm.particles;

import schule.ngb.zm.Color;
import schule.ngb.zm.Vector;

import java.awt.Graphics2D;

public class BasicParticle extends Particle {

	protected Color color, startColor, finalColor;


	public BasicParticle() {
		super();
	}

	public BasicParticle( Color startColor ) {
		this(startColor, null);
	}

	public BasicParticle( Color startColor, Color finalColor ) {
		super();

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
	public void spawn( int pLifetime, Vector pPosition, Vector pVelocity ) {
		super.spawn(pLifetime, pPosition, pVelocity);
		this.color = this.startColor;
	}

	@Override
	public void update( double delta ) {
		super.update(delta);

		if( isActive() && startColor != null && finalColor != null ) {
			double t = 1.0 - lifetime / maxLifetime;
			this.color = Color.interpolate(startColor, finalColor, t);
		}
	}

	@Override
	public void draw( Graphics2D graphics ) {
		if( isActive() && this.color != null ) {
			graphics.setColor(this.color.getJavaColor());
			graphics.fillOval(position.getIntX() - 3, position.getIntY() - 3, 6, 6);
		}
	}

}
