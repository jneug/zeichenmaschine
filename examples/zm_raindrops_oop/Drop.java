import schule.ngb.zm.Updatable;
import schule.ngb.zm.shapes.Picture;

public class Drop extends Picture implements Updatable {

	public static final double SPEED_PIXELPERSECOND = 100.0;

	public static final double SPEED_INCREASE = 1.2;

	public static final int START_Y = 40;


	private double speed = SPEED_PIXELPERSECOND;

	private boolean active = false;

	public Drop() {
		super(0, 0, "raindrop.png");
		hide();
	}

	public void increaseSpeed() {
		this.speed *= SPEED_INCREASE;
	}

	public void activate() {
		this.active = true;
	}

	public void reset() {
		moveTo(random(Raindrops.GAME_BORDER, Raindrops.GAME_WIDTH - Raindrops.GAME_BORDER), START_Y);
		show();
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void update( double delta ) {
		y += speed * delta;
	}

}
