package serijalizacija;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import racun.GenerisiRacun;

public class Serijalizacija {
	   public static final String SERIJALIZACIJA_PATH = "./resources" + File.separator + "Serijalizacija";

	    static {
	        File dir = new File(SERIJALIZACIJA_PATH);
	        if (!dir.exists()) {
	            dir.mkdirs(); // Kreira direktorijum ako ne postoji
	        }
	    }

	    public HashMap<String, GenerisiRacun> pronadjiRacunSaNajviseGubitaka(ArrayList<GenerisiRacun> racuni) {
	        HashMap<String, GenerisiRacun> vozilaGubici = new HashMap<>();

	        for (GenerisiRacun racun : racuni) {
	            double gubici = izracunajGubitke(racun);
	            String tipVozila = racun.getSTipVozila() != null && !racun.getSTipVozila().isEmpty()
	                               ? racun.getSTipVozila().substring(0, 1)
	                               : "";

	            if (!vozilaGubici.containsKey(tipVozila) || gubici > izracunajGubitke(vozilaGubici.get(tipVozila))) {
	                vozilaGubici.put(tipVozila, racun);
	            }
	        }
	        return vozilaGubici;
	    }

	    private double izracunajGubitke(GenerisiRacun racun) {
	        return racun.getPopust() + racun.getPromocija() + racun.getScenaPopravke();
	    }

	    public GenerisiRacun deserijalizujRacun(String tipVozila) throws IOException, ClassNotFoundException {
	        File file = new File(SERIJALIZACIJA_PATH, tipVozila + ".bin");
	        if (!file.exists()) {
	            throw new IOException("Fajl ne postoji: " + file.getPath());
	        }

	        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
	            return (GenerisiRacun) ois.readObject();
	        }
	    }
	/*
	public static final String SERIJALIZACIJA_PATH = "./resources" + File.separator + "Serijalizacija";

    public HashMap<String, GenerisiRacun> pronadjiRacunSaNajviseGubitaka(ArrayList<GenerisiRacun> racuni) {
        // HashMap za čuvanje računa sa najvećim gubicima po vrsti vozila
        HashMap<String, GenerisiRacun> vozilaGubici = new HashMap<>();

        // Iteracija kroz sve račune
        for (GenerisiRacun racun : racuni) {
            double gubici = racun.getPopust() + racun.getPromocija() + racun.getScenaPopravke();
            String tipVozila = racun.getSTipVozila() != null && !racun.getSTipVozila().isEmpty()
                    ? racun.getSTipVozila().substring(0, 1)
                    : "";
            // Ako HashMap ne sadrži trenutni tip vozila ili trenutni račun ima veće gubitke od postojećeg
            if (!vozilaGubici.containsKey(tipVozila) || gubici > izracunajGubitke(vozilaGubici.get(tipVozila))) {
                // Dodaj ili ažuriraj račun sa najvećim gubicima za taj tip vozila
                vozilaGubici.put(tipVozila, racun);
            }
        }
        return vozilaGubici;
    }
    
    // Metoda za deserijalizaciju jednog fajla
    public GenerisiRacun deserijalizujRacun(String tipVozila) throws IOException, ClassNotFoundException {
        File file = new File(SERIJALIZACIJA_PATH, tipVozila + ".bin");
        if (!file.exists()) {
            throw new IOException("Fajl ne postoji: " + file.getPath());
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (GenerisiRacun) ois.readObject();
        }
    }
    	
    	/* HashMap<String, GenerisiRacun> vozilaGubici = new HashMap<>();

        // Iteracija kroz sve račune
        for (GenerisiRacun racun : racuni) {
            // Izračunaj ukupne gubitke za trenutni račun
            double gubici = racun.getPopust() + racun.getPromocija() + racun.getScenaPopravke();
            // Dobavi tip vozila
            String tipVozila = racun.getSTipVozila();

            // Ako HashMap ne sadrži trenutni tip vozila ili trenutni račun ima veće gubitke od postojećeg
            if (!vozilaGubici.containsKey(tipVozila) || gubici > izracunajGubitke(vozilaGubici.get(tipVozila))) {
                // Dodaj ili ažuriraj račun sa najvećim gubicima za taj tip vozila
                vozilaGubici.put(tipVozila, racun);
            }
        }
        return vozilaGubici;
    	
    	/*  HashMap<String, GenerisiRacun> vozilaGubici = new HashMap<>();

        for (GenerisiRacun racun : racuni) {
            double gubici = racun.getPopust() + racun.getPromocija() + racun.getScenaPopravke();
            String tipVozila = racun.getSTipVozila();

            if (!vozilaGubici.containsKey(tipVozila) || gubici > izracunajGubitke(vozilaGubici.get(tipVozila))) {
                vozilaGubici.put(tipVozila, racun);
            }
        }
        return vozilaGubici;*/
    
/*
    private double izracunajGubitke(GenerisiRacun racun) {
        return racun.getPopust() + racun.getPromocija() + racun.getScenaPopravke();
    }

    public void serijalizujRacuneSaNajviseGubitaka(HashMap<String, GenerisiRacun> vozilaGubici) {
        File dir = new File(SERIJALIZACIJA_PATH);
        if (!dir.exists()) {
            dir.mkdirs(); // Kreiraj direktorijum ako ne postoji
        }

        for (Map.Entry<String, GenerisiRacun> entry : vozilaGubici.entrySet()) {
            String tipVozila = entry.getKey();
            GenerisiRacun racun = entry.getValue();

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(SERIJALIZACIJA_PATH, tipVozila + ".bin")))) {
                oos.writeObject(racun);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	/*
    public HashMap<String, GenerisiRacun> pronadjiRacunSaNajviseGubitaka(ArrayList<GenerisiRacun> racuni) {
        HashMap<String, GenerisiRacun> vozilaGubici = new HashMap<>();

        for (GenerisiRacun racun : racuni) {
            double gubici = racun.getPopust() + racun.getPromocija() + racun.getScenaPopravke();
            String tipVozila = racun.getSTipVozila();
            System.out.println("TIp vozila " + tipVozila);
            if (!vozilaGubici.containsKey(tipVozila) || gubici > izracunajGubitke(vozilaGubici.get(tipVozila))) {
                vozilaGubici.put(tipVozila, racun);
            }
        }
        return vozilaGubici;
    }

    private double izracunajGubitke(GenerisiRacun racun) {
        return racun.getPopust() + racun.getPromocija() + racun.getScenaPopravke();
    }

    public void serijalizujRacuneSaNajviseGubitaka(HashMap<String, GenerisiRacun> vozilaGubici) {
        for (Map.Entry<String, GenerisiRacun> entry : vozilaGubici.entrySet()) {
            String tipVozila = entry.getKey();
            GenerisiRacun racun = entry.getValue();

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIJALIZACIJA_PATH + File.separator + tipVozila + ".bin"))) {
                oos.writeObject(racun);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    */
}
