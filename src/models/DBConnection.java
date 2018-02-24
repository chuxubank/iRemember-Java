package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
//	static private DBConnection instance = null;
	private static String conn_str = null;
    private Connection conn = null;
    
	public DBConnection() {
		conn_str="jdbc:sqlite:iRemember.db";
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(conn_str,"root","");
			conn.setAutoCommit(true);
			System.out.println("Open database successfully!");
			Statement stmt = conn.createStatement();
			String sql_creatdc = "CREATE TABLE IF NOT EXISTS deck("
					+ "ID		INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "NAME		TEXT					NOT NULL)";
			String sql_creatcd = "CREATE TABLE IF NOT EXISTS card("
					+ "ID		INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "FRONT	TEXT					NOT NULL,"
					+ "BACK		TEXT					NOT NULL,"
					+ "CREATTIME	DATATIME,"
					+ "DONETIME	DATATIME,"
					+ "STATE	INT,"
					+ "DECKNUM	INT)";
			stmt.executeUpdate(sql_creatdc);
			stmt.executeUpdate(sql_creatcd);
			stmt.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName()+":"+e.getMessage());
			System.exit(0);
		}
	}
	
//	public static DBConnection getInstance() {
//		if (instance == null) {
//			instance = new DBConnection();
//		}
//		return instance;
//	}
	//ǿ�мӵ�getConnection �о����и��õķ���
	public Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(conn_str,"root","");
		System.out.println("Open database successfully!");
		return conn;
	}
	  
	public Object Select2Object(String sql) throws SQLException {
        Object result = null;
        ResultSet rs = null;
        Connection conn = null;
		PreparedStatement stat = null;
        try {
        	conn = getConnection();
			stat = conn.prepareStatement(sql);
            rs = stat.executeQuery();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        }finally {
        	close(rs, stat, conn);
        }
    }
	
	/**
     * ���ص������ֵ����count\min\max��
     * 
     * @param sql
     *            sql���
     * @param paramters
     *            �����б�
     * @return ���
     * @throws SQLException
     */
    public Object Select2Object(String sql, Object... args) throws SQLException {
    	Object result = null;
        ResultSet rs = null;
        Connection conn = null;
		PreparedStatement stat = null;
        try {
        	conn = getConnection();
			stat = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
            	stat.setObject(i + 1, args[i]);
            }
            rs = stat.executeQuery();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            close(rs,stat,conn);
        }
    }
	
	public ResultSet ExecuteQuery(String sql, Object... args) {
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = getConnection();
			stat = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				stat.setObject(i + 1, args[i]);
			}
			System.out.println(stat.toString());
			rs = stat.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stat, conn);
		}
		return rs;
	}
	
	public ResultSet ExecuteQuery(String sql) throws SQLException {
		ResultSet rs = null;
		Connection conn = null;
		Statement stat = null;
		try {
			conn = getConnection();
			stat = conn.createStatement();
			System.out.println(stat.toString());
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			stat.close();
			conn.close();
		}
		return rs;
	}
	
	//����ѧ�귺������
//	/**
//	 * ��ѯselect
//	 * @throws Exception 
//	 */
//	@SuppressWarnings("rawtypes")
//	public List executeQuery(String sql, Object... args) {
//		List list=null;
//		ResultSet rs = null;
//		Connection conn = null;
//		PreparedStatement stat = null;
//		try {
//			conn = getConnection();
//			stat = conn.prepareStatement(sql);
//			for (int i = 0; i < args.length; i++) {
//				stat.setObject(i + 1, args[i]);
//			}
//			System.out.println(stat.toString());
//			rs = stat.executeQuery();
//			list=resultSetToList(rs);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			close(stat, conn);
//		}
//		return list;
//	}
	
	/**
	 * ִ��insert update delete���
	 * 
	 * @param sql
	 */
	public int ExecuteSQL(String sql, Object... args) {
		int result=0;
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = getConnection();
			stat = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				stat.setObject(i + 1, args[i]);
			}
			System.out.println(stat.toString());
			result=stat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stat, conn);
		}
		return result;
	}

	
	public void close() {
		try {
			conn.close();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		System.out.println("Close database successfully!");
	}
	
	/**
	 * �ͷ�����
	 * 
	 * @param stat
	 * @param conn
	 */
	private void close(PreparedStatement stat, Connection conn) {
		close(null, stat, conn);
	}

	/**
	 * �ͷ�����
	 * 
	 * @param rs
	 * @param stat
	 * @param conn
	 */
	private void close(ResultSet rs, PreparedStatement stat, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stat != null) {
					stat.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Close database successfully!");
	}
	
//	/**
//	 * ��resultsetת��ΪList
//	 * */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private  List resultSetToList(ResultSet rs) throws java.sql.SQLException {   
//        if (rs == null)   
//            return Collections.EMPTY_LIST;   
//        ResultSetMetaData md = rs.getMetaData(); 
//        int columnCount = md.getColumnCount(); 
//        List list = new ArrayList();   
//        //��map���뼯���з���ʹ�ø���Ĳ�ѯ
//        Map rowData = new HashMap();   
//        while (rs.next()) {   
//         rowData = new HashMap(columnCount);
//         //�����Ϸ���map��
//         for (int i = 1; i <= columnCount; i++) {   
//                 rowData.put(md.getColumnName(i), rs.getObject(i));   
//         }   
//         list.add(rowData);   
//        }  
//        return list;   
//	}
	//������
	public static void main(String[] args) throws SQLException {
		DBConnection ir = null;
		ResultSet rs = null;
		try {
			ir = new DBConnection();
			Statement stmt = ir.conn.createStatement();
			rs = stmt.executeQuery("select * from deck;");
			while (rs.next()) {
				String name = rs.getString("name");
				int id = rs.getInt("id");
				
				System.out.printf("name = %s; occupation = %d\n",
					name, id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ir.close();
//		DBConnection ir = null;
//		ResultSet rs = null;
//		try {
//			ir = new DBConnection();
//			rs = ir.ExecuteQuery(sql);
//			while (rs.next()) {
//				String name = rs.getString("name");
//				int id = rs.getInt("id");
//				
//				System.out.printf("name = %s; occupation = %d\n",
//					name, id);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
}
