package racun;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDate;

import loader.ConfigLoader;
import vozila.Vozilo;

public class GenerisiRacun implements Serializable {
	private static final String LOKACIJA_SKLADISTENJA = "./resources" + File.separator + "racuni";
	private static final String SUMARNI_IZVJESTAJ = "./resources" + File.separator + "Sumarni_Izvjestj";
    private static final long serialVersionUID = 1L;
	private static final double KOEFICIJENT_ZA_POPRAVAK_AUTOMOBILA = 0.07;
    private static final double KOEFICIJENT_ZA_POPRAVAK_BICIKALA = 0.04;
    private static final double KOEFICIJENT_ZA_POPRAVAK_TROTINETA = 0.02;
    
    ConfigLoader configLoader;
	private double osnovnaCijena;
	private double popust;
	private double promocija;
	private double ukupnoZaPlacanje;
	private boolean sacUziDioGrada;
	private String sTipVozila;
	private double sCenaPopravke;
	private LocalDate datum;
	

	public GenerisiRacun(ConfigLoader configLoader) {
		this.configLoader = configLoader;
	}
	
	public GenerisiRacun(ConfigLoader configLoader, String tipVozila) {
		this.sTipVozila = tipVozila;
		this.configLoader = configLoader;
	}

	public ConfigLoader getConfigLoader() {
		return configLoader;
	}

	public void setConfigLoader(ConfigLoader configLoader) {
		this.configLoader = configLoader;
	}
	public LocalDate getDatum() {
		return datum;
	}
	
	public void setDatum(LocalDate datum) {
		this.datum =datum;
	}
	
	public double getScenaPopravke() {
		return sCenaPopravke;
	}
	
	public boolean getUziDioGrada() {
		return sacUziDioGrada;
	}
	
	public String getSTipVozila() {
		return sTipVozila;
	}
	public double getOsnovnaCijena() {
		return osnovnaCijena;
	}

	public void setOsnovnaCijena(double osnovnaCijena) {
		this.osnovnaCijena = osnovnaCijena;
	}

	public double getPopust() {
		return popust;
	}

	public void setPopust(double popust) {
		this.popust = popust;
	}

	public double getPromocija() {
		return promocija;
	}

	public void setPromocija(double promocija) {
		this.promocija = promocija;
	}

	public double getUkupnoZaPlacanje() {
		return ukupnoZaPlacanje;
	}

	public void setUkupnoZaPlacanje(double ukupnoZaPlacanje) {
		this.ukupnoZaPlacanje = ukupnoZaPlacanje;
	}
	
	public void izracunajOsnovnuCijenu(String tipVozila, double trajanjeUSekundama) {
		String v=tipVozila;
		System.out.println("Tip vozila iz izracunaj os cijenu: " + tipVozila);
		if(v.startsWith("A")) {
			osnovnaCijena = Double.parseDouble(configLoader.getProperty("CAR_UNIT_PRICE"))*trajanjeUSekundama;
		}else if(v.startsWith("B")) {
			osnovnaCijena = Double.parseDouble(configLoader.getProperty("BIKE_UNIT_PRICE")) * trajanjeUSekundama;
		}else if(v.startsWith("T")) {
			osnovnaCijena = Double.parseDouble(configLoader.getProperty("BIKE_UNIT_PRICE")) * trajanjeUSekundama;
		}else {
			osnovnaCijena=0;
			System.out.println("Nepoznato vozilo!");
		}
		
	}
	
	
	
	public void izracunajCijenuNaOsnovuUdaljenosti(boolean uziDioGrada) {
		sacUziDioGrada=uziDioGrada;
		if(uziDioGrada) {
			osnovnaCijena *= Double.parseDouble(configLoader.getProperty("DISTANCE_NARROW"));
		}else
			osnovnaCijena *= Double.parseDouble(configLoader.getProperty("DISTANCE_WIDE"));
	}
	
	public void primjeniPopust(int brojIznajmljivanja) {
		if(brojIznajmljivanja % 10 ==0) {
			popust = osnovnaCijena * Double.parseDouble(configLoader.getProperty("DISCOUNT"));
		}
	}
	
	public void primjeniPormociju(boolean jePromocija) {
		if(jePromocija)
			promocija = osnovnaCijena * Double.parseDouble(configLoader.getProperty("DISCOUNT_PROM"));
	}
	
    public void izracunajPopravke(double cenaPopravke, String tipVozila) {
    	sCenaPopravke=cenaPopravke;
        sTipVozila=tipVozila;
    	double koeficijent = 0;
        String v = tipVozila.toLowerCase();

        if (v.startsWith("A")) {
            koeficijent = KOEFICIJENT_ZA_POPRAVAK_AUTOMOBILA; 
        } else if (v.startsWith("B")) {
            koeficijent = KOEFICIJENT_ZA_POPRAVAK_BICIKALA;
        } else if (v.startsWith("T")) {
            koeficijent = KOEFICIJENT_ZA_POPRAVAK_TROTINETA;
        } else {
            // Ako nije ni jedno od navedenih vozila
            koeficijent = 0.0;
        }

      
    }
   
    
	public void desioSeKvar(boolean jeKvar) {
		if(jeKvar) {
			osnovnaCijena = 0;
			ukupnoZaPlacanje = 0;
			popust = 0;
			promocija = 0;
		}
	}
	
	public void izracunajUkupnoZaPlacanje() {
		ukupnoZaPlacanje = osnovnaCijena - popust - promocija;
	}
	
	public void izdajRacun(Vozilo v, String tipVozila, double trajanjeUSekundama, boolean jePromocija, boolean jeKvar, int brojIznajmljivanja) {
		izracunajOsnovnuCijenu(tipVozila, trajanjeUSekundama);
		izracunajCijenuNaOsnovuUdaljenosti(v.isSiriDioGrada);
		primjeniPopust(brojIznajmljivanja);
		primjeniPormociju(jePromocija);
		desioSeKvar(jeKvar);
		izracunajUkupnoZaPlacanje();
		
		prikaziRacun();
	}
	
	public void prikaziRacun(){
		System.out.println("Osnovna cijena: " + this.osnovnaCijena);
		System.out.println("Popust: " + this.popust);
		System.out.println("Promocija: " +this.promocija);
		System.out.println("Ukupno za placanje: " + this.ukupnoZaPlacanje);
		System.out.println("\n");
	}
	
	public void generisiRacun(String id, String dokument) {
	    // Logika za generisanje računa u tekstualnom formatu i snimanje u fajl.
	    try (PrintWriter out = new PrintWriter(LOKACIJA_SKLADISTENJA + "/racun_" + id + "_" + dokument + ".txt")) {
			out.println("Osnovna cijena: " + this.osnovnaCijena);
	        out.println("Popust: " + this.popust);
	        out.println("Promocija: " + this.promocija);
	        out.println("Ukupno za placanje: " + this.ukupnoZaPlacanje);
	    } catch (FileNotFoundException e) {
	        System.err.println("Greška prilikom snimanja računa: " + e.getMessage());
	    }
	   
	}
	
    @Override
    public String toString() {
        return String.format("Datum: %s\nPopust: %.2f\nPromocija: %.2f\nScena Popravke: %.2f\nTip Vozila: %s",
                datum, popust, promocija, sCenaPopravke, sTipVozila);
    }
	
}	
