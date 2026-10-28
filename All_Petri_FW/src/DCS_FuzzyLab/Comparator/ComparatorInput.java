package DCS_FuzzyLab.Comparator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ComparatorInput {
	public static void main(String[] args) throws InterruptedException, IOException {

		File file = new File("D:\\Faculta\\Facultate\\DCS\\Lab\\PetriInputData\\comparator.txt");
		Files.deleteIfExists(file.toPath());

		if (file.getParentFile() != null) {
			file.getParentFile().mkdirs();

			FileWriter fw = new FileWriter(file.getPath());
			Float f1, f2;


			for (float i = 0; i < 3600; i++) {
				f1 = (float) Math.sin(Math.toRadians(i));
				f2 = (float) Math.cos(Math.toRadians(i));


				fw.write("P0:" + f1 + "F" + "," + "P1:" + f2 + "F\n");
			}
			fw.close();
			System.out.println("Done!");
		}
	}
}