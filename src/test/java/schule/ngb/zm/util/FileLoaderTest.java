package schule.ngb.zm.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileLoaderTest {

	@Test
	void loadLines() {
		String[] data;
		List<String> lines;

		data = new String[]{
			"Header1,Header2,Header3",
			"1.1,1.2,1.3",
			"2.1,2.2,2.3",
			"3.1,3.2,3.3"
		};

		lines = FileLoader.loadLines("data_comma.csv");
		assertEquals(data.length, lines.size());
		for( int i = 0; i < lines.size(); i++ ) {
			assertEquals(data[i], lines.get(i));
		}

		data = new String[]{
			"Nöme;Häder2;Straße",
			"1.1;1.2;1.3",
			"2.1;2.2;2.3",
			"3.1;3.2;3.3"
		};

		lines = FileLoader.loadLines("data_semicolon_latin.csv", FileLoader.ISO_8859_1);
		assertEquals(data.length, lines.size());
		for( int i = 0; i < lines.size(); i++ ) {
			assertEquals(data[i], lines.get(i));
		}
	}

	@Test
	void loadText() {
		String data;
		String text;

		data = "Header1,Header2,Header3\n" +
			"1.1,1.2,1.3\n" +
			"2.1,2.2,2.3\n" +
			"3.1,3.2,3.3\n";
		text = FileLoader.loadText("data_comma.csv");
		assertEquals(data, text);

		data = "Nöme;Häder2;Straße\n" +
			"1.1;1.2;1.3\n" +
			"2.1;2.2;2.3\n" +
			"3.1;3.2;3.3\n";
		text = FileLoader.loadText("data_semicolon_latin.csv", FileLoader.ISO_8859_1);
		assertEquals(data, text);
	}

	@Test
	void loadCsv() {
		double[][] data;
		double[][] csv;

		data = new double[][]{
			{1.1,1.2,1.3},
			{2.1,2.2,2.3},
			{3.1,3.2,3.3}
		};
		csv = FileLoader.loadDoubles("data_comma.csv", ',', true);
		assertArrayEquals(data, csv);

		csv = FileLoader.loadDoubles("data_semicolon_latin.csv", ';', true, FileLoader.ISO_8859_1);
		assertArrayEquals(data, csv);
	}

}
