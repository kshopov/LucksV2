package lucks.com.model;

public class Language {
	int id;
	boolean isChecked;
	String language;
	
	public Language(){
		this.id = 0;
		this.language = "";
		this.isChecked = false;
	}
	
	public Language(String lang){
		this.id = 0;
		this.language = lang;
		this.isChecked = false;
	}
	
	public String getLang(){
		return this.language;
	}
	
	public void setLang(String lang){
		this.language = lang;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public boolean getIsChecked(){
		return this.isChecked;
	}
	
	public void setIsChecked(boolean isChecked){
		this.isChecked = isChecked;
	}
}