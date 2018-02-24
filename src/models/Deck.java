package models;

public class Deck {
	private int id;
	private String name;
	private int numofcard;
	private int donenumofcard;

	public Deck(int id, String name, int numofcard, int donenumofcard) {
		super();
		this.id = id;
		this.name = name;
		this.numofcard = numofcard;
		this.donenumofcard = donenumofcard;
	}
	public int getNumofcard() {
		return numofcard;
	}
	public void setNumofcard(int numofcard) {
		this.numofcard = numofcard;
	}
	public int getDonenumofcard() {
		return donenumofcard;
	}
	public void setDonenumofcard(int donenumofcard) {
		this.donenumofcard = donenumofcard;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

//class DeckDAO {
//	private DBConnection ir;
//	
//	public DeckDAO() {
//		ir = new DBConnection();
//	}
//
//	private Deck Record2Entity(ResultSet rs) throws SQLException {
//		Deck tempd = new Deck();
//		tempd.setId(rs.getInt("id"));
//		tempd.setName(rs.getString("name"));
//		return tempd;
//	}
//	
//	public List<Deck> query(String sql,Object... args) {
//		
//		System.out.println(sql);
//		try {
//			ResultSet rs = ir.executeQuery(sql, null);
//			while (rs.next()) {
//				Deck tempc = Record2Entity(rs);
//				list.add(tempc);
//			}
//			rs.close();
//			ir.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
//}
