package net.masa3mc.altcheck.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.masa3mc.altcheck.AltCheck;

public class DataMySQL {

	private static final AltCheck instance = AltCheck.instance;
	public static Connection connection;

	private static final String host = instance.getConfig().getString("Data.hostname");
	private static final String database = instance.getConfig().getString("Data.database");
	private static final String table = instance.getConfig().getString("Data.table");
	private static final int port = instance.getConfig().getInt("Data.port");
	private static final String username = instance.getConfig().getString("Data.username");
	private static final String password = instance.getConfig().getString("Data.password");

	public static Connection getConnection(String host, int port, String database, String username, String password)
			throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) {
			return connection;
		}
		synchronized (instance) {
			if (connection != null && !connection.isClosed()) {
				return connection;
			}
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username,
					password);
		}
		return connection;
	}

	public static boolean setData(String uuid, String IP) {
		try {
			executeUpdate("insert into " + table + "(UUID, IP) values('" + uuid + "', '" + IP + "')");
			return true;
		} catch (Exception e) {
			instance.getLogger().warning("An Exception in DataMySQL.setData()");
			instance.getLogger().warning(e.getMessage());
		}
		return false;
	}

	public static ResultSet getData() {
		return result("select * from " + table);
	}

	public static boolean deteleData(String uuid) {
		try {
			if (isExist(uuid, Type.UUID)) {
				executeUpdate("delete from " + table + " where UUID = '" + uuid + "'");
			}
			return true;
		} catch (Exception e) {
			instance.getLogger().warning("An Exception in DataMySQL.deleteData()");
			instance.getLogger().warning(e.getMessage());
		}
		return false;
	}

	private static void executeUpdate(String sql) {
		try {
			getConnection(host, port, database, username, password);
			Statement state = connection.createStatement();
			state.executeUpdate(sql);
			state.close();
		} catch (SQLException e) {
			instance.getLogger().warning("SQLException in DataMySQL.executeUpdate()");
			instance.getLogger().warning(e.getMessage());
		} catch (Exception e) {
			instance.getLogger().warning("An Exception in DataMySQL.executeUpdate()");
			instance.getLogger().warning(e.getMessage());
		}
	}

	private static ResultSet result(String sql) {
		try {
			getConnection(host, port, database, username, password);
			return connection.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			instance.getLogger().warning("SQLException in DataMySQL.result()");
			instance.getLogger().warning(e.getMessage());
		} catch (Exception e) {
			instance.getLogger().warning("An Exception in DataMySQL.result()");
			instance.getLogger().warning(e.getMessage());
		}
		return null;
	}

	private static boolean isExist(String data, Type type) {
		ResultSet result = getData();
		try {
			while (result.next()) {
				if (result.getString(type.name()).equals(data)) {
					return true;
				}
			}
			result.close();
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	public enum Type {
		IP, UUID;
	}
}
