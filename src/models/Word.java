package models;

public class Word extends Card {
	private String phonetic;
	
	public Word(int id, String creattime, String front, String back, int state, String donetime, int decknum,
			String phonetic) {
		super(id, creattime, front, back, state, donetime, decknum);
		this.phonetic = phonetic;
	}

	public String getPhonetic() {
		return phonetic;
	}

	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}
	
}
