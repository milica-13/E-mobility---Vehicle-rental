package vozila;

public class Bicikl extends Vozilo{
	public double domet;
	//private static int id=1;
	
	public Bicikl(String id, String proizvodjac, String model, double cijenaNabavke,
			double domet, String vrsta) {
		super(id, proizvodjac, model, cijenaNabavke, vrsta);
		this.domet = domet;
		//this.id++;
	}
	
	public void setDomet(double domet) {
		this.domet = domet;
	}
	
	public double getDomet() {
		return domet;
	}
	
	@Override
	public String toString() {
		return super.toString() + " domet sa jednim punjenjem za bicikl " + this.domet;
	}
}
