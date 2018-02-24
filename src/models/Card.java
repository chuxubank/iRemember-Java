package models;

public abstract class Card {
	private int id;
	private String creattime;
	private String front;
	private String back;
	private int state;
	private String donetime;
	private int decknum;
	private String deckname;
	
	public Card(int id, String creattime, String front, String back, int state, String donetime, int decknum) {
		super();
		this.id = id;
		this.creattime = creattime;
		this.front = front;
		this.back = back;
		this.state = state;
		this.donetime = donetime;
		this.decknum = decknum;
	}

	public Card(int id, String creattime, String front, String back, int state, String donetime) {
		super();
		this.id = id;
		this.creattime = creattime;
		this.front = front;
		this.back = back;
		this.state = state;
		this.donetime = donetime;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreattime() {
		return creattime;
	}
	public void setCreattime(String creattime) {
		this.creattime = creattime;
	}
	public String getFront() {
		return front;
	}
	public void setFront(String front) {
		this.front = front;
	}
	public String getBack() {
		return back;
	}
	public void setBack(String back) {
		this.back = back;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getDonetime() {
		return donetime;
	}
	public void setDonetime(String donetime) {
		this.donetime = donetime;
	}
	public int getDecknum() {
		return decknum;
	}

	public void setDecknum(int decknum) {
		this.decknum = decknum;
	}

	public String getDeckname() {
		return deckname;
	}

	public void setDeckname(String deckname) {
		this.deckname = deckname;
	}
}
