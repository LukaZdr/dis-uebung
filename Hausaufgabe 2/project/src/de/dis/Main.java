package de.dis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import de.dis.data.DbConnectionManager;
import de.dis.data.Haus;
import de.dis.data.Makler;
import de.dis.data.Wohnung;

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
					System.out.println("Vertrag");
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
	
	
	
}
