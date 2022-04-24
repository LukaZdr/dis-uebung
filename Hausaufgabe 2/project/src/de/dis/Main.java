package de.dis;

import java.util.Objects;

import de.dis.data.Kaufvertrag;
import de.dis.data.Makler;
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
					System.out.println("Immo");
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
	 * ========== Immobilien-Verwaltung ==========
	 */
	
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
					deleteMarkler();
					break;
				case BACK:
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
		
		m.setContractDate(FormUtil.readString("Vertragsdatum"));
		m.setPlace(FormUtil.readString("Ort"));
		m.setStartDate(FormUtil.readString("Vertragsbeginn"));
		m.setDuration(FormUtil.readString("Vertragsdauer"));
		m.setAdditionalCosts(FormUtil.readString("Zus�tzliche Kosten"));
		m.setPersonId(FormUtil.readInt("PersonId"));
		m.setApartmentId(FormUtil.readInt("ApartmentId"));
		m.save();
		
		System.out.println("Mietvertrag mit der ID "+m.getId()+" wurde erzeugt.");
	}
	
	public static void createPurchaseContract() {
		Kaufvertrag k = new Kaufvertrag();

		
		k.setContractDate(FormUtil.readString("Vertragsdatum"));
		k.setPlace(FormUtil.readString("Ort"));
		k.setInstallmentNumber(FormUtil.readInt("Ratennummer"));
		k.setInterestRate(FormUtil.readInt("Zinssatz"));
		k.setPersonId(FormUtil.readInt("PersonId"));
		k.setHouseId(FormUtil.readInt("HouseId"));
		k.save();
		
		System.out.println("Kaufvertrag mit der ID "+k.getId()+" wurde erzeugt.");
	}
}