import schule.ngb.zm.ImageLayer;
import schule.ngb.zm.Spielemaschine;
import schule.ngb.zm.util.FontLoader;

import java.awt.Font;
import java.lang.reflect.InvocationTargetException;

public class HehomonGame extends Spielemaschine {

	public static void main( String[] args ) {
		new HehomonGame();
	}


	private Hehomon attacker, defender;

	private Hitpoints hpAttacker, hpDefender;

	private Display display;

	private Menu menu;

	private boolean idle = true;

	private static final Class[] HEHOMONS = new Class[]{
		Alligung.class, Salamanyte.class, Gardon.class, Mantairy.class, Shigong.class, Toxo.class, Wokachu.class
	};

	public HehomonGame() {
		super(960, 642, "ZM: Hehomon");
		//setFullscreen(true);
	}

	private Hehomon newHehomon() {
		try {
			Class<Hehomon> clazz = (Class<Hehomon>) choice(HEHOMONS);
			return clazz.getConstructor().newInstance();
		} catch( NoSuchMethodException | InstantiationException |
				 IllegalAccessException | InvocationTargetException ex ) {
			exit();
			return null;
		}
	}

	@Override
	public void setup() {
		Font font = FontLoader.loadFont("fonts/FredokaOne-Regular.ttf" );

		ImageLayer bg = new ImageLayer("images/hintergrund.jpg");
		canvas.addLayer(1, bg);



		attacker = newHehomon();
		attacker.moveTo(width * .25, 250);
		attacker.flip(LEFT);
		defender = newHehomon();
		defender.moveTo(width * .75, 250);

		hpAttacker = new Hitpoints(attacker);
		hpDefender = new Hitpoints(defender);

		display = new Display(500, 500);
		display.alignTo(SOUTHEAST, -50.0);

		menu = new Menu(attacker);
		menu.getWidth();
		menu.alignTo(SOUTHWEST, -50.0);

		add(attacker, defender);
		add(hpAttacker, hpDefender);
		add(display, menu);
	}

	public void keyPressed() {
		if( !idle ) {
			return;
		}

		idle = false;
		if( keyCode == KEY_A ) {
			display.setText(attacker.getName() + " benutzt " + attacker.getNameAngr1() + "!");
			delay(500);
			attacker.angriff1(defender);
		} else if( keyCode == KEY_S ) {
			display.setText(attacker.getName() + " benutzt " + attacker.getNameAngr2() + "!");
			delay(500);
			attacker.angriff2(defender);
		} else if( keyCode == KEY_D ) {
			display.setText(attacker.getName() + " benutzt " + attacker.getNameVert1() + "!");
			delay(500);
			attacker.verteidigung1(defender);
		} else if( keyCode == KEY_F ) {
			display.setText(attacker.getName() + " benutzt " + attacker.getNameVert2() + "!");
			delay(500);
			attacker.verteidigung2(defender);
		}

		delay(500);
		pruefeSiegbedingung();
		verteidigerAmZug();
		display.setText(attacker.getName() + " ist am Zug.");
		idle = true;
	}

	private void verteidigerAmZug() {
		int zufall = random(1, 4);
		switch( zufall ) {
			case 1:
				display.setText(defender.getName() + " benutzt " + defender.getNameAngr1() + "!");
				delay(500);
				defender.angriff1(attacker);
				break;
			case 2:
				display.setText(defender.getName() + " benutzt " + defender.getNameAngr2() + "!");
				delay(500);
				defender.angriff2(attacker);
				break;
			case 3:
				display.setText(defender.getName() + " benutzt " + defender.getNameVert1() + "!");
				delay(500);
				defender.verteidigung1(attacker);
				break;
			case 4:
				display.setText(defender.getName() + " benutzt " + defender.getNameVert2() + "!");
				delay(500);
				defender.verteidigung2(attacker);
				break;
		}

		delay(500);
		pruefeSiegbedingung();
	}

	private void pruefeSiegbedingung() {
		if( defender.getLp() <= 0 || true ) {
			//view.angreiferGewinnt();
			display.setText(attacker.getName() + " gewinnt!");
		} else if( attacker.getLp() <= 0 ) {
			//view.verteidigerGewinnt();
			display.setText(defender.getName() + " gewinnt!");
		}
	}

	@Override
	public void fullscreenChanged() {
		if( defender != null ) {
			defender.moveTo(width * .75, 250);
			hpDefender.moveTo(width * .75, defender.getAbsAnchorPoint(DOWN).y + 50);
		}
	}

}
