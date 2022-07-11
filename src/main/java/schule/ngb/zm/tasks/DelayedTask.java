package schule.ngb.zm.tasks;

import schule.ngb.zm.Zeichenmaschine;

import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public abstract class DelayedTask extends Task implements Delayed {

	protected long startTime = System.currentTimeMillis(); // in ms

	/**
	 * Gibt die absolute Verzögerung der Task zurück. Im Gegensatz zu
	 * {@link #getDelay(TimeUnit)} sollte das Ergebnis von {@code getDelay()}
	 * bei mehrmaligem Aufruf konstant bleiben.
	 *
	 * @return Die ursprüngliche Verzögerung in Millisekunden
	 */
	public abstract int getDelay();

	public long getStartTime() {
		return startTime + getDelay();
	}

	/**
	 * Gibt die verbleibende Verzögerung bis zur Ausführung der Task zurück. Im
	 * Gegensatz zu {@link #getDelay()} sollte für mehrere Aufrufe von
	 * {@code getDelay(TimeUnit)} gelten, dass der zeitlich spätere Aufruf einen
	 * kleineren Wert zurückgibt, als der Frühere (abhängig von der gewählten
	 * {@link TimeUnit}).
	 *
	 * @param unit Die Zeiteinheit für die Verzögerung.
	 * @return Die verbleibende Verzögerung in der angegebenen Zeiteinheit.
	 */
	@Override
	public long getDelay( TimeUnit unit ) {
		int diff = (int) (getStartTime() - System.currentTimeMillis());
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	}

	@Override
	public int compareTo( Delayed o ) {
		return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
	}

	@Override
	public void run() {
		while( getDelay(TimeUnit.MILLISECONDS) > 0 ) {
			try {
				System.out.println("Waiting for " + getDelay(TimeUnit.MILLISECONDS) + " ms");
				Thread.sleep(getDelay(TimeUnit.MILLISECONDS));
				//wait(getDelay(TimeUnit.MILLISECONDS));
			} catch( InterruptedException e ) {
				// Keep waiting
			}
		}

		initialize();

		running = true;
		this.update(0.0);
		running = false;
		done = true;

		finish();
	}

}
