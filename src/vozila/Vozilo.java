package vozila;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import parser.VoziloParser;

public class Vozilo implements Serializable{
	public static final String VOZILA_PATH = "./resources" + File.separator + "VozilaPJ2.csv";
	public static final int MAX_NIVO_BATERIJE = 100;
	
	protected String id;
	protected String proizvodjac;
	protected String model;
	protected double cijenaNabavke;
	protected int nivoBaterije;
	protected String vrsta;
	public static boolean voziloSeKrece=false;
	//public static int brojIznajmljivanja=0;
	public static boolean isSiriDioGrada = false;
	Random rand = new Random();
	
	protected boolean kvar = false;
	protected String opisKvara;
	protected LocalDate datumKvara;
	

	public Vozilo() {
		this.id = " ";
		this.proizvodjac = " ";
		this.model = " ";
		this.cijenaNabavke = 0.0;
		this.nivoBaterije = 0;
		this.kvar = false;
		this.vrsta = " ";
	}
	
	public Vozilo(String id) {
		this.id = id;
		obradiBateriju();
	}
	
	public Vozilo(String id, String proizvodjac, String model, double cijenaNabavke, String vrsta) {
		this.id = id;
		this.proizvodjac = proizvodjac;
		this.model = model;
		this.cijenaNabavke = cijenaNabavke;
		//this.nivoBaterije = nivoBaterije;
		this.vrsta = vrsta;
		this.nivoBaterije = 100;
		//this.brojIznajmljivanja++;
		obradiBateriju();
	}

	public String getVrsta() {
		return vrsta;
	}
	
	public void setVrsta(String vrsta) {
		this.vrsta =vrsta;
	}

	public String getID() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getProizvodjac() {
		return proizvodjac;
	}

	public void setProizvodjac(String proizvodjac) {
		this.proizvodjac = proizvodjac;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public double getCijena() {
		return cijenaNabavke;
	}

	public void setCijenaNabavke(double cijenaNabavke) {
		this.cijenaNabavke = cijenaNabavke;
	}

	public int getNivoBaterije() {
		return nivoBaterije;
	}

	public void setNivoBaterije(int nivoBaterije) {
		this.nivoBaterije = nivoBaterije;
	}
	
	public boolean isPokvaren() {
		return kvar;
	}

	public void setPokvaren(boolean pokvaren) {
		this.kvar = pokvaren;
	}

	public String getOpis() {
		return opisKvara;
	}

	public void setOpisKvara(String opisKvara) {
		this.opisKvara = opisKvara;
	}

	@Override
	public String toString() {
		return "Podaci o vozilu! Id: " + this.id + ", proizvodjac: " + this.proizvodjac + ", model: " + this.model
				+ " cijena nabavke " + this.cijenaNabavke;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Vozilo))
			return false;
		Vozilo tmp = (Vozilo)other;
		return this.id.equals(tmp.getID());
	}
	
	@Override
	public int hashCode() {
		int hash = 3;
		hash = hash * 7 + id.hashCode();
		return hash;
	}
	
	public static ArrayList<Vozilo> citajPodatke(){
		ArrayList<Vozilo> vozila = new ArrayList<Vozilo>();
		try {
			Path path = Paths.get(VOZILA_PATH);
			List<String> lines = Files.readAllLines(path);
			VoziloParser parser = new VoziloParser(path);
			vozila = parser.parseVozila(lines);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return vozila;
	}
	
	public void obradiBateriju() {
		if (this.nivoBaterije > 15) { // Provjera da baterija ima više od 15%
			this.nivoBaterije -= 5; // Umanji bateriju za 5%
		} else if (this.nivoBaterije > 10 && this.nivoBaterije <= 15) {
			this.nivoBaterije = 10; // Ako je nivo između 10 i 15%, postavi na 10%
		} else {
			this.nivoBaterije = 100; // Ako je baterija ispod 10%, postavi je na 100%
		}
		//System.out.println("Pozvana je metoda za obradu baterije " + this.nivoBaterije);
	}
}
