package kompanija;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import application.MainSceanController;
import javafx.application.Platform;
import parser.IzmamljivanjeParser;
import vozila.Vozilo;

public class Iznajmljivanje implements Runnable{
	public static final String IZMAMLJIVANJE_PATH = "./resources" + File.separator + "Iznajmljivanje.csv";
	
	private LocalDateTime datumIvrijemeIznajmljivanja;
	private String imeKorisnika;
	private String idPrevoznogSredstva;
	private Double pocetnaLokacija;
	private Double odrediste;
	private int trajanjeKoriscenja;
	private boolean kvar=false;
	private boolean promocija = false;
	public static Vozilo vozilo;
	
	private boolean straniDrzavljanin = false;
	private String dokument;
	private Random rand = new Random();
	/*Mapa u kojoj cuvamo par korisnik, dokument*/
	public static Map<String, String> korisniciDokumenti = new HashMap<>();
	/*Mapa za cuvanje broja iznajmljivanja po korinsiku*/
	private static Map<String, Integer> brojIznajmljivanjaPoKorisniku = new HashMap<>();

	public Object objectLock = new Object();
	private MainSceanController controller;
	
	public Iznajmljivanje() {
		this.datumIvrijemeIznajmljivanja = LocalDateTime.now();
		this.imeKorisnika = " ";
		this.idPrevoznogSredstva =" ";
		this.pocetnaLokacija =0.0;
		this.odrediste = 0.0;
		this.trajanjeKoriscenja = 0;
		this.kvar = false;
		this.promocija = false;
		
	    postaviDokument();
	    dodajIznajmljivanje();
	}

