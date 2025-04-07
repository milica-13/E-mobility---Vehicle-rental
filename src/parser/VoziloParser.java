package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import javafx.scene.media.AudioClip;
import vozila.Automobil;
import vozila.Bicikl;
import vozila.Trotinet;
import vozila.Vozilo;

public class VoziloParser
{
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy.");
	protected Path path;
	
	public VoziloParser(Path path) {
		this.path = path;
	}
	

	public ArrayList<Vozilo> parseVozila(List<String> lines){
		ArrayList<Vozilo> vozila = new ArrayList<Vozilo>();
		/*
		for(var line: lines) {
			String[] atributi = line.split(",");
			Vozilo vozilo = parseVozilo(atributi);
			vozila.add(vozilo);
		}
		*/
		for(int i=1; i<lines.size(); i++) {
			String line = lines.get(i);
			String[] atributi = line.split(",");
			Vozilo vozilo = parseVozilo(atributi);
			vozila.add(vozilo);
		}
		return vozila;
	}
	
	private static Vozilo parseVozilo(String[] atributi){
		String vrsta = atributi[8];
		switch(vrsta.toLowerCase()){
		case "automobil": 
			LocalDate datum = LocalDate.parse(atributi[3], DATE_FORMATTER);
			return new Automobil(atributi[0], atributi[1], atributi[2], Double.parseDouble(atributi[4]),
					datum, atributi[7], atributi[8]);
		case "bicikl":
			return new Bicikl(atributi[0], atributi[1], atributi[2], Double.parseDouble(atributi[4]), 
					Double.parseDouble(atributi[5]), atributi[8]);
		case "trotinet":
			return new Trotinet(atributi[0], atributi[1], atributi[2], Double.parseDouble(atributi[4]), 
					Double.parseDouble(atributi[6]), atributi[8]);
			default:
				throw new IllegalArgumentException("Nepoznat tip vozila!");
		}
	}
		/*ArrayList<String> list = new ArrayList<>(); //przna lista
		var lines = this.getLines(); 
		var tmp = this.getLineContetnt(lines.get(1), "|");
		list.addAll(Arrays.asList(tmp));
		
		return list;*/
	
	public String getVrsta(String[] atributi) {
		return atributi[8];
	}
	
}
