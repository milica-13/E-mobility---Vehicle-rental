package application;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
/*
import java.awt.Button;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import kompanija.Iznajmljivanje;
import loader.ConfigLoader;
import racun.DnevniIzvjestaj;
import racun.GenerisiRacun;
import serijalizacija.Serijalizacija;
import vozila.Automobil;
import vozila.Bicikl;
import vozila.Trotinet;
import vozila.Vozilo;

public class MainSceanController {
	public static boolean start=true;
	public static final int BROJ_KOLONA = 20;
	public static final int BROJ_REDOVA = 20;
	private static final int VRIJEME_CEKANAJ_IZVRSAVANJA = 5000;
	private static final String CONFIG_FILE_NAME = "pricing.properties";
	//public static final String private ConfigLoader configLoader = new ConfigLoader("putanja/do/vaseg/properties/fajla");


	private Label[][] labels = new Label[BROJ_REDOVA][BROJ_KOLONA];
	private ScheduledExecutorService scheduler;
	private ArrayList<Iznajmljivanje> svaIznajmljivanja;
	private ArrayList<Vozilo> vozila;
	private ConfigLoader configLoader;
	private GenerisiRacun racun;
	private ArrayList<GenerisiRacun> generisaniRacuni = new ArrayList<GenerisiRacun>();
	//private GridPane gridPane = new GridPane();
	//private Button btnPocetak = new Button();
	@FXML
	private Tab mapaTab;

	@FXML
	private Tab prikazVozilaTab;
 
    @FXML
    private Button btnStart;

    @FXML
    private GridPane gridPaneMapa;
    @FXML
    private TableColumn<Automobil, Double> automobilCijena;

    @FXML
    private TableColumn<Automobil, LocalDate> automobilDatumNabavke;

    @FXML
    private TableColumn<Automobil, String> automobilId;

    @FXML
    private TableColumn<Automobil, String> automobilModel;

    @FXML
    private TableColumn<Automobil, String> automobilOpis;

    @FXML
    private TableColumn<Automobil, String> automobilProizvodjac;

    @FXML
    private TableColumn<Bicikl, Double> biciklCijena;

    @FXML
    private TableColumn<Bicikl, Double> biciklDomet;

    @FXML
    private TableColumn<Bicikl, String> biciklId;

    @FXML
    private TableColumn<Bicikl, String> biciklModel;

    @FXML
    private TableColumn<Bicikl, String> biciklProizvodjac;

    @FXML
    private TableView<Automobil> tabelaAutomobil;

    @FXML
    private TableView<Bicikl> tabelaBicikl;

    @FXML
    private TableView<Trotinet> tabelaTrotinet;

    @FXML
    private TableColumn<Trotinet, Double> trotinetCijena;

    @FXML
    private TableColumn<Trotinet, String> trotinetId;

    @FXML
    private TableColumn<Trotinet, Double> trotinetMaxBrzina;

    @FXML
    private TableColumn<Trotinet, String> trotinetModel;

    @FXML
    private TableColumn<Trotinet, String> trotinetProizvodjac;

	
	
    public void initialize() {
        svaIznajmljivanja = Iznajmljivanje.citajPodatke();
        vozila = Vozilo.citajPodatke();
        try {
			configLoader = new ConfigLoader(CONFIG_FILE_NAME);
			//System.out.println("fajl otvoren");
		} catch (IOException e) {
			e.printStackTrace();
		}
        postaviGridPane(gridPaneMapa);
        start = true;
        ucitajVozila();
        inicijalizujTabele();
        dodajKvar();
        kvarovi();
        //tabelaKvarova.setItems(svaVozila);
        dnevniIzvj();
        serijalizacija();

        voziloComboBox.getItems().addAll("A", "T", "B");
    }

   	public Label[][] getLabels() {
		return labels;
	}


	public void setLabels(Label[][] labels) {
		this.labels = labels;
	}

    public static boolean isStart() {
		return start;
	}


	public static void setStart(boolean start) {
		MainSceanController.start = start;
	}


	public Tab getMapaTab() {
		return mapaTab;
	}


	public void setMapaTab(Tab mapaTab) {
		this.mapaTab = mapaTab;
	}


	public Tab getPrikazVozilaTab() {
		return prikazVozilaTab;
	}


	public void setPrikazVozilaTab(Tab prikazVozilaTab) {
		this.prikazVozilaTab = prikazVozilaTab;
	}

	public Button getBtnStart() {
		return btnStart;
	}

	public void setBtnStart(Button btnStart) {
		this.btnStart = btnStart;
	}

	public GridPane getGridPaneMapa() {
		return gridPaneMapa;
	}

	public void setGridPaneMapa(GridPane gridPaneMapa) {
		this.gridPaneMapa = gridPaneMapa;
	}

    public void setIznajmljivanja(ArrayList<Iznajmljivanje> svaIznajmljivanja) {
        this.svaIznajmljivanja = svaIznajmljivanja;
    }
    
    private void pokreniKretanjeVozila(ArrayList<Iznajmljivanje> iznajmljivanja) throws InterruptedException {
        //System.out.println("Pokrenula se metoda");
        //System.out.println("Ispis iz metode pokreni ");
       // for(Iznajmljivanje iz: iznajmljivanja)
        //	System.out.println(iz);
        int i = 0;
        while (i < iznajmljivanja.size()) {
            ArrayList<Iznajmljivanje> trenutnoIzn = new ArrayList<>();
     
            // Dodaj prvo iznajmljivanje u trenutnu grupu
            trenutnoIzn.add(iznajmljivanja.get(i));
            LocalDateTime trenutak = iznajmljivanja.get(i).getDatumIvrijemeIznajmljivanja().truncatedTo(ChronoUnit.MINUTES);
            i++;

            // Traži sva vozila koja imaju isto vrijeme iznajmljivanja kao trenutak
            while (i < iznajmljivanja.size() &&
                    trenutak.isEqual(iznajmljivanja.get(i).getDatumIvrijemeIznajmljivanja().truncatedTo(ChronoUnit.MINUTES))) {
                trenutnoIzn.add(iznajmljivanja.get(i));
                i++;
            }

            // Pokretanje niti za grupu vozila
            List<Thread> threads = new ArrayList<>();
            for (Iznajmljivanje iz : trenutnoIzn) {
                Thread nit = new Thread(iz);
                threads.add(nit);
                nit.start();
            }

            // Sačekaj da se sve niti završe pre nego što pređeš na sledeću grupu
            for (Thread nit : threads) {
                nit.join();
            }

            for (Iznajmljivanje iz : trenutnoIzn) {
                racun = new GenerisiRacun(configLoader, iz.getIdPrevoznogSredstva()); // ili kako već instancirate vašu klasu
                
                racun.izdajRacun(iz.getVozilo(), iz.getIdPrevoznogSredstva(), iz.getTrajanjeKoriscenja(),  iz.isPromocija(), iz.isKvar(), iz.getBrojIznajmljivanja(iz.getImeKorisnika()));
             	// iz.postaviDokument(iz.getImeKorisnika());
                racun.generisiRacun(iz.getImeKorisnika(), iz.getDokument());
             	LocalDate dat = trenutak.toLocalDate();
             	racun.setDatum(dat);
             	//System.out.println("DATUM: " + dat);
             	System.out.println(iz.getDokument());
             	generisaniRacuni.add(racun);
            }

            // Pauza prije prelaska na sljedeću grupu
            Thread.sleep(VRIJEME_CEKANAJ_IZVRSAVANJA); 
            trenutnoIzn.clear();
        
        }
        generisiDnevniIzvjestaj(generisaniRacuni);
    }
    
    @FXML
    public void onBtnStartClicked() throws InterruptedException {
//        ArrayList<Iznajmljivanje> svaIznajmljivanja = Iznajmljivanje.citajPodatke();
        
        for (Iznajmljivanje iznajmljivanje : svaIznajmljivanja) {
            // Postavi controller i druge potrebne podatke
            iznajmljivanje.setController(this);

        }
        new Thread(() -> {
			try {
				pokreniKretanjeVozila(svaIznajmljivanja);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

  	    //pokreniKretanjeVozila(svaIznajmljivanja); // Zatvori scheduler kada su svi zadaci zakazani

    }

    
    public void postaviGridPane(GridPane gridPane) {
        labels = new Label[BROJ_REDOVA][BROJ_KOLONA];
        for (int i = 0; i < this.BROJ_REDOVA; i++) {
            for (int j = 0; j < this.BROJ_KOLONA; j++) {
                Label label = new Label();
                label.setStyle("-fx-border-color: black; -fx-alignment: center;");
                label.setId(i+","+j);
    
                gridPane.add(label, i, j);
                labels[i][j] = label; // Čuvamo referencu na labelu
                
            }
        }
        
    }
    
    public void setPodaci(ArrayList<Iznajmljivanje> izmami) {
        if (izmami != null && !izmami.isEmpty()) {
            int row = 0;
            for (Iznajmljivanje iznajmljivanje : izmami) {
                Label label = new Label(iznajmljivanje.getIdPrevoznogSredstva());
                label.setAlignment(Pos.BASELINE_CENTER);
                // Dodavanje labela u grid pane
                gridPaneMapa.add(label, 0, row++);
            }
        } else {
            Label noDataLabel = new Label("Nema podataka za prikaz.");
            gridPaneMapa.add(noDataLabel, 0, 0);
        }
    }

    public void removeLabelAtPosition(int x, int y) {
        Platform.runLater(() -> {
            // Ovdje se vrši izmena UI komponente
            Label label = getLabelAtPosition(x, y);
            if (label != null) {
                label.setText("");  // ili bilo koja druga operacija na labeli
            }
        });
    }

    public void updateLabelAtPosition(int x, int y, String voziloId) {
        //Label label = getLabelAtPosition(x, y);
  
            labels[x][y].setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-alignment: center;");
            labels[x][y].setText(voziloId);
        
    }

    public void addLabelAtPosition(int x, int y, String id, int baterija) {

    		//labels[x][y].setStyle("-fx-font-size: 8px;");
    		//labels[x][y].setText(id + " " + baterija + "%");
    		//labels[x][y].setText(id);
    		//Tooltip tooltip = new Tooltip("Baterija: " + baterija + "%");
    		//Tooltip.install(labels[x][y], tooltip);

            labels[x][y].setText(id + baterija);
            System.out.println(baterija);
            //labels[x][y].setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-alignment: center;");
            if(id.startsWith("A")) {
                labels[x][y].setStyle("-fx-background-color: lightgreen; -fx-border-color: black; -fx-alignment: center;");
                
            }else if(id.startsWith("B")) {
                labels[x][y].setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-alignment: center;");
                
            }else if(id.startsWith("T")) {
                labels[x][y].setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-alignment: center;");
                
            }
           
        }
    
    public Label getLabelAtPosition(int x, int y) {
        if (x >= 0 && x < BROJ_KOLONA && y >= 0 && y < BROJ_REDOVA) {
            return labels[x][y]; // Pristupamo direktno iz niza
        }
        return null;
    }
    
    //TAB ZA PRIKAZ KOMENTARA
    private ObservableList<Automobil> automobili = FXCollections.observableArrayList();
    private ObservableList<Bicikl> bicikli = FXCollections.observableArrayList();
    private ObservableList<Trotinet> trotineti = FXCollections.observableArrayList();
    
    private void inicijalizujTabele() {
        // Poveži kolone sa atributima
    	automobilId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        automobilProizvodjac.setCellValueFactory(new PropertyValueFactory<>("Proizvodjac"));
        automobilModel.setCellValueFactory(new PropertyValueFactory<>("Model"));
        automobilDatumNabavke.setCellValueFactory(new PropertyValueFactory<>("datumNabavke"));
        automobilCijena.setCellValueFactory(new PropertyValueFactory<>("Cijena"));
        automobilOpis.setCellValueFactory(new PropertyValueFactory<>("Opis"));
       // automobilVrsta.setCellValueFactory(new PropertyValueFactory<>("Vrste"));
        
        biciklId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        biciklProizvodjac.setCellValueFactory(new PropertyValueFactory<>("Proizvodjac"));
        biciklModel.setCellValueFactory(new PropertyValueFactory<>("Model"));
        biciklCijena.setCellValueFactory(new PropertyValueFactory<>("Cijena"));
        biciklDomet.setCellValueFactory(new PropertyValueFactory<>("Domet"));
        //biciklVrsta.setCellValueFactory(new PropertyValueFactory<>("Vrsta"));
        
        trotinetId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        trotinetProizvodjac.setCellValueFactory(new PropertyValueFactory<>("Proizvodjac"));
        trotinetModel.setCellValueFactory(new PropertyValueFactory<>("Model"));
        trotinetCijena.setCellValueFactory(new PropertyValueFactory<>("Cijena"));
        trotinetMaxBrzina.setCellValueFactory(new PropertyValueFactory<>("maxBrzina"));
        
        // Postavi ObservableList kao podatke za prikaz u tabelama
        tabelaAutomobil.setItems(automobili);
        tabelaBicikl.setItems(bicikli);
        tabelaTrotinet.setItems(trotineti);
    }
    
    public void ucitajVozila() {
      //vozila = Vozilo.citajPodatke();
 
        // Razvrstaj vozila po tipu
        automobili.addAll(vozila.stream()
                .filter(v -> v instanceof Automobil)
                .map(v -> (Automobil) v)
                .collect(Collectors.toList()));

        bicikli.addAll(vozila.stream()
                .filter(v -> v instanceof Bicikl)
                .map(v -> (Bicikl) v)
                .collect(Collectors.toList()));

        trotineti.addAll(vozila.stream()
                .filter(v -> v instanceof Trotinet)
                .map(v -> (Trotinet) v)
                .collect(Collectors.toList()));
    }
    
    
    //TRECI TAB
    //PRIKAZ KVAROVA

    @FXML
    private TableColumn<Vozilo, String> kolonaId;

    @FXML
    private TableColumn<Iznajmljivanje, String> kolonaKvar;

    @FXML
    private TableColumn<Vozilo, String> kolonaOpis;

    @FXML
    private TableColumn<Iznajmljivanje, LocalDate> kolonaVrijeme;

    @FXML
    private TableColumn<Vozilo, String> kolonaVrstaPrevoznogSredstva;
    
    @FXML
    private Tab prikazKvaraTab;

    @FXML
    private TableView<Object> tabelaCetvrta;
    
    private ObservableList<Object> svaVozila = FXCollections.observableArrayList();
    
    public void kvarovi() {
    	kolonaId.setCellValueFactory(new PropertyValueFactory<>("ID"));
    	//kolonaKvar.setCellValueFactory(new PropertyValueFactory<>("Kvar"));
    	kolonaOpis.setCellValueFactory(new PropertyValueFactory<>("Opis"));
    	//kolonaVrijeme.setCellValueFactory(new PropertyValueFactory<>("DatumIznajmljivanja"));
    	//kolonaVrstaPrevoznogSredstva.setCellValueFactory(new PropertyValueFactory<>("vrstaPrevoznogSredstva"));
    	
    	/*for(Vozilo v: svaVozila) {
    		System.out.println("vozilo:" + v);
    	}*/
    	
    	kolonaKvar.setCellFactory(column -> {
    	    return new TableCell<Iznajmljivanje, String>() {
    	        @Override
    	        protected void updateItem(String item, boolean empty) {
    	            super.updateItem(item, empty);

    	            if (empty) {
    	                setText(null);
    	            } else {
    	                Object obj = getTableView().getItems().get(getIndex());
    	                if (obj instanceof Iznajmljivanje) {
    	                    //setText("Nepoznato");
    	                	Iznajmljivanje iznajmljivanje = (Iznajmljivanje) obj;
    	                    String tekst = iznajmljivanje.isKvar() ? "da" : "ne";
    	                    setText(tekst);
    	                } else {
    	                	setText("da");
    	                    // Ako nije Iznajmljivanje, možeš odabrati da ostaviš polje prazno ili postaviš neki default tekst
    	                    
    	                }
    	            }
    	        }
    	    };
    	});


    	/*
    	kolonaVrijeme.setCellFactory(column -> {
    	    return new TableCell<Iznajmljivanje, LocalDate>() {
    	        @Override
    	        public void updateItem(LocalDate item, boolean empty) {
    	            super.updateItem(item, empty);

    	            if (empty || item == null) {
    	                 //Iznajmljivanje currentIznajmljivanje = getTableView().getItems().get(getIndex());

    	                //if (currentIznajmljivanje != null) {
    	                    // Prikazivanje datuma i vremena ili bilo kojeg drugog relevantnog podatka
    	                    //setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    	                //}
    	            	
    	            	setText(null); // Postavite praznu ćeliju kada nema podatka
    	                ///setGraphic(null);
    	            } else {
    	                // Dobijanje trenutnog objekta iz reda
    	            	System.out.println("srdaca");
    	            }
    	        }
    	    };
    	});
*/
    	
    	kolonaVrstaPrevoznogSredstva.setCellFactory(column -> {
    	    return new TableCell<Vozilo, String>() {
    	        @Override
    	        protected void updateItem(String item, boolean empty) {
    	            super.updateItem(item, empty);
    	            
    	            if (empty) {
    	                setText(null); // Prazne ćelije ne prikazuju ništa
    	            } else {
    	                Vozilo v = getTableView().getItems().get(getIndex());
    	                setText(v.getVrsta()); // Prikazivanje vrste vozila
    	            }
    	        }
    	    };
    	});


    	tabelaCetvrta.setItems(svaVozila);
    }
    
     public List<Vozilo> vozilaZaDodavanje = new ArrayList<>();
    public void dodajKvar() {
        // Privremena lista

        for (Iznajmljivanje iz : svaIznajmljivanja) {
            if (iz.isKvar()) {
                //System.out.println(iz.isKvar());
                for (Vozilo v : vozila) {
                    if (iz.getIdPrevoznogSredstva().equals(v.getID()) && !svaVozila.contains(v)) {
                        vozilaZaDodavanje.add(v); // Dodavanje u privremenu listu
                       // System.out.println("Dodano vozilo: " + v.getID());
                    }
                }
            }
        }

        svaVozila.addAll(vozilaZaDodavanje);
    }
    
