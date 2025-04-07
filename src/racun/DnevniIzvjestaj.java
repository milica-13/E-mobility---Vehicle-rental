package racun;

import java.time.LocalDate;

public class DnevniIzvjestaj {
	   private LocalDate datum;
	    private double ukupanPrihod;
	    private double ukupanPopust;
	    private double ukupnaPromocija;
	    private double ukupnoUziDioGrada;
	    private double ukupnoSiriDioGrada;
	    private double iznosZaOdrzavanje;
	    private double iznosZaPopravke;
	    private double troskoviKompanije;
	    private double porez;

	    public DnevniIzvjestaj(LocalDate datum) {
	        this.datum = datum;
	    }

	    public void dodajRacun(GenerisiRacun racun) {
	        ukupanPrihod += racun.getUkupnoZaPlacanje();
	        ukupanPopust += racun.getPopust();
	        ukupnaPromocija += racun.getPromocija();
	        if (racun.getUziDioGrada()) {
	            ukupnoUziDioGrada += racun.getUkupnoZaPlacanje();
	        } else {
	            ukupnoSiriDioGrada += racun.getUkupnoZaPlacanje();
	        }
	    }

	    public void izracunajIzvjestaj() {
	        iznosZaOdrzavanje = ukupanPrihod * 0.2;
	        iznosZaPopravke = 0.0; // Treba računati na osnovu koeficijenata
	        troskoviKompanije = ukupanPrihod * 0.2;
	        porez = (ukupanPrihod - iznosZaOdrzavanje - iznosZaPopravke - troskoviKompanije) * 0.1;
	    }

	    public LocalDate getDatum() { return datum; }
	    public double getUkupanPrihod() { return ukupanPrihod; }
	    public double getUkupanPopust() { return ukupanPopust; }
	    public double getUkupnaPromocija() { return ukupnaPromocija; }
	    public double getUkupnoUziDioGrada() { return ukupnoUziDioGrada; }
	    public double getUkupnoSiriDioGrada() { return ukupnoSiriDioGrada; }
	    public double getIznosZaOdrzavanje() { return iznosZaOdrzavanje; }
	    public double getIznosZaPopravke() { return iznosZaPopravke; }
	    public double getTroskoviKompanije() { return troskoviKompanije; }
	    public double getPorez() { return porez; }

	    // Metode za računanje iznosa za popravke treba dodati posebno, na osnovu računa
	    public void dodajPopravke(double iznosPopravke, String tipVozila) {
	        double koeficijent = 0;
	        switch (tipVozila.toLowerCase().charAt(0)) {
	            case 'a':
	                koeficijent = 0.07;
	                break;
	            case 'b':
	                koeficijent = 0.04;
	                break;
	            case 't':
	                koeficijent = 0.02;
	                break;
	            default:
	                koeficijent = 0.0;
	                break;
	        }
	        iznosZaPopravke += iznosPopravke * koeficijent;
	    }

}
