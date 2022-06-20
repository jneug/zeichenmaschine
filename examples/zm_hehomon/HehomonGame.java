import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.shapes.Picture;

public class HehomonGame extends Zeichenmaschine {

	public static void main( String[] args ) {
		new HehomonGame();
	}


	private Hehomon attacker, defender;

	private Hitpoints hpAttacker, hpDefender;

	public HehomonGame() {
		super(1024, 768, "ZM: Hehomon");
		//setFullscreen(true);
	}

	@Override
	public void setup() {
		attacker = new Alligung();
		defender = new Salamanyte();

		hpAttacker = new Hitpoints(200, 400, attacker);
		hpDefender = new Hitpoints(width-100, 400, defender);

		shapes.add(new Picture(200, 100, "images/"+attacker.getBild()));
		shapes.add(new Picture(width-200, 100, "images/"+defender.getBild()));
		shapes.add(hpAttacker, hpDefender);
	}

	@Override
	public void update( double delta ) {
	}

	@Override
	public void draw() {
	}

	@Override
	public void keyPressed() {
		attacker.nimmSchaden(random(0, 10));
		defender.nimmSchaden(random(0, 10));
	}

}
