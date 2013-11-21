package lucks.com.data;

import java.util.List;

import lucks.com.model.Language;
import lucks.com.model.Luck;

public interface DataManager {
	
	//language
	public long addLanguage(Language lang);
	
	public void updateCheckedLanguages(List<Language> langs);
	
	public void deleteLanguage(int id);
	
	public List<Language> getAllLanguages();
	
	//luck
	public long addNewLuck(Luck luck);
	
	public void updateLucks(List<Luck> lucks);
	
	public Luck getRandomLuck();
	List<Language> getAvailableLanguages();

}