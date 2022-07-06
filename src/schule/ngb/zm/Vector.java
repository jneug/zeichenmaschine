package schule.ngb.zm;

import java.awt.geom.Point2D;

/**
 * Ein zweidimensionaler Vektor.
 * <p>
 * Ein Vektor besteht aus zwei Komponenten {@code x} und {@code y}. Die
 * {@code Vector} Klasse unterstützt alle wesentlichen Vektoroperationen wie
 * {@link #scale(double) Skalierung}, {@link #add(Vector) Addition} oder
 * {@link #length() Längen-} und {@link #distance(Vector) Abstandsberechnung}.
 * Jede Operation, für die ein Parameter (ein anderer Vektor oder ein Skalar)
 * benötigt werden, gibt es in zwei Varianten: als Objektmethode, die das
 * Vektorobjekt selbst verändert und als Klassenmethode, die einen neuen Vektor
 * erzeugt. Die Objektmethoden unterstützen
 * <a href="https://en.wikipedia.org/wiki/Method_chaining">method chaining</a>
 * und geben jeweils das Vektorobjekt selbst zurück.
 * <p>
 * Außerdem werden eine Reihe von Hilfsmethoden implementiert, um
 * {@link #random() Zufallsvektoren} zu erzeugen, oder die
 * {@link #mouse() Mausposition} als Vektorobjekt zu holen.
 * <p>
 * Der Vektor der Zeichenmaschine erweitert die Klasse {@link Point2D} und lässt
 * sich dadurch einfach mit den Klassen des {@link java.awt} Pakets benutzen.
 */
public class Vector extends Point2D.Double {

	/**
	 * Der Nullvektor {@code Vector(0.0, 0.0)}.
	 */
	public static final Vector ZERO = new Vector(0.0, 0.0);

	/**
	 * Vektor der negativen Ordinatenachse {@code Vector(0.0, -1.0)} [oben]
	 */
	public static final Vector UP = new Vector(0, -1.0);

	/**
	 * Vektor der Ordinatenachse {@code Vector(0.0, 1.0)} [unten]
	 */
	public static final Vector DOWN = new Vector(0, 1.0);

	/**
	 * Vektor der Abszissenachse {@code Vector(1.0, 0.0)} [rechts]
	 */
	public static final Vector RIGHT = new Vector(1.0, 0);

	/**
	 * Vektor der negativen Abszissenachse {@code Vector(-1.0, 0.0)} [links]
	 */
	public static final Vector LEFT = new Vector(-1.0, 0.0);

	/**
	 * Vektor mit der aktuellen Mausposition {@code Vector(mouseX, mouseY)}
	 *
	 * @return Einen Vektor mit den Koordinaten des Mauszeigers.
	 */
	public static final Vector mouse() {
		return new Vector(Constants.mouseX, Constants.mouseY);
	}


	/**
	 * Erzeugt den Nullvektor.
	 */
	public Vector() {
		x = 0.0;
		y = 0.0;
	}

