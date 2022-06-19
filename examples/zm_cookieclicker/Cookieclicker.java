import schule.ngb.zm.GraphicsLayer;
import schule.ngb.zm.Options;
import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.shapes.Picture;
import schule.ngb.zm.shapes.Text;

import java.util.ArrayList;

public class Cookieclicker extends Zeichenmaschine {

	public static void main( String[] args ) {
		new Cookieclicker();
	}

	private int cookies = 0;

	private int cookiesPerClick = 1;

	private int grandmas = 0;

	private int autoclicker = 12, autoclickerTimer = 0, autoClickerDelay = 600;

	private Text tCookies, tShopGrandma, tShopAutoclicker;

	private Picture pCookie;

	private boolean cookieDown = false;

	public Cookieclicker() {
		super(1280, 900, "Cookieclicker");
	}

	@Override
	public void setup() {
		pCookie = new Picture(width / 2, height / 2, "assets/cookie.png");
		pCookie.scale(.5);
		shapes.add(pCookie);

		tCookies = new Text(width - 60, 60, "" + cookies);
		tCookies.setAnchor(NORTHEAST);
		tCookies.setStrokeColor(255);
		tCookies.setFontsize(36);
		shapes.add(tCookies);

		background.setColor(0);
	}

	@Override
	public void update( double delta ) {
		tCookies.setText("" + cookies);

		autoclickerTimer -= (int)(delta * 1000.0);
		if( autoclickerTimer <= 0 ) {
			cookies += autoclicker;
			autoclickerTimer += autoClickerDelay;
		}

		synchronized( particles ) {
			ArrayList<NumberParticle> remove = new ArrayList<>();
			for( NumberParticle p : particles ) {
				if( p.isActive() ) {
					p.update(delta);
				} else {
					remove.add(p);
				}
			}
			for( NumberParticle p : remove ) {
				particles.remove(p);
			}
		}
	}

	@Override
	public void draw() {
	}

	ArrayList<NumberParticle> particles = new ArrayList<>();

	@Override
	public void mousePressed() {
		if( pCookie.getBounds().contains(mouseX, mouseY) ) {
			cookieDown = true;
			cookies += cookiesPerClick;
			pCookie.scale(.95);

			synchronized( particles ) {
				NumberParticle p = new NumberParticle(mouseX, mouseY, cookiesPerClick);
				particles.add(p);
				shapes.add(p);
			}
		}
	}

	@Override
	public void mouseReleased() {
		if( cookieDown ) {
			pCookie.scale(1 / .95);
		}
	}

}
