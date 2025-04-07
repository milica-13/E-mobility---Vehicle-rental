package application;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	@Override
	public void start(Stage primaryStage) {
		final String MAIN_SCEAN_FXML = "/application/MainScean.fxml";

	    try {
	        // Učitajte FXML datotekus
	        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_SCEAN_FXML));
	        Parent root = loader.load();

	        // Nabavite kontroler povezan sa FXML-om
	        MainSceanController controller = loader.getController();

	        // Postavite scenu
	        Scene scene = new Scene(root, 593, 537);
	        primaryStage.setTitle("EMobility application");
	        primaryStage.setScene(scene);
	        primaryStage.setResizable(false);
	        primaryStage.show();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}



	public static void main(String[] args) {
	    launch(args);
	}


}

/*
public class Main {
    public static void main(String[] args) {
        // Kreiramo nekoliko instanci Iznajmljivanje za testiranje
        Iznajmljivanje iznajmljivanje1 = new Iznajmljivanje(LocalDateTime.now(), "Korisnik1", "Vozilo1", 10.0, 20.0, 30, false, false);
        Iznajmljivanje iznajmljivanje2 = new Iznajmljivanje(LocalDateTime.now(), "Korisnik1", "Vozilo2", 15.0, 25.0, 40, false, true);
        Iznajmljivanje iznajmljivanje3 = new Iznajmljivanje(LocalDateTime.now(), "Korisnik2", "Vozilo3", 20.0, 30.0, 50, true, false);
        Iznajmljivanje iznajmljivanje4 = new Iznajmljivanje(LocalDateTime.now(), "Korisnik2", "Vozilo4", 25.0, 35.0, 60, true, true);
        Iznajmljivanje iznajmljivanje5 = new Iznajmljivanje(LocalDateTime.now(), "Korisnik1", "Vozilo5", 30.0, 40.0, 70, false, false);

        // Ispisujemo generisane dokumente za svakog korisnika
        System.out.println("Dokument za Korisnik1 (iznajmljivanje 1): " + iznajmljivanje1.getDokument());
        System.out.println("Dokument za Korisnik1 (iznajmljivanje 2): " + iznajmljivanje2.getDokument());
        System.out.println("Dokument za Korisnik2 (iznajmljivanje 3): " + iznajmljivanje3.getDokument());
        System.out.println("Dokument za Korisnik2 (iznajmljivanje 4): " + iznajmljivanje4.getDokument());
        System.out.println("Dokument za Korisnik1 (iznajmljivanje 5): " + iznajmljivanje5.getDokument());

        // Provera broja unikatnih dokumenata za svaki korisnik
        System.out.println("Ukupan broj dokumenata u mapi: " + Iznajmljivanje.korisniciDokumenti.size());

        // Ispisivanje svih dokumenata u mapi
        System.out.println("Svi dokumenti u mapi:");
        for (Map.Entry<String, String> entry : Iznajmljivanje.korisniciDokumenti.entrySet()) {
            System.out.println("Korisnik: " + entry.getKey() + ", Dokument: " + entry.getValue());
        }
    }
}


/*
public class Main {
    public static void main(String[] args) {
        String imeKorisnika = "Korisnik1";

        // Iznajmljivanje 10 puta za "Korisnik1"
        for (int i = 0; i < 10; i++) {
            Iznajmljivanje iznajmljivanje = new Iznajmljivanje(LocalDateTime.now(), imeKorisnika, "Vozilo" + i, 10.0 * i, 20.0 * i, 30, false, false);
            System.out.println("Iznajmljivanje " + (i + 1) + ": Dokument: " + iznajmljivanje.getDokument() + ", Promocija: " + iznajmljivanje.isPromocija());
        }

        // Provera broja iznajmljivanja i popusta
        System.out.println("Ukupan broj iznajmljivanja za " + imeKorisnika + ": " + Iznajmljivanje.getBrojIznajmljivanja(imeKorisnika));
        
        // Kreirajmo još jedno iznajmljivanje nakon što je korisnik ostvario popust
        Iznajmljivanje iznajmljivanje11 = new Iznajmljivanje(LocalDateTime.now(), imeKorisnika, "Vozilo11", 110.0, 220.0, 330, false, false);
        System.out.println("Iznajmljivanje 11: Dokument: " + iznajmljivanje11.getDokument() + ", Promocija: " + iznajmljivanje11.isPromocija());
    }
}
*/
