package racun;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Izvjestaj {
	private static final double KOEFICIJENT_ZA_POPRAVAK_AUTOMOBILA = 0.07;
    private static final double KOEFICIJENT_ZA_POPRAVAK_BICIKALA = 0.04;
    private static final double KOEFICIJENT_ZA_POPRAVAK_TROTINETA = 0.02;
    private static final double KONSTANTA_ZA_ODRZAVANJE = 0.2;
    private static final double TROSKOVI_KOMPANIJE = 0.2; /* troskovi 20% */
    private static final double KONSTANTA_ZA_OBRACUN_POREZA = 0.1; /*konstanta 10%*/
    
    private ArrayList<GenerisiRacun> racuni;

    public Izvjestaj(ArrayList<GenerisiRacun> racuni) {
        this.racuni = racuni;
    }

    public double izracunajUkupanPrihod() {
        return racuni.stream().mapToDouble(GenerisiRacun::getUkupnoZaPlacanje).sum();
    }

    public double izracunajUkupanPopust() {
        return racuni.stream().mapToDouble(GenerisiRacun::getPopust).sum();
    }

    public double izracunajUkupnuPromociju() {
        return racuni.stream().mapToDouble(GenerisiRacun::getPromocija).sum();
    }

    public double izracunajUkupnoUziDioGrada() {
        return racuni.stream().filter(GenerisiRacun::getUziDioGrada).mapToDouble(GenerisiRacun::getUkupnoZaPlacanje).sum();
    }

    public double izracunajUkupnoSiriDioGrada() {
        return racuni.stream().filter(r -> !r.getUziDioGrada()).mapToDouble(GenerisiRacun::getUkupnoZaPlacanje).sum();
    }

    public double izracunajUkupanIznosZaOdrzavanje() {
        return izracunajUkupanPrihod() * KONSTANTA_ZA_ODRZAVANJE;
    }

    public double izracunajUkupanIznosZaPopravke() {
        return racuni.stream().mapToDouble(r -> {
            double koeficijent = 0;
            switch (r.getSTipVozila().toLowerCase().charAt(0)) {
                case 'A':
                    koeficijent = KOEFICIJENT_ZA_POPRAVAK_AUTOMOBILA;
                    break;
                case 'B':
                    koeficijent = KOEFICIJENT_ZA_POPRAVAK_BICIKALA;
                    break;
                case 'T':
                    koeficijent = KOEFICIJENT_ZA_POPRAVAK_TROTINETA;
                    break;
            }
            return koeficijent * r.getScenaPopravke();
        }).sum();
    }

    public double izracunajUkupanPorez() {
        double prihod = izracunajUkupanPrihod();
        double odrzavanje = izracunajUkupanIznosZaOdrzavanje();
        double popravke = izracunajUkupanIznosZaPopravke();
        double troskoviKompanije = prihod * TROSKOVI_KOMPANIJE;
        return (prihod - odrzavanje - popravke - troskoviKompanije) * KONSTANTA_ZA_OBRACUN_POREZA;
    }

    public HashMap<LocalDate, DnevniIzvjestaj> generisiDnevneIzvjestaje() {
        HashMap<LocalDate, DnevniIzvjestaj> dnevniIzvjestaji = new HashMap<>();

        for (GenerisiRacun racun : racuni) {
            LocalDate datum = racun.getDatum();
            DnevniIzvjestaj dnevniIzvjestaj = dnevniIzvjestaji.getOrDefault(datum, new DnevniIzvjestaj(datum));
            dnevniIzvjestaj.dodajRacun(racun);
            dnevniIzvjestaji.put(datum, dnevniIzvjestaj);
        }

        return dnevniIzvjestaji;
    }
}
