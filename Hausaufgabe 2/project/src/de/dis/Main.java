package de.dis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.sql.Date;
import java.util.List;

import de.dis.data.DbConnectionManager;
import de.dis.data.Haus;
import de.dis.data.Makler;
import de.dis.data.Wohnung;
import de.dis.data.Kaufvertrag;
import de.dis.data.Mietvertrag;
import de.dis.data.Person;

/**
 * Hauptklasse
 */
public class Main {
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}
	
	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_MAKLER = 0;
		final int MENU_IMMOBILIE = 1;
		final int MENU_VERTRAG = 2;
		final int QUIT = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Immobilien-Verwaltung", MENU_IMMOBILIE);
		mainMenu.addEntry("Vertrags-Verwaltung", MENU_VERTRAG);
		mainMenu.addEntry("Beenden", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_MAKLER:
					loginAdmin();
					break;
				case MENU_IMMOBILIE:
					loginMakler();
					break;
				case MENU_VERTRAG:
					showVertragsMenu();
					break;
				case QUIT:
					return;
			}
		}
	}

	/**
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * ============================== Maklerverwaltung ==============================
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	
	/**
	 * login with hardcoded password
	 */
	
	public static void loginAdmin() {
		int  tries_left = 2;
		System.out.println("Gebe das Adminpassword ein");
		String password = FormUtil.readString("Passwort");
		
		while (tries_left > 0) {
			if (Objects.equals(password, "immopasswort")) {
				showMaklerMenu();
			} else{
				System.out.println("Falsches Passwort noch " + tries_left + " Versuche");
				password = FormUtil.readString("Passwort");
				tries_left -= 1;
			}
		}
		System.out.println("Passwort zu oft falsch eingegeben");
		return;
	}
	
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public static void showMaklerMenu() {
		//Menüoptionen
		final int NEW_MAKLER = 0;
		final int DELETE_MAKLER = 1;
		final int BACK = 2;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
		maklerMenu.addEntry("Makler L�schen", DELETE_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_MAKLER:
					newMakler();
					break;
				case DELETE_MAKLER:
					deleteMarkler();
				case BACK:
					showMainMenu();
					return;
			}
		}
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void newMakler() {
		Makler m = new Makler();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde erzeugt.");
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void deleteMarkler() {
		int id = FormUtil.readInt("MarklerId");
		Makler.delete(id);
		System.out.println("Makler mit der ID "+id+" wurde gel�scht.");
	}
	
	/**
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * ============================== Immobilienverwaltung ==============================
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	
	/**
	 * Zeigt das Immobilienmenü
	 */
	public static void showEstateMenu() {
		//Menüoptionen
		final int NEW_ESTATE = 0;
		final int EDIT_ESTATE = 1;
		final int DELETE_ESTATE = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu estateMenu = new Menu("Estate-Verwaltung");
		estateMenu.addEntry("Neue Immobilie", NEW_ESTATE);
		estateMenu.addEntry("Immobilie editieren", EDIT_ESTATE);
		estateMenu.addEntry("Immobilie löschen", DELETE_ESTATE);
		estateMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();
			
			switch(response) {
				case NEW_ESTATE:
					newEstate();
					break;
				case EDIT_ESTATE:
					editEstate();
					break;	
				case DELETE_ESTATE:
					//deleteEstate();
					break;
				case BACK:
					return;
			}
		}
	}
	
	
	public static void newEstate() {
		
		System.out.println("Bitte gib 1 für Haus oder 2 für Wohnung ein!:");
		int auswahl = FormUtil.readInt("Auswahl");
		
		if (auswahl == 1) {
			Haus h = new Haus();
			
			//Immobilie
			h.setId(FormUtil.readInt("Id"));
			h.setCity(FormUtil.readString("Stadt"));
			h.setPostalCode(FormUtil.readInt("Postleitzahl"));
			h.setStreet(FormUtil.readString("Straße"));
			h.setStreetNumber(FormUtil.readInt("Straßennummer"));
			h.setSquareArea(FormUtil.readInt("Square-Area"));
			
            //Haus
            h.setFloors(FormUtil.readInt("Etagen"));
            h.setPrice(FormUtil.readString("Preis"));
            boolean hasGarden = Boolean.parseBoolean(FormUtil.readString("Garten? (True/False)"));
            h.setGarden(hasGarden);
            h.save();
            
            System.out.println("Immobilie mit der ID " + h.getId() + " wurde erzeugt.");
			
		} else if (auswahl == 2) {
			Wohnung w = new Wohnung();
			w.setId(FormUtil.readInt("Id"));
			w.setCity(FormUtil.readString("Stadt"));
			w.setPostalCode(FormUtil.readInt("Postleitzahl"));
			w.setStreet(FormUtil.readString("Straße"));
			w.setStreetNumber(FormUtil.readInt("Straßennummer"));
			w.setSquareArea(FormUtil.readInt("Square-Area"));
            
            w.setFloor(FormUtil.readInt("Stockwerk"));
            w.setRent(FormUtil.readString("Miete"));
            w.setRooms(FormUtil.readInt("Anzahl Räume"));
            w.setBalcony(FormUtil.readInt("Balkonanzahl"));
            
            boolean hasBuiltInKitchen = Boolean.parseBoolean(FormUtil.readString("Einbauküche? (True/False)"));
            w.setBuiltInKitchen(hasBuiltInKitchen);
           
            w.save();
          
		} else {
			System.out.println("Bitte gib 1 für Haus oder 2 für Wohnung ein!:");
            newEstate();
		}
			
			
		}
	
	   public static void editEstate() {
	        System.out.println("Möchtest du 1. Haus oder 2. Appartment bearbeiten? Bitte gebe 1 oder 2 ein:");
	        int auswahl = FormUtil.readInt("Auswahl");
	        if (auswahl == 1) {
	            Haus h = new Haus();
	            	//Immobilie
	                System.out.println("Bitte gib die ID der zu ändernden Immobilie an");
	                h.setId(FormUtil.readInt("ID"));
	                h.setCity(FormUtil.readString("Stadt"));
	                h.setPostalCode(FormUtil.readInt("Postleitzahl"));
	                h.setStreet(FormUtil.readString("Straße"));
	                h.setStreetNumber(FormUtil.readInt("Hausnummer"));
	                h.setSquareArea(FormUtil.readInt("Square-Area"));
	                //Haus
	                h.setFloors(FormUtil.readInt("Stockwerkanzahl"));
	                h.setPrice(FormUtil.readString("Preis"));
	                boolean hasGarden = Boolean.parseBoolean(FormUtil.readString("Garten? (True/False)"));
	                h.setGarden(hasGarden);
	                h.save();
	        }

	        else if (auswahl == 2) {
	            Wohnung w = new Wohnung();
	            
	                System.out.println("Bitte gib die ID der zuändernden Immobilie an");
	                w.setId(FormUtil.readInt("ID"));
	                w.setCity(FormUtil.readString("Stadt"));
	                w.setPostalCode(FormUtil.readInt("Postleitzahl"));
	                w.setStreet(FormUtil.readString("Straße"));
	                w.setStreetNumber(FormUtil.readInt("Hausnummer"));
	                w.setSquareArea(FormUtil.readInt("Square-Area"));
	                
	                
	                System.out.println("Bitte gib die ID des zugehörigen Maklers an:");
	                w.setAgentId(FormUtil.readInt("Makler-ID"));
	                w.setFloor(FormUtil.readInt("Stockwerk"));
	                w.setRent(FormUtil.readString("Mietkosten"));
	                w.setRooms(FormUtil.readInt("Anzahl Räume"));
	                w.setBalcony(FormUtil.readInt("Balkonanzahl"));
	                
	                boolean hasBuiltInKitchen = Boolean.parseBoolean(FormUtil.readString("Einbauküche? (True/False)"));
	                w.setBuiltInKitchen(hasBuiltInKitchen);
	                w.save();

	            }
	         else {
	            System.out.println("Bitte gib 1 für Haus oder 2 für Appartment ein!");
	            
	        }
	   
}
	
	
	public static void loginMakler() {
		
		try {
		
			int  tries_left = 3;
			System.out.println("Authentifiziere dich als Makler");
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			while (tries_left > 0) {
				String login = FormUtil.readString("Login");
				
				String selectSQL = "SELECT password FROM estate_agents WHERE login = ?";
				PreparedStatement pstmt = con.prepareStatement(selectSQL);
				pstmt.setString(1, login);
	
				// Führe Anfrage aus
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					String agent_password = rs.getString("password");
					while (tries_left > 0) {
						String password = FormUtil.readString("Passwort");
						if (Objects.equals(password, agent_password)) {
							showEstateMenu();
						} else{
							tries_left -= 1;
							System.out.println("Falsche Login-Passwort Kombination noch " + tries_left + " Versuche");
						}
					}
				}
				else {
					System.out.println("User existiert nicht");
				}
			}
			System.out.println("Passwort zu oft falsch eingegeben");
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
//	public static void showImmobilienMenu() {
//		// Menu optionen
//		final int N
//	}

	/**
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * ============================== Vertragsverwaltung ============================
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	
	/**
	 * Zeigt die Vertragsverwaltung
	 */
	public static void showVertragsMenu() {
		//Menüoptionen
		final int NEW_PERSON = 0;
		final int CREATE_TENANCY_CONTRACT = 1;
		final int CREATE_PURCHASE_CONTRACT = 2;
		final int CONTRACT_OWERVIEW = 3;
		final int BACK = 4;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Vertrags-Verwaltung");
		maklerMenu.addEntry("Neue Person", NEW_PERSON);
		maklerMenu.addEntry("Neuer Mietvertrag", CREATE_TENANCY_CONTRACT);
		maklerMenu.addEntry("Neue Kaufvertrag", CREATE_PURCHASE_CONTRACT);
		maklerMenu.addEntry("Vertrags�bersicht", CONTRACT_OWERVIEW);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_PERSON:
					newPerson();
					break;
				case CREATE_TENANCY_CONTRACT:
					createTenancyContract();
					break;
				case CREATE_PURCHASE_CONTRACT:
					createPurchaseContract();
					break;
				case CONTRACT_OWERVIEW:
					contractOverview();
					break;
				case BACK:
					showMainMenu();
					return;
			}
		}
	}
	
	/**
	 * Neue Person
	 */

	public static void newPerson() {
		Person p = new Person();
		
		p.setFirstName(FormUtil.readString("Vorname"));
		p.setLastName(FormUtil.readString("Nachname"));
		p.setAddress(FormUtil.readString("Adresse"));
		p.save();
		
		System.out.println("Person mit der ID "+p.getId()+" wurde erzeugt.");
	}
	public static void createTenancyContract() {
		Mietvertrag m = new Mietvertrag();
		
		m.setContractDate(Date.valueOf(FormUtil.readString("Vertragsdatum (YYY-dd-mm)")));
		m.setPlace(FormUtil.readString("Ort"));
		m.setStartDate(Date.valueOf(FormUtil.readString("Vertragsbeginn (YYY-dd-mm)")));
		m.setDuration(FormUtil.readString("Vertragsdauer"));
		float kosten = Float.parseFloat(FormUtil.readString("Zus�tzliche Kosten"));
		m.setAdditionalCosts(kosten);
		m.setPersonId(FormUtil.readInt("PersonId"));
		m.setApartmentId(FormUtil.readInt("ApartmentId"));
		m.save();
		
		System.out.println("Mietvertrag mit der ID "+m.getId()+" wurde erzeugt.");
	}
	
	public static void createPurchaseContract() {
		Kaufvertrag k = new Kaufvertrag();

		
		k.setContractDate(Date.valueOf(FormUtil.readString("Vertragsdatum (YYY-dd-mm)")));
		k.setPlace(FormUtil.readString("Ort"));
		k.setInstallmentNumber(FormUtil.readInt("Ratennummer"));
		float zinssatz = Float.parseFloat(FormUtil.readString("Zinssatz"));
		k.setInterestRate(zinssatz);
		k.setPersonId(FormUtil.readInt("PersonId"));
		k.setHouseId(FormUtil.readInt("HouseId"));
		k.save();
		
		System.out.println("Kaufvertrag mit der ID "+k.getId()+" wurde erzeugt.");
	}
	
	public static void contractOverview() {
		System.out.println("Mietvertr�ge:");
		List<Mietvertrag> ms = Mietvertrag.index();
        for (int i = 0; i < ms.size(); i++) {
        	Mietvertrag m = ms.get(i);
        	System.out.println("Id: " + m.getId() +
        					   " |Vertragsnummer:" + m.getContractNumber() +
        					   " |Ort:" + m.getPlace() +
        					   " |Vertragsdatum:" + m.getContractDate() +
        					   " |Vertragsbegin:" + m.getStartDate() +
        					   " |Vertragsdauer:" + m.getDuration() +
        					   " |Zus�tzliche Kosten:" + m.getAdditionalCosts() +
           					   " |PersonID:" + m.getPersonId() +
        					   " |ApartmentID:" + m.getApartmentId());
        }

		System.out.println("Kaufvertr�ge:");
		List<Kaufvertrag> ks = Kaufvertrag.index();
        for (int i = 0; i < ks.size(); i++) {
        	Kaufvertrag k = ks.get(i);
        	System.out.println("Id: " + k.getId() +
        					   " |Vertragsnummer:" + k.getContractNumber() +
        					   " |Ort:" + k.getPlace() +
        					   " |Vertragsdatum:" + k.getContractDate() +
        					   " |Ratennummer:" + k.getInstallmentNumber() +
        					   " |Zinssatz:" + k.getInterestRate() +
           					   " |PersonID:" + k.getPersonId() +
        					   " |HausID:" + k.getHouseId());
        }
	}
}
