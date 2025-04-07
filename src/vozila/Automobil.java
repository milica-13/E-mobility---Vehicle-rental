package vozila;

import java.time.LocalDate;
import java.util.Random;

public class Automobil extends Vozilo {
	public LocalDate datumNabavke;
	public String opis;
	//private static int id=1;
	private int brojLjudi;


	public Automobil(String id, String proizvodjac, String model, double cijenaNabavke, LocalDate datumNabavke,
			String opis, String vrsta) {
		super(id,proizvodjac, model, cijenaNabavke, vrsta);
		this.datumNabavke = datumNabavke;
		this.opis = opis;
		//this.id++;
		Random rand = new Random();
		this.brojLjudi = rand.nextInt(4)+1;
	}
		
	
	public LocalDate getDatumNabavke() {
		return datumNabavke;
	}

	public void setDatumNabavke(LocalDate datumNabavke) {
		this.datumNabavke = datumNabavke;
	}
	
	public int getBrojLjudi() {
		return brojLjudi;
	}

	public void setBrojLjudi(int brojLjudi) {
		this.brojLjudi = brojLjudi;
	}
	
	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}
	
	
	@Override
	public String toString() {
		return super.toString() + " datum nabavke " + this.datumNabavke + " opis: " + this.opis;
	}
	
	
}
