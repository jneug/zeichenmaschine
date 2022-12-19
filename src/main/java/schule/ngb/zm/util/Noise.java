package schule.ngb.zm.util;

import java.util.Random;

/**
 * Generator für Perlin-Noise.
 * <p>
 * Die Implementierung basiert auf dem von Ken Perlin entwickelten Algorithmus
 * und wurde anhand der <a
 * href="https://adrianb.io/2014/08/09/perlinnoise.html">Beschreibung von
 * FLAFLA2</a> implementiert.
 */
@SuppressWarnings( "unused" )
public class Noise {

	private static final int N = 256;

	private static final int M = N - 1;

	/**
	 * Interne Permutationstabelle für diesen Generator.
	 */
	private int[] p;

	private double octaves = 1, persistence = .5;

	private double frequency = 1;

	private double amplitude = 1;

	private int repeat = -1;

	private double rangeMin = 0.0, rangeMax = 1.0;

	public Noise() {
		this(null);
	}

	public Noise( long seed ) {
		init(new Random(seed));
	}

	/**
	 * Initialisiert diesen Perlin-Noise mit dem angegebenen Zufallsgenerator.
	 *
	 * @param rand Ein Zufallsgenerator-Objekt.
	 */
	public Noise( Random rand ) {
		init(rand);
	}

	public double getOctaves() {
		return octaves;
	}

	public void setOctaves( double pOctaves ) {
		this.octaves = pOctaves;
	}

	public double getPersistence() {
		return persistence;
	}

