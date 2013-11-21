package lucks.com.model;

public class Luck {
	private int mLuckId;
	private int mLuckLanguageId;
	private String mLuckText;
	private String mLuckAuthor;
	
	public Luck(){
		this.mLuckId = 0;
		this.mLuckLanguageId = 0;
		this.mLuckText = "";
		this.mLuckAuthor = "";
	}
	
	public Luck(int luckId, int luckLanguageID,
			String luckText, String luckAuthor){
		this.mLuckId = luckId;
		this.mLuckLanguageId = luckLanguageID;
		this.mLuckText = luckText;
		this.mLuckAuthor = luckAuthor;
	}
	
	public void setId(int luckId){
		this.mLuckId = luckId;
	}
	
	public int getId(){
		return this.mLuckId;
	}
	
	public void setLuckLanguageId(int luckLanguageId){
		this.mLuckLanguageId = luckLanguageId;
	}
	
	public int getluckLanguageId(){
		return this.mLuckLanguageId;
	}
	
	public void setLuckText(String luckText){
		this.mLuckText = luckText;
	}
	
	public String getLuckText(){
		return this.mLuckText;
	}
	
	public void setLuckAuthor(String luckAuthor){
		this.mLuckAuthor = luckAuthor;
	}
	
	public String getLuckAuthor(){
		return this.mLuckAuthor;
	}
}