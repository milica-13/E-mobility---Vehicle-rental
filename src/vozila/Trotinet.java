package vozila;

public class Trotinet extends Vozilo{
	private double maxBrzina;
	//private static int id=1;

	public Trotinet(String id, String prozivodjac, String model, double cijenaNabavke,
			double maxBrzina, String vrsta) {
		super(id,prozivodjac, model, cijenaNabavke, vrsta);
		this.maxBrzina = maxBrzina;
		//this.id++;
	}
	
	public double getMaxBrzina() {
		return maxBrzina;
	}

	public void setMaxBrzina(double maxBrzina) {
		this.maxBrzina = maxBrzina;
	}
	@Override
	public String toString() {
		return super.toString() + " maksimalna brzina tortineta " + this.maxBrzina;
 	}
}