	public void setPersistence( double pPersistence ) {
		this.persistence = pPersistence;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency( double pFrequency ) {
		this.frequency = pFrequency;
	}

	public double getAmplitude() {
		return amplitude;
	}

	public void setAmplitude( double pAmplitude ) {
		this.amplitude = pAmplitude;
	}

	public void setRange( double pRangeMin, double pRangeMax ) {
		this.rangeMin = pRangeMin;
		this.rangeMax = pRangeMax;
	}

	public double getRangeMin() {
		return rangeMin;
	}

	public double getRangeMax() {
		return rangeMax;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat( int pRepeat ) {
		this.repeat = pRepeat;
	}

	public double noise( double x ) {
		double total = 0;
		double freq = this.frequency;
		double amp = this.amplitude;
		double maxValue = 0;

		for( int i = 0; i < octaves; i++ ) {
			total += perlin(x * freq) * amp;

			maxValue += amp;

			amp *= persistence;
			freq *= 2;
		}

		return lerp(rangeMin, rangeMax, (total / maxValue));
	}

	public double noise( double x, double y ) {
		double total = 0;
		double freq = this.frequency;
		double amp = this.amplitude;
		double maxValue = 0;

		for( int i = 0; i < octaves; i++ ) {
			total += perlin(x * freq, y * freq) * amp;

			maxValue += amp;

			amp *= persistence;
			freq *= 2;
		}

		return lerp(rangeMin, rangeMax, (total / maxValue));
	}

	public double noise( double x, double y, double z ) {
		double total = 0;
		double freq = this.frequency;
		double amp = this.amplitude;
		double maxValue = 0;

		for( int i = 0; i < octaves; i++ ) {
			total += perlin(x * freq, y * freq, z * freq) * amp;

			maxValue += amp;

			amp *= persistence;
			freq *= 2;
		}

		return lerp(rangeMin, rangeMax, (total / maxValue));
	}

	private double perlin( double x ) {
		// @formatter:off
		if( repeat > 0 ) {
			x %= repeat;
		}

		int xi = (int)x & M;

		double xf = x - (int)x;

		double u = fade(xf);

		int a, b;
		a = p[    xi ];
		b = p[inc(xi)];

		return (lerp(grad(a,xf), grad(b,xf-1), u) + 1) / 2;
		// @formatter:on
	}

	private double perlin( double x, double y ) {
		// @formatter:off
		if( repeat > 0 ) {
			x %= repeat;
			y %= repeat;
		}

		int xi = (int) x & M;
		int yi = (int) y & M;

		double xf = x - (int) x;
		double yf = y - (int) y;

		double u = fade(xf);
		double v = fade(yf);

		int aa, ab, ba, bb;
		aa = p[p[    xi ] +     yi ];
		ab = p[p[    xi ] + inc(yi)];
		ba = p[p[inc(xi)] +     yi ];
		bb = p[p[inc(xi)] + inc(yi)];

		double x1, x2;
		x1 = lerp(
			grad(aa,    xf  , yf),
			grad(ba, xf-1, yf),
			u);
		x2 = lerp(
			grad(ab,    xf  , yf-1),
			grad(bb, xf-1, yf-1),
			u);

		return (lerp(x1, x2, v) + 1) / 2;
		// @formatter:on
	}

	private double perlin( double x, double y, double z ) {
		// @formatter:off
		if( repeat > 0 ) {
			x %= repeat;
			y %= repeat;
			z %= repeat;
		}

		int xi = (int)x & M;
		int yi = (int)y & M;
		int zi = (int)z & M;
		double xf = x - (int)x;
		double yf = y - (int)y;
		double zf = z - (int)z;

		double u = fade(xf);
		double v = fade(yf);
		double w = fade(zf);

		int aaa, aba, aab, abb, baa, bba, bab, bbb;
		aaa = p[p[p[    xi ] +     yi ] +     zi ];
		aba = p[p[p[    xi ] + inc(yi)] +     zi ];
		aab = p[p[p[    xi ] +     yi ] + inc(zi)];
		abb = p[p[p[    xi ] + inc(yi)] + inc(zi)];
		baa = p[p[p[inc(xi)] +     yi ] +     zi ];
		bba = p[p[p[inc(xi)] + inc(yi)] +     zi ];
		bab = p[p[p[inc(xi)] +     yi ] + inc(zi)];
		bbb = p[p[p[inc(xi)] + inc(yi)] + inc(zi)];

		double x1, x2, y1, y2;
		x1 = lerp(
			grad(aaa,    xf  , yf, zf),
			grad(baa, xf-1, yf, zf),
			u);
		x2 = lerp(
			grad(aba,    xf  , yf-1, zf),
			grad(bba, xf-1, yf-1, zf),
			u);
		y1 = lerp(x1, x2, v);

		x1 = lerp(
			grad(aab,    xf  , yf, zf-1),
			grad(bab, xf-1, yf, zf-1),
			u);
		x2 = lerp(
			grad(abb,    xf  , yf-1, zf-1),
			grad(bbb, xf-1, yf-1, zf-1),
			u);
		y2 = lerp(x1, x2, v);

		return (lerp(y1, y2, w) + 1) / 2;
		// @formatter:on
	}

	public void init( Random rand ) {
		p = new int[N * 2];

		if( rand == null ) {
			System.arraycopy(PERLIN_PERMUTATION, 0, p, 0, N);
		} else {
			// Generate random permutation
			for( int i = 0; i < N; i++ ) {
				int n = rand.nextInt(N);
				if( p[n] == 0 )
					p[n] = i;
				else
					i--;
			}
		}

		// Duplicate permutation array to prevent overflow errors
		System.arraycopy(p, 0, p, N, N);
	}

	private double fade( double t ) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	private double lerp( double a, double b, double t ) {
		return a + t * (b - a);
	}

	private int inc( int i ) {
		++i;
		if( repeat > 0 )
			i = i % repeat;
		return i;
	}

	private double grad( int hash, double x ) {
		switch( hash & 0x1 ) {
			// @formatter:off
			case 0x0: return  x;
			case 0x1: return -x;
			 default: return 0;
			// @formatter:on
		}
	}


	private double grad( int hash, double x, double y ) {
		switch( hash & 0x3 ) {
			// @formatter:off
			case 0x0: return  x;
			case 0x1: return -x;
			case 0x2: return  y;
			case 0x3: return -y;
			 default: return 0;
			// @formatter:on
		}
	}

	private double grad( int hash, double x, double y, double z ) {
		switch( hash & 0xF ) {
			// @formatter:off
			case 0x0: return  x + y;
			case 0x1: return -x + y;
			case 0x2: return  x - y;
			case 0x3: return -x - y;
			case 0x4: return  x + z;
			case 0x5: return -x + z;
			case 0x6: return  x - z;
			case 0x7: return -x - z;
			case 0x8: return  y + z;
			case 0x9: return -y + z;
			case 0xA: return  y - z;
			case 0xB: return -y - z;
			case 0xC: return  y + x;
			case 0xD: return -y + z;
			case 0xE: return  y - x;
			case 0xF: return -y - z;
			 default: return 0; // never happens
			// @formatter:on
		}
	}

	private static final int[] PERLIN_PERMUTATION = new int[]{
		151, 160, 137, 91, 90, 15,
		131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
		190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
		88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
		77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244,
		102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
		135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123,
		5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
		223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
		129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228,
		251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
		49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254,
		138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
	};

}
