package ntut.csie.ezScrum.issue.sql.service.tool.internal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import ntut.csie.ezScrum.issue.sql.service.core.Configuration;
import ntut.csie.ezScrum.issue.sql.service.tool.ISQLControl;

public class MySQLControl implements ISQLControl {
	String mHost;
	String mPort;
	String mDbName;
	String mUser;
	String mPassword;
	Configuration mConfig = null;

	Connection mConnection = null;
	DataSource mDatatSource = null;

	String[] mKeys;
	
	public MySQLControl(Configuration config) {
		mConfig = config;
		mHost = config.getServerUrl();
		mPort = "3306";
		mUser = config.getDBAccount();
		mPassword = config.getDBPassword();
		mDbName = config.getDBName();
		loadDriver();
	}

	public MySQLControl(String host, String port, String dbName) {
		mHost = host;
		mPort = port;
		mDbName = dbName;
		loadDriver();
	}

	public void setUser(String user) {
		mUser = user;
	}

	public void setPassword(String password) {
		mPassword = password;
	}

	public void connection() {
		try {
			if (mDatatSource == null) {
				mDatatSource = ConnectionPoolManager.getInstance()
						.getConnectionPool("com.mysql.jdbc.Driver", getURL(),
								mUser, mPassword);
			}

			// 只有在Connection為null或者是Connection已經Close的情況下才進行Connection
			if (mConnection == null || mConnection.isClosed())
				mConnection = mDatatSource.getConnection();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			mDatatSource = null;
			ConnectionPoolManager.getInstance().RemoveConnectionPool(getURL());
		}
	}

	@Override
	public void connectionToServer() {
		try {
			mConnection = DriverManager.getConnection(getServerURL(), mUser,
					mPassword);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public String[] getKeys() {
		return mKeys;
	}

	private void setKeys(Statement statement, String query) throws SQLException {
		boolean result = statement.execute(query,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet keys = statement.getGeneratedKeys();

		if (keys.next()) {
			ResultSetMetaData _metadata = keys.getMetaData();
			int columnCount = _metadata.getColumnCount();
			mKeys = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				mKeys[i - 1] = keys.getString(i);
			}
		} else {
			mKeys = new String[0];
		}
	}

	public boolean execute(String query) {
		return execute(query, false);
	}

	public boolean execute(String query, boolean returnKeys) {
		try {
			Statement statement = mConnection.createStatement();
			if (returnKeys) {
				setKeys(statement, query);
			} else {
				return statement.execute(query);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * for insert, update, delete
	 */
	public boolean executeUpdate(String query) {
		return executeUpdate(query, false);
	}

	/**
	 * for insert, update, delete
	 */
	public boolean executeUpdate(String query, boolean returnKeys) {
		try {
			Statement statement = mConnection.createStatement();
			if (returnKeys) {
				setKeys(statement, query);
			} else {
				return statement.executeUpdate(query) > 0 ? true : false;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public ResultSet executeQuery(String query) {
		ResultSet result = null;
		try {
			Statement statement = mConnection.createStatement();
			result = statement.executeQuery(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return result;
	}

	public void close() {
		try {
			mConnection.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private String getURL() {
		return "jdbc:mysql://" + mHost + ":" + mPort + "/" + mDbName
				+ "?useUnicode=true&characterEncoding=utf8";
	}

	private String getServerURL() {
		return "jdbc:mysql://" + mHost + ":" + mPort;
	}

	private void loadDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public Connection getconnection() {
		return mConnection;
	}

}
