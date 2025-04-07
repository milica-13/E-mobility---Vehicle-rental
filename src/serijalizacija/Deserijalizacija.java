package serijalizacija;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import vozila.Vozilo;

public class Deserijalizacija {
	private static final String DESERIJALIZACIJA_PATH = "./resources" + File.separator + "Serijalizacija";

	 public static ArrayList<Vozilo> deserijalizacijaVozila() throws IOException, ClassNotFoundException {
	        File folder = new File(DESERIJALIZACIJA_PATH);
	        File[] files = folder.listFiles((dir, name) -> name.endsWith(".ser"));
	        ArrayList<Vozilo> vehicles = new ArrayList<>();

	        if (files != null) {
	            for (File file : files) {
	                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
	                    vehicles.add((Vozilo) ois.readObject());
	                }
	            }
	        }
	        return vehicles;
	    }
}
