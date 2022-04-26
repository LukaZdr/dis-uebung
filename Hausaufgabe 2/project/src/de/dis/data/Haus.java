package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Haus {
	// Immobilie
	private String city;
	private int postalCode;
	private String street;
	private int streetNumber;
	private int squareArea;
	private int agentId;
	// Haus
	private int id = -1;
	private int floors;
	private float price;
	private boolean garden;
	private int estateId;

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
	
	public int getFloors() {
		return floors;
	}

	public void setFloors(int floors) {
		this.floors = floors;
	}

	// Haus

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean isGarden() {
		return garden;
	}

	public void setGarden(boolean garden) {
		this.garden = garden;
	}
	
	public int getEstateId() {
		return estateId;
	}

	public void setEstateId(int estateId) {
		this.estateId = estateId;
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
				String insertHausSQL = "INSERT INTO houses (id, floors, price, garden, estate_id) VALUES (?, ?, ?, ?, ?)";

				PreparedStatement pstmtHaus = con.prepareStatement(insertHausSQL,
						Statement.RETURN_GENERATED_KEYS);
				
				pstmtHaus.setInt(1, getId());
				pstmtHaus.setInt(2, getFloors());
				pstmtHaus.setFloat(3, getPrice());
				pstmtHaus.setBoolean(4, isGarden());
				pstmtHaus.setInt(5, dbImmoId);
				ResultSet rsHaus = pstmtHaus.getGeneratedKeys();
				
				if (rsHaus.next()) {
					setId(rsHaus.getInt(1));
				}
				
				rsHaus.close();
				pstmtHaus.close();
				pstmtImmo.close();
				System.out.println("Immobilie mit der ID " + getId() + " wurde erzeugt.");

			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateHausSQL = "UPDATE houses SET floors = ?, price = ?, garden = ?, estate_id = ? WHERE id = ?";
				PreparedStatement pstmtHaus = con.prepareStatement(updateHausSQL);

				String selectSQL = "SELECT estate_id FROM houses WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, getId());
				ResultSet rs = pstmt.executeQuery();
				int estateId = -1;
				if (rs.next()) {
					estateId = rs.getInt("estate_id");
				} else {
					System.out.println("Immobilie konnte nicht gefunden werden");
					return;
				}
				
				// Setze Anfrage Parameter
				pstmtHaus.setInt(1, getFloors());
				pstmtHaus.setFloat(2, getPrice());
				pstmtHaus.setBoolean(3, isGarden());
				pstmtHaus.setInt(4, estateId);
				pstmtHaus.setInt(5, getId());
				pstmtHaus.executeUpdate();
				
				String updateImmoSQL = "UPDATE estates SET city = ?, postal_code = ?, street = ?, street_number = ?, square_area = ?, agent_id = ? WHERE id = ?";
				PreparedStatement pstmtImmo = con.prepareStatement(updateImmoSQL);

				pstmtImmo.setString(1, getCity());
				pstmtImmo.setInt(2, getPostalCode());
				pstmtImmo.setString(3, getStreet());
				pstmtImmo.setInt(4, getStreetNumber());
				pstmtImmo.setInt(5, getSquareArea());
				pstmtImmo.setInt(6, getAgentId());
				pstmtImmo.setInt(7, estateId);
				
				pstmtImmo.close();
				pstmtHaus.close();
				System.out.println("Das Haus mit der Id " + getId() + " wurde geupdated");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
public static void delete(int id) {
		
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();
			
			String estateIdSQL = "SELECT estate_id FROM houses WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(estateIdSQL);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			int delete_id;
			if (rs.next()) {
				delete_id = rs.getInt("estate_id");
			} else {
				System.out.println("Ein Haus mit dieser ID existiert nicht");
				return;
			}

			// Erzeuge Anfrage
			String deleteSQL = "DELETE FROM estates WHERE id = ?";
			PreparedStatement pstmtDelete = con.prepareStatement(deleteSQL);
			pstmtDelete.setInt(1, delete_id);

			
			// FÃ¼hre Anfrage aus
			pstmtDelete.execute();
			pstmtDelete.close();
			pstmt.close();
			System.out.println("Haus mit der Id" + id + "wurde gelöscht");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}