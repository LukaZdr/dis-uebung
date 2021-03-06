package de.dis.data;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Kaufvertrag {
	// Vertrag
	private Date contractDate;
	private String place;
	// Mietvertrag
	private int id = -1;
	private int installmentNumber;
	private float interestRate;
	private int contractNumber;
	// Verk?ufe
	private int houseId;
	private int personId;

	// Vertrag
	public Date getContractDate() {
		return contractDate;
	}
	public void setContractDate(Date contractDate) {
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
	public int getInstallmentNumber() {
		return installmentNumber;
	}
	public void setInstallmentNumber(int installmentNumber) {
		this.installmentNumber = installmentNumber;
	}
	public float getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}
	public int getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}
	
	// Verk?ufe
	public int getHouseId() {
		return houseId;
	}
	public void setHouseId(int houseId) {
		this.houseId = houseId;
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
			// create contracts record
			String insertMietSQL = "INSERT INTO contracts (contract_date, place) VALUES (?, ?)";
			
			PreparedStatement pstmtContract = con.prepareStatement(insertMietSQL,
					Statement.RETURN_GENERATED_KEYS);

			// Setze Anfrageparameter und fC<hre Anfrage aus
			pstmtContract.setDate(1, getContractDate());
			pstmtContract.setString(2, getPlace());
			pstmtContract.executeUpdate();

			// Hole die Id des engefC<gten Datensatzes
			ResultSet rsContract = pstmtContract.getGeneratedKeys();
			int dbContractId = -1;
			if (rsContract.next()) {
				dbContractId = rsContract.getInt(1);
				setContractNumber(dbContractId);
			}
			// create purchase_contracts record
			String insertPurchaseSQL = "INSERT INTO purchase_contracts (installment_number, interest_rate, contract_number) VALUES (?, ?, ?)";

			PreparedStatement pstmtPurchase = con.prepareStatement(insertPurchaseSQL,
					Statement.RETURN_GENERATED_KEYS);
			
			pstmtPurchase.setInt(1, getInstallmentNumber());
			pstmtPurchase.setFloat(2, getInterestRate());
			pstmtPurchase.setInt(3, getContractNumber());
			pstmtPurchase.executeUpdate();

			// Hole die Id des engefC<gten Datensatzes
			ResultSet rsPurchase = pstmtPurchase.getGeneratedKeys();
			int dbPurchaseId = -1;
			if (rsPurchase.next()) {
				dbPurchaseId = rsPurchase.getInt(1);
				setId(dbPurchaseId);
			}
			rsPurchase.close();
			
			// create sell record
			String insertSellsSQL = "INSERT INTO sells (purchase_contract_id, house_id, person_id) VALUES (?, ?, ?)";
			PreparedStatement pstmtSell = con.prepareStatement(insertSellsSQL,
					Statement.RETURN_GENERATED_KEYS);
			
			pstmtSell.setInt(1, getId());
			pstmtSell.setInt(2, getHouseId());
			pstmtSell.setInt(3, getPersonId());
			pstmtSell.executeUpdate();
			
			pstmtSell.close();
			pstmtPurchase.close();
			pstmtContract.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Kaufvertrag> index() {
		Connection con = DbConnectionManager.getInstance().getConnection();
		try {
			String selectJoinSQL = "SELECT * FROM purchase_contracts AS pc JOIN contracts AS c ON pc.contract_number = c.contract_number JOIN sells AS s ON pc.id = s.purchase_contract_id";
			PreparedStatement pstmt = con.prepareStatement(selectJoinSQL);
			ResultSet rs = pstmt.executeQuery();
			
			List<Kaufvertrag> purchaseList = new ArrayList<Kaufvertrag>();
			while (rs.next()) {
				Kaufvertrag k = new Kaufvertrag();
				k.setContractDate(rs.getDate("contract_date"));
				k.setPlace(rs.getString("place"));
				// Mietvertrag
				k.setId(rs.getInt("id"));
				k.setInstallmentNumber(rs.getInt("installment_number"));
				k.setInterestRate(rs.getFloat("interest_rate"));
				k.setContractNumber(rs.getInt("contract_number"));
				// Vermietungen
				k.setHouseId(rs.getInt("house_id"));
				k.setPersonId(rs.getInt("person_id"));
				purchaseList.add(k);
				
			}
			return purchaseList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}