	/**
	 * Erzeugt einen Vektor mit den angegebenen Komponenten.
	 *
	 * @param x Die x-Komponente.
	 * @param y Die y-Komponente.
	 */
	public Vector( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Erzeugt einen Vektor mit denselben Werten wie der angegebene Punkt.
	 *
	 * @param point Ein Punkt, dessen Koordinaten kopiert werden.
	 */
	public Vector( Point2D point ) {
		x = point.getX();
		y = point.getY();
	}

	/**
	 * Erzeugt einen Vektor mit zufälligen Werten.
	 * <p>
	 * Die Werte liegen zwischen 0 und 100.
	 *
	 * @return Ein zufälliger Vektor.
	 */
	public static Vector random() {
		return new Vector(Constants.random(100.0), Constants.random(100.0));
	}

	/**
	 * Erzeugt einen Vektor mit zufälligen Werten innerhalb der angegebenen
	 * Grenzen.
	 *
	 * @param min Untere Grenze der Zufallswerte.
	 * @param max Obere Grenze der Zufallswerte.
	 * @return Ein zufälliger Vektor.
	 */
	public static Vector random( double min, double max ) {
		return new Vector(Constants.random(min, max), Constants.random(min, max));
	}

	/**
	 * Erzeugt einen Vektor mit zufälligen Werten innerhalb der angegebenen
	 * Grenzen.
	 *
	 * @param minX Untere Grenze der x-Komponente.
	 * @param maxX Obere Grenze der x-Komponente.
	 * @param minY Untere Grenze der y-Komponente.
	 * @param maxY Obere Grenze der y-Komponente.
	 * @return Ein zufälliger Vektor.
	 */
	public static Vector random( double minX, double maxX, double minY, double maxY ) {
		return new Vector(Constants.random(minX, maxX), Constants.random(minY, maxY));
	}

	/**
	 * Erzeugt einen neuen Vektor mit derselben Richtun wie der angegebene
	 * Vektor und der Länge 1.
	 *
	 * @param vector Der original Vektor.
	 * @return Ein neuer Vektor mit der angegebene Länge.
	 */
	public static Vector normalize( Vector vector ) {
		Vector vec = vector.copy();
		vec.normalize();
		return vec;
	}

	/**
	 * Subtrahiert den zweiten vom ersten Vektor.
	 *
	 * @param vector1 Erster Vektor.
	 * @param vector2 Zweiter Vektor.
	 * @return Differenzvektor der beiden Vektoren.
	 */
	public static Vector sub( Vector vector1, Vector vector2 ) {
		Vector vec = vector1.copy();
		vec.sub(vector2);
		return vec;
	}

	/**
	 * Erzeugt einen neuen Vektor der gleich dem angegebenen Vektor um {code
	 * scalar} skaliert ist.
	 *
	 * @param vector Ein Vektor.
	 * @param scalar Ein Skalar.
	 * @return Ein skalierter Vektor.
	 */
	public static Vector scale( Vector vector, double scalar ) {
		Vector vec = vector.copy();
		vec.scale(scalar);
		return vec;
	}

	/**
	 * Erzeugt einen neuen Vektor der gleich dem angegebenen Vektor durch {code
	 * scalar} dividiert ist.
	 *
	 * @param vector Ein Vektor.
	 * @param scalar Ein Skalar.
	 * @return Ein skalierter Vektor.
	 */
	public static Vector div( Vector vector, double scalar ) {
		Vector vec = vector.copy();
		vec.div(scalar);
		return vec;
	}

	/**
	 * Erzeugt einen neuen Vektor mit denselben Komponenten wie dieses
	 * Vektorobjekt.
	 *
	 * @return Eine Kopie dieses Vektors.
	 */
	public Vector copy() {
		return new Vector(x, y);
	}

	/**
	 * Setzt die Komponenten dieses Vektors neu.
	 *
	 * @param x Der neue x-Wert.
	 * @param y Der neue y-Wert.
	 * @return Dieser Vektor selbst (method chaining)
	 */
	public Vector set( double x, double y ) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Setzt die Komponenten dieses Vectors auf die Werte des angegebenen
	 * Vektors.
	 *
	 * @param vector Ein Vektor.
	 * @return Dieser Vektor selbst (method chaining)
	 */
	public Vector set( Vector vector ) {
		x = vector.x;
		y = vector.y;
		return this;
	}

	/**
	 * Setzt die Komponenten dieses Vectors auf die Werte des angegebenen
	 * Punktes.
	 *
	 * @param pPunkt Ein Punkt.
	 * @return Dieser Vektor selbst (method chaining)
	 */
	public Vector set( Point2D pPunkt ) {
		x = pPunkt.getX();
		x = pPunkt.getY();
		return this;
	}

	/**
	 * Setzt die x-Komponente des Vektors.
	 *
	 * @param x Der neue x-Wert.
	 */
	public void setX( double x ) {
		this.x = x;
	}

	/**
	 * Setzt die y-Komponente des Vektors.
	 *
	 * @param y Der neue y-Wert.
	 */
	public void setY( double y ) {
		this.y = y;
	}

	/**
	 * Berechnet die Länge des Vektors.
	 * <p>
	 * Zur Berechnung der Länge muss eine Quadratwurzel gezogen werden. Dies ist
	 * langsam und kann unter Umständen vermieden werden, wenn mit
	 * {@link #lengthSq()} gearbeitet wird.
	 *
	 * @return Die Länge des Vektors.
	 */
	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Berechnet die quadrierte Länge des Vektors.
	 *
	 * @return Das Quadrat der Länge.
	 */
	public double lengthSq() {
		return x * x + y * y;
	}

	/**
	 * Legt die Länge des Vektors fest.
	 * @param length Die neue Länge des Vektors.
	 * @return Dieser Vektor selbst (method chaining)
	 */
	public Vector setLength( double length ) {
		normalize();
		return scale(length);
	}

	/**
	 * Erzeugt einen neuen Vektor mit derselben Richtung wie der angegebene
	 * Vektor und der Länge {@code length}.
	 *
	 * @param vector Der original Vektor.
	 * @param length Die neue Länge des Vektors.
	 * @return Ein neuer Vektor mit der angegebene Länge.
	 */
	public static Vector setLength( Vector vector, double length ) {
		Vector vec = vector.copy();
		vec.setLength(length);
		return vec;
	}

	/**
	 *
	 * @return Dieser Vektor selbst (method chaining)
	 */
	public Vector normalize() {
		double len = length();
		if( len != 0 && len != 1 ) {
			x /= len;
			y /= len;
		}
		return this;
	}

	/**
	 * Addiert den Vektor {@code vector} zu diesem.
	 * @param vector Ein anderer Vektor.
	 * @return Dieser Vektor selbst (method chaining)
	 */
	public Vector add( Vector vector ) {
		x += vector.x;
		y += vector.y;
		return this;
	}

	/**
	 * Addiert die angegebenen Werte zur x- und y-Komponente des Vektors.
	 * @param x Summand x-Komponente.
	 * @param y Summand y-Komponente.
	 * @return Dieser Vektor selbst (method chaining)
	 */
	public Vector add( double x, double y ) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Addiert die beiden Vektoren zu einem neuen Vektor.
	 *
	 * @param vector1 Erster Vektor.
	 * @param vector2 Zweiter Vektor.
	 * @return Summenvektor der beiden Vektoren.
	 */
	public static Vector add( Vector vector1, Vector vector2 ) {
		Vector vec = vector1.copy();
		vec.add(vector2);
		return vec;
	}

	public Vector sub( Vector vector ) {
		x -= vector.x;
		y -= vector.y;
		return this;
	}

	public Vector sub( double x, double y ) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	public Vector scale( double scalar ) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	public Vector div( double scalar ) {
		if( scalar == 0.0 ) {
			throw new IllegalArgumentException("Can't divide by zero.");
		}
		x /= scalar;
		y /= scalar;
		return this;
	}

	/**
	 * Berechnet den Abstand zum angegebenen Punkt.
	 * <p>
	 * Zur Berechnung der Länge des Differenzvektors muss eine Quadratwurzel
	 * gezogen werden. Um Geschwindigkeitsbuße zu vermeiden und auf den genauen
	 * Abstand verzichtet werden kann, kann unter Umständen
	 * {@link #distanceSq(Vector)} verwendet werden.
	 *
	 * @param vector Ein anderer Vektor.
	 * @return Der Abstand zum Vektor.
	 */
	public double distance( Vector vector ) {
		return super.distance(vector);
	}

	/**
	 * Berechnet den Abstand zwischen den durch die angegebenen Vektoren
	 * beschriebenen Punkten.
	 * <p>
	 * Zur Berechnung der Länge des Differenzvektors muss eine Quadratwurzel
	 * gezogen werden. Um Geschwindigkeitsbuße zu vermeiden und auf den genauen
	 * Abstand verzichtet werden kann, kann unter Umständen
	 * {@link #distanceSq(Vector, Vector)} verwendet werden.
	 *
	 * @param vector1 Der erste Ortsvektor.
	 * @param vector2 Der zweite Ortsvektor.
	 * @return Der Abstand zwischen den Vektoren.
	 */
	public static double distance( Vector vector1, Vector vector2 ) {
		return vector1.distance(vector2);
	}

	/**
	 * Berechnet den quadrierten Abstand zum angegebenen Vektor.
	 * <p>
	 * Das Quadrat des Abstandes ist in der Regel schneller zu berechnen und ist
	 * in vielen Fällen ausreichend. Beispielsweise lassen sich Vergleiche mit
	 * dem quadrierten Abstand durchführen, wenn auch die gewünschte Entfernung
	 * quadriert wird.
	 *
	 * @param vector
	 * @return
	 */
	public double distanceSq( Vector vector ) {
		return super.distanceSq(vector);
	}

	/**
	 * Berechnet den Abstand zum Quadrat zwischen den durch die angegebenen
	 * Vektoren beschriebenen Punkten.
	 * <p>
	 * Das Quadrat des Abstandes ist in der Regel schneller zu berechnen und ist
	 * in vielen Fällen ausreichend. Beispielsweise lassen sich Vergleiche mit
	 * dem quadrierten Abstand durchführen, wenn auch die gewünschte Entfernung
	 * quadriert wird.
	 *
	 * @param vector1 Der erste Ortsvektor.
	 * @param vector2 Der zweite Ortsvektor.
	 * @return Der Abstand zwischen den Vektoren.
	 */
	public static double distanceSq( Vector vector1, Vector vector2 ) {
		return vector1.distance(vector2);
	}

	/**
	 * Bestimmt das Skalarprodukt des Vektors mit dem angegebenen Vektor.
	 *
	 * @param vector Ein zweiter Vektor.
	 * @return Das Skalarprodukt der Vektoren.
	 */
	public double dot( Vector vector ) {
		return x * vector.x + y * vector.y;
	}

	/**
	 * Bestimmt das Skalarprodukt der angegebenen Vektoren.
	 *
	 * @param vector1 Der erste Vektor.
	 * @param vector2 Der zweite Vektor.
	 * @return Das Skalarprodukt der Vektoren.
	 */
	public static double dot( Vector vector1, Vector vector2 ) {
		return vector1.dot(vector2);
	}

	/**
	 * Berechnet das <a
	 * href="https://de.wikipedia.org/wiki/Kreuzprodukt">Kreuzprodukt</a> des
	 * Vektors mit dem angegebenen Vektor.
	 *
	 * @param vector Ein Vektor.
	 * @return Das Kreuzprodukt der Vektoren.
	 */
	public double cross( Vector vector ) {
		// See: http://allenchou.net/2013/07/cross-product-of-2d-vectors/
		return x * vector.y - vector.x * y;
	}

	/**
	 * Berechnet das <a
	 * href="https://de.wikipedia.org/wiki/Kreuzprodukt">Kreuzprodukt</a> der
	 * angegebenen Vektoren.
	 *
	 * @param vector1 Der erste Vektor.
	 * @param vector2 Der zweite Vektor.
	 * @return Das Kreuzprodukt der Vektoren.
	 */
	public static double cross( Vector vector1, Vector vector2 ) {
		return vector1.cross(vector2);
	}

	/**
	 * Beschränkt die Länge dieses Vektors auf den Bereich 0 bis {@code max}.
	 *
	 * @param max Maximale Länge des Vektors.
	 * @return Dieser Vektor selbst (method chaining)
	 * @see #setLength(double)
	 */
	public Vector limit( double max ) {
		if( lengthSq() > max * max ) {
			setLength(max);
		}
		return this;
	}

	/**
	 * Beschränkt die Länge dieses Vektors auf den Bereich {@code min} bis
	 * {@code max}.
	 *
	 * @param min Minimale Länge des Vektors.
	 * @param max Maximale Länge des Vektors.
	 * @return Dieser Vektor selbst (method chaining)
	 * @see #setLength(double)
	 */
	public Vector limit( double min, double max ) {
		if( min > max ) {
			double t = min;
			min = max;
			max = t;
		}

		if( lengthSq() < min * min ) {
			setLength(min);
		} else if( lengthSq() > max * max ) {
			setLength(max);
		}
		return this;
	}

	/**
	 * Bestimmt den Winkel, den der Vektor mit der Ordinatenachse einschließt in
	 * Grad.
	 *
	 * @return Winkel des Vektors in Grad.
	 */
	public double angle() {
		return Math.toDegrees(Math.atan2(y, x));
	}

	/**
	 * Bestimmt den Winkel, den der Vektor mit der Ordinatenachse einschließt in
	 * Bogenmaß.
	 *
	 * @return Winkel des Vektors in Bogenmaß.
	 */
	public double heading() {
		return Math.atan2(y, x);
	}

	/**
	 * Dreht den Vektor um {@code degree} Grad im Uhrzeigersinn.
	 *
	 * @param degree Gradzahl für die Rotation.
	 * @return Dieser Vektor selbst (method chaining)
	 * @see #rotateRad(double)
	 */
	public Vector rotate( double degree ) {
		return rotateRad(Math.toRadians(degree));
	}

	/**
	 * Dreht den Vektor um {@code rad} im Uhrzeigersinn.
	 *
	 * @param rad Winkel der Rotation in Bogenmaß.
	 * @return Dieser Vektor selbst (method chaining)
	 * @see #rotate(double)
	 */
	public Vector rotateRad( double rad ) {
		double temp = x;
		x = x * Math.cos(rad) - y * Math.sin(rad);
		y = temp * Math.sin(rad) + y * Math.cos(rad);
		return this;
	}

	/**
	 * Dreht den Vektor um {@code degree} Grad im Uhrzeigersinn.
	 *
	 * @param vector Ein Vektor.
	 * @param degree Gradzahl für die Rotation.
	 * @return Ein gedrehter Vektor.
	 */
	public static Vector rotate( Vector vector, double degree ) {
		Vector vec = vector.copy();
		vec.rotate(degree);
		return vec;
	}

	/**
	 * Verschiebt diesen Vektor um den Faktor {@code t} in Richtung des Vektors
	 * {@code vector}. Im Vergleich zu {@link #interpolate(Vector, double)} ist
	 * {@code t} auf den Bereich nur zwischen 0 und 1 beschränkt. Für {@code t}
	 * gleich 0 passiert nichts, für {@code t} wird dieser Vektor zu
	 * {@code vector}.
	 *
	 * @param vector Der erste Ortsvektor.
	 * @param t Ein Wert zwischen 0 und 1.
	 * @return Dieser Vektor selbst (method chaining)
	 */
	public Vector morph( Vector vector, double t ) {
		double tt = Math.min(Math.max(t, 0.0), 1.0);
		x = x + (vector.x - x) * tt;
		y = y + (vector.y - y) * tt;
		return this;
	}

	/**
	 * Erzeugt einen Vektor, der auf der Strecke zwischen den angegebenen
	 * Vektoren liegt. Der Ort des Vektors auf der Strecke wird relativ von
	 * {@code vector1} abhängig von {@code t} bestimmt. Im Vergleich zu
	 * {@link #interpolate(Vector, Vector, double)} ist {@code t} auf den
	 * Bereich nur zwischen 0 und 1 beschränkt. Für {@code t} gleich 0 ist der
	 * erzeugte Vektor gleich {@code vector1}, für {@code t} gleich 1 ist das
	 * Ergebnis gleich {@code vector2}. Für {@code t} gleich 0.5 liegt der
	 * Vektor genau mittig zwischen den beiden Ortsvektoren.
	 *
	 * @param vector1 Der erste Ortsvektor.
	 * @param vector2 Der zweite Ortsvektor.
	 * @param t Ein Wert zwischen 0 und 1.
	 * @return Ein Vektor zwischen den beiden Ortsvektoren.
	 */
	public static Vector morph( Vector vector1, Vector vector2, double t ) {
		Vector vec = vector1.copy();
		vec.morph(vector2, t);
		return vec;
	}

	/**
	 * Verschiebt diesen Vektor um den Faktor {@code t} in Richtung des Vektors
	 * {@code vector}. Für {@code t} gleich 0 ist der erzeugte Vektor gleich
	 * {@code vector1}, für {@code t} gleich 1 ist das Ergebnis gleich
	 * {@code vector2}. Für {@code t} gleich 0.5 liegt der Vektor genau mittig
	 * zwischen den beiden Ortsvektoren.
	 * <p>
	 * Negative Werte für {@code t} verschieben den Vektor von {@code vector}
	 * weg und Werte größer 1 schieben ihn über den anderen Vektor hinaus. Soll
	 * dies verhindert werden kann {@link #morph(Vector, double)} verwendet
	 * werden.
	 *
	 * @param vector Der erste Ortsvektor.
	 * @param t Ein Wert zwischen 0 und 1.
	 * @return Dieser Vektor selbst (method chaining)
	 */
	public Vector interpolate( Vector vector, double t ) {
		x = x + (vector.x - x) * t;
		y = y + (vector.y - y) * t;
		return this;
	}

	/**
	 * Erzeugt einen Vektor, der auf der Strecke zwischen den angegebenen
	 * Vektoren liegt. Der Ort des Vektors auf der Strecke wird relativ von
	 * {@code vector1} abhängig von {@code t} bestimmt. Für {@code t} gleich 0
	 * ist der erzeugte Vektor gleich {@code vector1}, für {@code t} gleich 1
	 * ist das Ergebnis gleich {@code vector2}. Für {@code t} gleich 0.5 liegt
	 * der Vektor genau mittig zwischen den beiden Ortsvektoren.
	 * <p>
	 * Negative Werte für {@code t} verschieben den Vektor von {@code vector}
	 * weg und Werte größer 1 schieben ihn über den anderen Vektor hinaus. Soll
	 * dies verhindert werden kann {@link #morph(Vector, Vector, double)}
	 * verwendet werden.
	 *
	 * @param vector1 Der erste Ortsvektor.
	 * @param vector2 Der zweite Ortsvektor.
	 * @param t Ein Wert zwischen 0 und 1.
	 * @return Ein Vektor zwischen den beiden Ortsvektoren.
	 */
	public static Vector interpolate( Vector vector1, Vector vector2, double t ) {
		Vector vec = vector1.copy();
		vec.interpolate(vector2, t);
		return vec;
	}

	@Override
	public String toString() {
		return "schule.ngb.zm.Vector[x = " + x + ", y = " + y + "]";
	}

}