		public Iznajmljivanje(LocalDateTime datum, String imeKorisnika, String idPrevoznogSredstva,
			Double pocetnaLokacija, Double odrediste, int trajanjeKoriscenja, boolean kvar, boolean promocija) {
		super();
		this.datumIvrijemeIznajmljivanja = datum;
		this.imeKorisnika = imeKorisnika;
		this.idPrevoznogSredstva = idPrevoznogSredstva;
		this.pocetnaLokacija = pocetnaLokacija;
		this.odrediste = odrediste;
		this.trajanjeKoriscenja = trajanjeKoriscenja;
		this.kvar = kvar;
		this.promocija = promocija;
		this.vozilo = new Vozilo(idPrevoznogSredstva);
		
	    postaviDokument();
	    dodajIznajmljivanje();
	    }
	

	
	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
		return "Datum iznajmljivanja " + this.datumIvrijemeIznajmljivanja.format(formatter) + 
				" Id prevoznog sredstva: " + this.idPrevoznogSredstva + " vozilo iznajmljenjo na lokaciji " 
				+ this.pocetnaLokacija + ", vraceno na lokaciju " + this.odrediste+ ". Vozilo je " + (this.kvar ? " imalo" : "nije imalo") + " kvar" +
				(this.promocija ? "Ostvarena je " : "Nije ostvarena") + "promocija. Iznajmljivanje je trajalo: "
				+ this.trajanjeKoriscenja;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Iznajmljivanje))
			return false;
		Iznajmljivanje tmp = (Iznajmljivanje)other;
		return this.idPrevoznogSredstva.equals(tmp.idPrevoznogSredstva); 
				 //&& this.imeKorisnika.equals(tmp.imeKorisnika);
				 
	}
	
	@Override
	public int hashCode() {
		int hash=3;
		hash = hash * 7 + this.idPrevoznogSredstva.hashCode();
		return hash;
	}
	
    // Metoda za postavljanje dokumenta
    public void postaviDokument() {
        if (imeKorisnika != null && !imeKorisnika.isEmpty()) {
            if (korisniciDokumenti.containsKey(imeKorisnika)) {
                this.dokument = korisniciDokumenti.get(imeKorisnika);
                //System.out.println("Dokument već postoji za korisnika: " + imeKorisnika);
            } else {
                this.straniDrzavljanin = rand.nextBoolean();  // Nasumično određujemo da li je strani državljanin
                this.dokument = generisiDokument();
                korisniciDokumenti.put(imeKorisnika, this.dokument);
                //System.out.println("Novi dokument generisan za korisnika: " + imeKorisnika);
            }
        } else {
            throw new IllegalArgumentException("Ime korisnika ne može biti prazno ili null.");
        }
    }
    
    public void postaviDokument(String korisnik) {
        if (korisnik != null && !korisnik.isEmpty()) {
            if (korisniciDokumenti.containsKey(imeKorisnika)) {
                this.dokument = korisniciDokumenti.get(korisnik);
                System.out.println("Dokument već postoji za korisnika: " + korisnik);
            } else {
                this.straniDrzavljanin = rand.nextBoolean();  // Nasumično određujemo da li je strani državljanin
                this.dokument = generisiDokument();
                korisniciDokumenti.put(korisnik, this.dokument);
                System.out.println("Novi dokument generisan za korisnika: " + korisnik);
            }
        } else {
            throw new IllegalArgumentException("Ime korisnika ne može biti prazno ili null.");
        }
    }


	private String generisiDokument() {
	    StringBuilder dokument = new StringBuilder();
	    if(straniDrzavljanin) {
	    	dokument.append("pasos_");
	    }else {
	    	dokument.append("licnaKarta_");
	    }
	    
	    // Generišemo 3 nasumična slova (A-Z)
	    for (int i = 0; i < 3; i++) {
	        char slovo = (char) (rand.nextInt(26) + 'A');
	        dokument.append(slovo);
	    }

	    // Generišemo 3 nasumične cifre (0-9)
	    for (int i = 0; i < 3; i++) {
	        int broj = rand.nextInt(10);
	        dokument.append(broj);
	    }

	    return dokument.toString();
	}

    /* Metoda za azuriranje broja iznajmljivanja*/
    private void dodajIznajmljivanje() {
        brojIznajmljivanjaPoKorisniku.merge(imeKorisnika, 1, Integer::sum);
    }

    /* Metoda za dohvatanje broja iznajmljivanja za korisnika*/
    public static int getBrojIznajmljivanja(String imeKorisnika) {
        return brojIznajmljivanjaPoKorisniku.getOrDefault(imeKorisnika, 0);
    }
	public String getDokument() {
		return dokument;
	}

	public LocalDateTime getDatumIvrijemeIznajmljivanja() {
		return datumIvrijemeIznajmljivanja;
	}
	
	public LocalDate getDatumIznajmljivanja() {
		return datumIvrijemeIznajmljivanja.toLocalDate();
	}

	public void setDatumIvrijemeIznajmljivanja(LocalDateTime datumIvrijemeIznajmljivanja) {
		this.datumIvrijemeIznajmljivanja = datumIvrijemeIznajmljivanja;
	}

	public String getImeKorisnika() {
		return imeKorisnika;
	}

	public void setImeKorisnika(String imeKorisnika) {
		this.imeKorisnika = imeKorisnika;
	}

	public String getIdPrevoznogSredstva() {
		return idPrevoznogSredstva;
	}

	public void setIdPrevoznogSredstva(String idPrevoznogSredstva) {
		this.idPrevoznogSredstva = idPrevoznogSredstva;
	}

	public Double getPocetnaLokacija() {
		return pocetnaLokacija;
	}

	public void setPocetnaLokacija(Double pocetnaLokacija) {
		this.pocetnaLokacija = pocetnaLokacija;
	}

	public Double getOdrediste() {
		return odrediste;
	}

	public void setOdrediste(Double odrediste) {
		this.odrediste = odrediste;
	}

	public boolean isKvar() {
		return kvar;
	}

	public void setKvar(boolean kvar) {
		this.kvar = kvar;
	}

	public boolean isPromocija() {
		return promocija;
	}

	public void setPromocija(boolean promocija) {
		this.promocija = promocija;
	}

	public Vozilo getPrevoznoSredstvo() {
		return vozilo;
	}

	public void setPrevoznoSredstvo(Vozilo vozilo) {
		this.vozilo = vozilo;
	}
	
	public void setMainSceanController(MainSceanController controller) {
		this.controller = controller;
	}
	
	public void setTrajanjeKoristcenja(int trajanjeKoristenja) {
		this.trajanjeKoriscenja = trajanjeKoristenja;
	}
	
	public int getTrajanjeKoriscenja() {
		return trajanjeKoriscenja;
	}
	
	public void setController(MainSceanController controller) {
		this.controller = controller;
	}
	
	public void setVozilo(Vozilo vozilo) {
		this.vozilo =vozilo;
	}
	
	public Vozilo getVozilo() {
		return vozilo;
	}
	
    public static int[] izracunajKoordinate(double lokacija) {
        int x = (int) lokacija; // Cijeli dio prije zareza
        String[] dijelovi = Double.toString(lokacija).split("\\."); // Dijeli broj na cijeli i decimalni dio
        int y = Integer.parseInt(dijelovi[1]); // Uzimanje cijelog decimalnog dijela kao broj

        return new int[]{x, y}; // Vraća niz gdje je [0] x, a [1] y koordinata
    	}

    	private static int brojDecimala(double broj) {
    	    String s = Double.toString(broj);
    	    int decimalnaTacka = s.indexOf('.');
    	    return s.length() - decimalnaTacka - 1;
    	}
    	
    public static ArrayList<Iznajmljivanje> citajPodatke() {
        ArrayList<Iznajmljivanje> izmami = new ArrayList<>();
        try {
            // Pretpostavimo da se CSV fajl zove "vozila.csv" i nalazi se u trenutnom direktorijumu
            Path path = Paths.get(IZMAMLJIVANJE_PATH);

            // Učitaj sve linije iz CSV fajla
            List<String> lines = Files.readAllLines(path);

            // Kreiraj instancu parsera
            IzmamljivanjeParser parser = new IzmamljivanjeParser(path);

            // Parsiraj linije u listu objekata tipa Iznajmljivanje
            izmami = parser.parseIzmamljivanjePodaci(lines);

        } catch (Exception e) {
            e.printStackTrace();
            // U slučaju greške možete vratiti praznu listu ili baciti RuntimeException
            // throw new RuntimeException("Greška pri čitanju podataka iz fajla", e);
        }
        return izmami;
    }
    /*
	    @Override
	    public void run() {
	    	synchronized (objectLock) {
	        try {
	        	if(MainSceanController.start) {
	        	int[] start = izracunajKoordinate(this.pocetnaLokacija);
	            int startX = start[0];
	            int startY = start[1];
	            
	            int[] cilj = izracunajKoordinate(this.odrediste);
	            int ciljX = cilj[0];
	            int ciljY = cilj[1];
	            
	            List<int[]> putanja = izrzracunajPutanju(startX, startY, ciljX, ciljY);
	            int brojPoljaPutanje = putanja.size();

	               for (int i = 0; i < putanja.size(); i++) {
	                    int trenutniX = putanja.get(i)[0];
	                    int trenutniY = putanja.get(i)[1];

	                    if (i > 0) { // Ako nije prva iteracija, ukloni labelu sa stare pozicije
	                        int prethodniX = putanja.get(i - 1)[0];
	                        int prethodniY = putanja.get(i - 1)[1];

	                        int finalPrethodniX = prethodniX;
	                        int finalPrethodniY = prethodniY;

	                        Platform.runLater(() -> {
	                            controller.removeLabelAtPosition(finalPrethodniX, finalPrethodniY);
	                        });
	                    }

	                    // Postavi labelu na trenutnu poziciju
	                    int finalTrenutniX = trenutniX;
	                    int finalTrenutniY = trenutniY;
	                    Platform.runLater(() -> {
	                        controller.addLabelAtPosition(finalTrenutniX, finalTrenutniY, vozilo);
	                    });

	                    Thread.sleep(500); // Pauza između svake iteracije
	                }

	        }
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	    
	    	}
	    }
	    */
    

    @Override
    public void run() {
           try {
           
                if (MainSceanController.start) {
               
                   
                	int[] start = izracunajKoordinate(this.pocetnaLokacija);
                    int startX = start[0];
                    int startY = start[1];
                    
                    int[] cilj = izracunajKoordinate(this.odrediste);
                    int ciljX = cilj[0];
                    int ciljY = cilj[1];
                    final int pom1=startX;
                    final int pom2=startY;
                    int brojKoraka = Math.abs(ciljX - startX) + Math.abs(ciljY - startY); 
                    System.out.println(brojKoraka + " " + idPrevoznogSredstva);
                    System.out.println("Trajanje: " + trajanjeKoriscenja);
                    int vrijemePoPolju = (int) (trajanjeKoriscenja * 1000) / brojKoraka;
                    System.out.println("Vrijeme: " + vrijemePoPolju);
                    Platform.runLater(()-> dodajVoziloNaMapu(pom1, pom2));
                    Thread.sleep(vrijemePoPolju);
                    provjeriGrad(startX, startY, ciljX, ciljY);
              
                    for (int i = 0; i < brojKoraka; i++) {
                        while (startX != ciljX) {
                            int prethodniX = startX;
                            int prethodniY = startY;
                            
                            startX += (startX < ciljX) ? 1 : -1;
                            final int finalStartX = startX;
                            final int finalStartY = startY;

                            
                            arzurirajMapu(prethodniX, prethodniY, finalStartX, finalStartY);
                            
                            Thread.sleep(vrijemePoPolju);
                            vozilo.obradiBateriju();
                            brojKoraka++;
                        }
                        
                        while (startY != ciljY) {
                            int prethodniX = startX;
                            int prethodniY = startY;
                            
                            startY += (startY < ciljY) ? 1 : -1;
                            final int finalStartX = startX;
                            final int finalStartY = startY;

                           arzurirajMapu(prethodniX, prethodniY, finalStartX, finalStartY);

                            Thread.sleep(vrijemePoPolju);
                            vozilo.obradiBateriju();
                            brojKoraka++;
                        }
                        vozilo.obradiBateriju();
                        //Thread.sleep(vrijemePoPolju);
                        if(startX==ciljX && startY==ciljY) {
                        	Platform.runLater(()->ukloniVoziloSaMape(ciljX, ciljY));
                        }
                    }

                   }
                   
    }             catch (Exception e) {
                e.printStackTrace();
    }
}

    public void arzurirajMapu(int prethodniX, int prethodniY, int naredniX, int naredniY) {
    	ukloniVoziloSaMape(prethodniX, prethodniY);
    	dodajVoziloNaMapu(naredniX, naredniY);
    	System.out.println("Pozicija: " + prethodniX + " " + prethodniY + " " + naredniX + " " + naredniY);
    }

    public void dodajVoziloNaMapu(int startX, int startY) {
    	String labelID=startX+","+startY;
    	//System.out.println(labelID);
    	for(int i=0; i<controller.BROJ_REDOVA; i++) {
    		for(int j=0; j<controller.BROJ_KOLONA; j++) {
    			String tmp = i+","+j;
    			if(labelID.equals(tmp)) {
    				//System.out.println(labelID);
    				//System.out.println(tmp);
    				Platform.runLater(() -> {
    					controller.addLabelAtPosition(startX, startY, getIdPrevoznogSredstva(), vozilo.getNivoBaterije());
    				}); 
    			}
    		}
    	}
    	
    }
    
    public void ukloniVoziloSaMape(int startX, int startY) {
    	String labelID=startX+","+startY;
    	for(int i=0; i<controller.BROJ_REDOVA; i++) {
    		for(int j=0; j<controller.BROJ_KOLONA; j++) {
    			String tmp = i+","+j;
				//System.out.println(labelID);
				//System.out.println(tmp);
    			if(labelID.equals(tmp)) {
    				controller.removeLabelAtPosition(i, j);
    			}
    		}
    	}
    	
    }
   
    public static void provjeriGrad(int startX, int startY, int ciljX, int ciljY) {
        if(startX>5 || startX>14 || startY>5 || startY<14) {
        	if(ciljX>5 || ciljX<14 || ciljY>5 || ciljY<14) {
					vozilo.isSiriDioGrada=true;
					System.out.println("Vozilo je u nekom dijelu grada " + vozilo.isSiriDioGrada);
		
        	}
        }
    }
}


