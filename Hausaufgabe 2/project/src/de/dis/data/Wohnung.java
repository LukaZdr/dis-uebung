package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Wohnung {
	// Immobilie
	private String city;
	private int postalCode;
	private String street;
	private int streetNumber;
	private int squareArea;
	private int agentId;
	// Wohnung
	private int id = -1;
	private int floor;
	private String rent;
	private int rooms;
	private int balcony;
	private boolean build_in_kitchen;
	private int estate_id;

	// Immobilie
	public int getId() {
		return id;
	}

	public void setId(int Id) {
		this.id = Id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public int getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}

	public int getSquareArea() {
		return squareArea;
	}

	public void setSquareArea(int squareArea) {
		this.squareArea = squareArea;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	// Wohnung
	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getRent() {
		return rent;
	}

	public void setRent(String rent) {
		this.rent = rent;
	}

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public int getBalcony() {
		return balcony;
	}

	public void setBalcony(int balcony) {
		this.balcony = balcony;
	}

	public boolean isBuild_in_kitchen() {
		return build_in_kitchen;
	}

	public void setBuild_in_kitchen(boolean build_in_kitchen) {
		this.build_in_kitchen = build_in_kitchen;
	}

	public int getEstate_id() {
		return estate_id;
	}

	public void setEstate_id(int estate_id) {
		this.estate_id = estate_id;
	}
	
	// create or update
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertImmoSQL = "INSERT INTO estates (city, postal_code, street, street_number, square_area, agent_id) VALUES (?, ?, ?, ?, ?, ?)";
				
				PreparedStatement pstmtImmo = con.prepareStatement(insertImmoSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmtImmo.setString(1, getCity());
				pstmtImmo.setInt(2, getPostalCode());
				pstmtImmo.setString(3, getStreet());
				pstmtImmo.setInt(4, getStreetNumber());
				pstmtImmo.setInt(5, getSquareArea());
				pstmtImmo.setInt(6, getAgentId());
				pstmtImmo.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rsImmo = pstmtImmo.getGeneratedKeys();
				int dbImmoId = -1;
				if (rsImmo.next()) {
					dbImmoId = rsImmo.getInt(1);
					setId(dbImmoId);
				}
				String insertHausSQL = "INSERT INTO apartments (id, floor, rent, rooms, balcony, build_in_kitchen, estate_id) VALUES (?, ?, ?, ?, ?)";

=
				PreparedStatement pstmtHaus = con.prepareStatement(insertHausSQL,
						Statement.RETURN_GENERATED_KEYS);
				
				pstmtHaus.setInt(1, getId());
				pstmtHaus.setInt(2, getFloor());
				pstmtHaus.setString(3, getPrice());
				pstmtHaus.setBoolean(4, isGarden());
				pstmtHaus.setInt(5, dbImmoId);
				ResultSet rsHaus = pstmtHaus.getGeneratedKeys();
				
				rsHaus.close();
				pstmtHaus.close();
				pstmtImmo.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateHausSQL = "UPDATE houses SET id = ?, floors = ?, price = ?, garden = ? WHERE id = ?";
				PreparedStatement pstmtHaus = con.prepareStatement(updateHausSQL);

				// Setze Anfrage Parameter
				pstmtHaus.setInt(1, getId());
				pstmtHaus.setInt(2, getFloors());
				pstmtHaus.setString(3, getPrice());
				pstmtHaus.setBoolean(4, isGarden());
				pstmtHaus.executeUpdate();
				
				String updateImmoSQL = "UPDATE estates SET city = ?, postal_code = ?, street = ?, street_number = ?, square_area = ?, agent_id = ? WHERE id = ?";
				PreparedStatement pstmtImmo = con.prepareStatement(updateImmoSQL);

				pstmtImmo.setString(1, getCity());
				pstmtImmo.setInt(2, getPostalCode());
				pstmtImmo.setString(3, getStreet());
				pstmtImmo.setInt(4, getStreetNumber());
				pstmtImmo.setInt(5, getSquareArea());
				pstmtImmo.setInt(6, getAgentId());
				pstmtImmo.setInt(7, getEstateId());
				
				pstmtImmo.close();
				pstmtHaus.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
public static void delete(int id) {
		
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			// Erzeuge Anfrage
			String deleteSQL = "DELETE FROM estates WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(deleteSQL);
			pstmt.setInt(1, id);
			
			// Führe Anfrage aus
			pstmt.executeQuery();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}