//SUMARNI IZVJESTAJ
    @FXML
    private Tab tabPoslovanje;
    
    @FXML
    private Button btnPrikazSumarnogIzvjestaja;
    
    @FXML
    private Label lblIznosSvihVoznji;

    @FXML
    private Label lblIznosZaPopravkeKvarova;

    @FXML
    private Label lblPorez;

    @FXML
    private Label lblTroskoviKompanije;

    @FXML
    private Label lblUkupanPopust;

    @FXML
    private Label lblUkupanPrihod;

    @FXML
    private Label lblUkupnoPromocije;

    @FXML
    private Label lblUkupnoZaOdrzavanje;
    
    @FXML
    private void onBtnPrikazSumarnogIzvjestajaClicked() {
        prikaziSumarniIzvjestaj();
    }
    
    private void prikaziSumarniIzvjestaj() {
        // Promjenljive za sumiranje vrijednosti
        double ukupanPrihod = 0;
        double ukupanPopust = 0;
        double ukupnaPromocija = 0;
        double ukupanIznosVoznjiUziDio = 0;
        double ukupanIznosVoznjiSiriDio = 0;
        double ukupnoZaOdrzavanje = 0;
        double ukupnoZaPopravke = 0;

        // Prolazak kroz sve dnevne izvještaje i sumiranje
        for (DnevniIzvjestaj dnevniIzvjestaj : dnevniRacuni) {
            ukupanPrihod += dnevniIzvjestaj.getUkupanPrihod();
            ukupanPopust += dnevniIzvjestaj.getUkupanPopust();
            ukupnaPromocija += dnevniIzvjestaj.getUkupnaPromocija();
            ukupanIznosVoznjiUziDio += dnevniIzvjestaj.getUkupnoUziDioGrada();
            ukupanIznosVoznjiSiriDio += dnevniIzvjestaj.getUkupnoSiriDioGrada();
            ukupnoZaOdrzavanje += dnevniIzvjestaj.getIznosZaOdrzavanje();
            ukupnoZaPopravke += dnevniIzvjestaj.getIznosZaPopravke();
        }

        // Postavljanje sumiranih vrijednosti u labele
        lblUkupanPrihod.setText(String.format("%.2f", ukupanPrihod));
        lblUkupanPopust.setText(String.format("%.2f", ukupanPopust));
        lblUkupnoPromocije.setText(String.format("%.2f", ukupnaPromocija));
        lblIznosSvihVoznji.setText(String.format("%.2f", ukupanIznosVoznjiUziDio + ukupanIznosVoznjiSiriDio));
        lblUkupnoZaOdrzavanje.setText(String.format("%.2f", ukupnoZaOdrzavanje));
        lblIznosZaPopravkeKvarova.setText(String.format("%.2f", ukupnoZaPopravke));
        lblTroskoviKompanije.setText(String.format("%.2f", ukupanPrihod * 0.2)); // Pretpostavka
        lblPorez.setText(String.format("%.2f", (ukupanPrihod - ukupnoZaOdrzavanje - ukupnoZaPopravke) * 0.1)); // Pretpostavka
    }
    
	//DNEVNI IZVJESTAJ

    @FXML
    private TableColumn<DnevniIzvjestaj, Double> kolonaPopravke;

    @FXML
    private TableColumn<DnevniIzvjestaj, Number> kolonaPopust;

    @FXML
    private TableColumn<DnevniIzvjestaj, Double> kolonaPrihod;

    @FXML
    private TableColumn<DnevniIzvjestaj, Double> kolonaPromocija;

    @FXML
    private TableColumn<DnevniIzvjestaj, Double> kolonaIznosSvihVoznji;

    @FXML
    private TableColumn<DnevniIzvjestaj, Double> kolonaOdrzavanje;
    
    @FXML
    private TableColumn<DnevniIzvjestaj, String> kolonaDatum;

    @FXML
    private TableView<DnevniIzvjestaj> tabelaDnevniIzvjestaj;
    
    private ObservableList<DnevniIzvjestaj> dnevniRacuni = FXCollections.observableArrayList();
    
    public void dnevniIzvj() {
    	kolonaDatum.setCellValueFactory(new PropertyValueFactory<>("Datum"));
    	kolonaPrihod.setCellValueFactory(new PropertyValueFactory<>("UkupanPrihod"));
    	//kolonaPopust.setCellValueFactory(new PropertyValueFactory<>("UkupanPopusts"));
    	kolonaPromocija.setCellValueFactory(new PropertyValueFactory<>("UkupnaPromocija"));
    	kolonaIznosSvihVoznji.setCellValueFactory(new PropertyValueFactory<>("TroskoviKompanije"));
    	kolonaOdrzavanje.setCellValueFactory(new PropertyValueFactory<>("IznosZaOdrzavanje"));
    	kolonaPopravke.setCellValueFactory(new PropertyValueFactory<>("IznosZaPopravke"));
    	
    	kolonaPopust.setCellFactory(column -> {
    	    return new TableCell<DnevniIzvjestaj, Number>() {
    	        @Override
    	        protected void updateItem(Number item, boolean empty) {
    	            super.updateItem(item, empty);

    	            if (empty || item == null) {
    	                setText(null);
    	             // Prazne ćelije ne prikazuju ništa
    	            } else {
    	            	 setText(String.format("%.2f", item.doubleValue())); // Prikazivanje numeričke vrednosti popusta sa dva decimalna mesta
    	    	            
    	               }
    	        }
    	    };
    	});

    	
    	tabelaDnevniIzvjestaj.setItems(dnevniRacuni);
    }
    
    public void generisiDnevniIzvjestaj(ArrayList<GenerisiRacun> generisaniRacuni) {
        
    	   HashMap<LocalDate, DnevniIzvjestaj> dnevniIzvjestaji = new HashMap<>();
    	    for (GenerisiRacun racun : generisaniRacuni) {
    	        LocalDate datum = racun.getDatum();
    	        DnevniIzvjestaj dnevniIzvjestaj = dnevniIzvjestaji.getOrDefault(datum, new DnevniIzvjestaj(datum));
    	        dnevniIzvjestaj.dodajRacun(racun);
    	        dnevniIzvjestaji.put(datum, dnevniIzvjestaj);
    	    }

    	    // Izračunavanje izveštaja
    	    for (DnevniIzvjestaj izvjestaj : dnevniIzvjestaji.values()) {
    	        izvjestaj.izracunajIzvjestaj();
    	    }

    	    // Ažuriranje ObservableList
    	    dnevniRacuni.clear();
    	    dnevniRacuni.addAll(dnevniIzvjestaji.values());
    	    tabelaDnevniIzvjestaj.setItems(dnevniRacuni); // Postavljanje u TableView
 
    }
    //SERIJALIZACIJA


    @FXML
    private Tab tabSerijalizacija;
    
    @FXML
    private Button serijalizacijaBtn;

    @FXML
    private void onBtnSerijalizacijaClicked() {
    	serijalizacija();
    	System.out.println("Kliknuto je dugme serijalizacija");
    }
    
    Serijalizacija serijalizacija = new Serijalizacija();
    
    private void serijalizacija() {
        // Pretpostavljam da imate listu svih računa u kontroleru
        ArrayList<GenerisiRacun> sviRacuni = generisaniRacuni; // Implementirajte ovu metodu
        serijalizacija = new Serijalizacija();
        // Kreirajte instancu klase Serijalizacija
        //serijalizacija = new Serijalizacija();
 
        // Pronađite račune sa najvećim gubicima
        HashMap<String, GenerisiRacun> vozilaGubici = serijalizacija.pronadjiRacunSaNajviseGubitaka(sviRacuni);
        System.out.println(vozilaGubici);
        // Serijalizujte račune sa najvećim gubicima
        serijalizacija.serijalizujRacuneSaNajviseGubitaka(vozilaGubici);
        
  
    }
    
    @FXML
    private ComboBox<String> voziloComboBox;
    
    @FXML
    private Button btnDeserijalizacija;
    
    @FXML
    private TextArea prikazTextArea;
    
    @FXML
    private void onBtnDeserijalizacijaClicked() {
        //voziloComboBox.getItems().addAll("A", "T", "B"); // Dodaj vrste vozila koje želiš
    	deserijalizujPodatke();
    	System.out.println("Kliknuto dugme za deserijalizaciju");
    }
    
    private void deserijalizujPodatke() {
    	serijalizacija = new Serijalizacija();
        String tipVozila = voziloComboBox.getValue();
        if (tipVozila != null) {
            try {
                GenerisiRacun racun = serijalizacija.deserijalizujRacun(tipVozila);
                prikazTextArea.setText(racun.toString());
            } catch (IOException | ClassNotFoundException e) {
                prikazTextArea.setText("Greška pri deserijalizaciji: " + e.getMessage());
            }
        } else {
            prikazTextArea.setText("Izaberite tip vozila.");
        }
    }
}
    

    
   