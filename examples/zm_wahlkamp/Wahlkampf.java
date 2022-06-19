import schule.ngb.zm.Color;
import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.charts.*;

public class Wahlkampf extends Zeichenmaschine {

	public static void main( String[] args ) {
		new Wahlkampf();
	}

	public Wahlkampf() {
		super(800, 800, "Landtagswahl 2022 in Bielefeld");
	}

	public void draw() {
		background.setColor(240);

		DatabaseConnector dbc = new DatabaseConnector("", 0, "wahlergebnisse.db", "", "");
		QueryResult res;

		String[] parteien = new String[]{"CDU", "SPD", "FDP", "AfD", "GRÜNE", "DIELINKE", "PIRATEN"};
		Color[] farben = new Color[]{BLACK, RED, YELLOW, BLUE, GREEN, MAGENTA, ORANGE};

		// Erststimmen im Bezirk
		StringBuilder sb = new StringBuilder("SELECT ");
		for( int i = 0; i < parteien.length; i++ ) {
			sb.append("SUM(");
			sb.append('"');
			sb.append('D');
			sb.append(i+1);
			sb.append('-');
			sb.append(parteien[i]);
			sb.append("Erstimmen");
			sb.append('"');
			sb.append(')');
			if( i < parteien.length-1 ) {
				sb.append(',');
			}
		}
		sb.append(" FROM \"2022_Landtagswahl\"");

		dbc.executeStatement(sb.toString());
		res = dbc.getCurrentQueryResult();
		if( res != null ) {
			String[] data = res.getData()[0];

			double[] values = new double[parteien.length];
			for( int i = 0; i < data.length; i++ ) {
				values[i] = Double.parseDouble(data[i]);
			}
			RingChart chart1 = new RingChart(200, 200, 100, parteien.length);
			RingChart chart2 = new RingChart(600, 200, 100, parteien.length);
			RingChart chart3 = new RingChart(200, 600, 100, parteien.length);
			RingChart chart4 = new RingChart(600, 600, 100, parteien.length);

			double s = sum(values);
			for( int i = 0; i < values.length; i++ ) {
				double value = values[i];
				chart1.setValue(i, value, s, parteien[i], farben[i]);
				chart2.setValue(i, value, s, parteien[i], farben[i]);
				chart3.setValue(i, value, s, parteien[i], farben[i]);
				chart4.setValue(i, value, s, parteien[i], farben[i]);
			}

			chart4.setStrokeColor(200);

			shapes.add(chart1, chart2, chart3, chart4);
		} else {
			System.err.println(dbc.getErrorMessage());
		}

		pause();
	}

}
