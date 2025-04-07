package parser;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import kompanija.Iznajmljivanje;

public class IzmamljivanjeParser {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
	private Path path;

	
	public IzmamljivanjeParser(Path path) {
		this.path = path;
	}
	
	public ArrayList<Iznajmljivanje> parseIzmamljivanjePodaci(List<String> lines) {
	    ArrayList<Iznajmljivanje> iznajmLista = new ArrayList<Iznajmljivanje>();
	    
	    for (int i = 1; i <lines.size(); i++) {
	        String red = lines.get(i).trim();
	        // Uklanjanje navodnika
	        red = red.replace("\"", "");
	        
	        String[] atributi = red.split(",");

	        try {
	        	LocalDateTime datumVrijeme = LocalDateTime.parse(atributi[0].trim(), DATE_TIME_FORMATTER);
	            String korisnik = atributi[1].trim();
	            String idPrevoza = atributi[2].trim();
	            double pocetnaKoordinata1 = Double.parseDouble(atributi[3].trim());
	            double pocetnaKoordinata2 = Double.parseDouble(atributi[4].trim());
	            double odredisteKoordinata1 = Double.parseDouble(atributi[5].trim());
	            double odredisteKoordinata2 = Double.parseDouble(atributi[6].trim());
	            int trajanje = Integer.parseInt(atributi[7].trim());
	            boolean kvar = atributi[8].trim().equalsIgnoreCase("da");
	            boolean promocija = atributi[9].trim().equalsIgnoreCase("da");
	            Iznajmljivanje tmp = new Iznajmljivanje(datumVrijeme, korisnik, idPrevoza, spoji(pocetnaKoordinata1, pocetnaKoordinata2),
	            		spoji(odredisteKoordinata1, odredisteKoordinata2), trajanje, kvar, promocija);
	           
	            iznajmLista.add(tmp);

	        } catch (Exception e) {
	            System.out.println("Problem parsing line " + (i + 1) + ": " + red);
	            e.printStackTrace();
	        }
	    }
	    //return iznajmLista;
	    return iznajmLista.stream()
                .sorted(Comparator.comparing(Iznajmljivanje::getDatumIvrijemeIznajmljivanja))
                .collect(Collectors.toCollection(ArrayList::new));
	}
	
	public Double spoji(Double vrijednost1, Double vrijednost2) {
		int brDecMijesta = String.valueOf(vrijednost2).length() - 2;
		double pomjeraj = Math.pow(10, brDecMijesta); //pomjeranje desnog dijela 
		return vrijednost1 + (vrijednost2/pomjeraj);
	}

}
