package kr.ac.kaist.kse.cc.heartratemonitor.infrastructure.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import kr.ac.kaist.kse.cc.heartratemonitor.domain.model.heartrate.HeartRate;

public class HeartRateJDBCPersistence extends JDBCPersistence {

	private static final String EEGInfo_INSERT_SQL = "insert into heartrate"
			+ "(name, value, time) values"
			+ "(?,?,?)";

	public void save(HeartRate heartRate) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = getConnection().prepareStatement(EEGInfo_INSERT_SQL);

			int index = 1;
			preparedStatement.setString(index++, heartRate.getName());
			preparedStatement.setInt(index++, heartRate.getValue());
			preparedStatement.setLong(index++, heartRate.getTime());
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
