package de.dis.data;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Mietvertrag {
	// Vertrag
	private String contractDate;
	private String place;
	// Mietvertrag
	private int id = -1;
	private String startDate;
	private String duration;
	private String additionalCosts;
	private int contractNumber;
	// Vermietungen
	private int apartmentId;
	private int personId;
	
	// Vertrag
	public String getContractDate() {
		return contractDate;
	}
	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}

	// Mietvertrag
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getAdditionalCosts() {
		return additionalCosts;
	}
	public void setAdditionalCosts(String additionalCosts) {
		this.additionalCosts = additionalCosts;
	}
	public int getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}
	
	// Vermietungen
	public int getApartmentId() {
		return apartmentId;
	}
	public void setApartmentId(int apartmentId) {
		this.apartmentId = apartmentId;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	
	// create
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			// Achtung, hier wird noch ein Parameter mitgegeben,
			// damit spC$ter generierte IDs zurC<ckgeliefert werden!
			String insertMietSQL = "INSERT INTO contracts (contract_date, place) VALUES (?, ?)";
			
			PreparedStatement pstmtContract = con.prepareStatement(insertMietSQL,
					Statement.RETURN_GENERATED_KEYS);

			// Setze Anfrageparameter und fC<hre Anfrage aus
			pstmtContract.setDate(1, Date.valueOf(getContractDate()));
			pstmtContract.setString(2, getPlace());
			pstmtContract.executeUpdate();

			// Hole die Id des engefC<gten Datensatzes
			ResultSet rsContract = pstmtContract.getGeneratedKeys();
			int dbContractId = -1;
			if (rsContract.next()) {
				dbContractId = rsContract.getInt(1);
				setContractNumber(dbContractId);
			}
			String insertHausSQL = "INSERT INTO tenancy_contracts (start_date, duration, additional_costs, contract_number) VALUES (?, ?, ?, ?)";

			PreparedStatement pstmtTenancy = con.prepareStatement(insertHausSQL,
					Statement.RETURN_GENERATED_KEYS);
			
			pstmtTenancy.setDate(1, Date.valueOf(getStartDate()));
			pstmtTenancy.setString(2, getDuration());
			pstmtTenancy.setString(3, getAdditionalCosts());
			pstmtTenancy.setInt(4, getContractNumber());
			pstmtTenancy.executeUpdate();

			// Hole die Id des engefC<gten Datensatzes
			ResultSet rsTenancy = pstmtTenancy.getGeneratedKeys();
			int dbTenancyId = -1;
			if (rsTenancy.next()) {
				dbTenancyId = rsTenancy.getInt(1);
				setId(dbTenancyId);
			}
			rsTenancy.close();

			// create sell record
			String insertSellsSQL = "INSERT INTO rents (tenancy_contracts_id, apartment_id, person_id) VALUES (?, ?, ?)";
			PreparedStatement pstmtSell = con.prepareStatement(insertSellsSQL,
					Statement.RETURN_GENERATED_KEYS);
			
			pstmtSell.setInt(1, getId());
			pstmtSell.setInt(2, getApartmentId());
			pstmtSell.setInt(3, getPersonId());
			pstmtSell.executeUpdate();
			
			pstmtSell.close();
			pstmtTenancy.close();
			pstmtContract.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}