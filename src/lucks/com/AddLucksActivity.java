package lucks.com;

import java.util.ArrayList;
import java.util.List;

import lucks.com.model.Language;
import lucks.com.model.Luck;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddLucksActivity extends Activity {

	private Luck newLuck;
	private List<Language> languages;
	private LucksApp app;
	
	private EditText addLuckTextView;
	private EditText addAuthorTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_luck);
		
		newLuck = new Luck();
		app = (LucksApp) getApplication();
		
		createAndFillSpinner();
		
		addLuckTextView = (EditText) findViewById(R.id.addLuck);
		addAuthorTextView = (EditText) findViewById(R.id.addAuthor);
		
		((Button) findViewById(R.id.okButton)).setOnClickListener(okListener);
		((Button) findViewById(R.id.cancelButton)).setOnClickListener(cancelListener);
	}
	
	public void createAndFillSpinner(){
		languages = new ArrayList<Language>();
		languages = app.getDataManager().getAllLanguages();
		String[] spinnerLanguages = new String[languages.size()];
		
		for(int i = 0; i < languages.size(); i++){
			spinnerLanguages[i] = languages.get(i).getLang();
		}
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerLanguages);
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				newLuck.setLuckLanguageId(pos + 1);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				//nothing to do
			}
		});
	}
	
	OnClickListener cancelListener = new OnClickListener() {
		public void onClick(View v) {
				finish();
		}
	};
	
	OnClickListener okListener = new OnClickListener() {
		public void onClick(View v) {
			if(addLuckTextView.getText().toString().length() == 0 || addLuckTextView.getText().toString().equals(null)){
				Toast.makeText(getBaseContext(), "Luck Text cannot be empty!", Toast.LENGTH_SHORT).show();
				addLuckTextView.requestFocus();
				return;
			}else{
				newLuck.setLuckText(addLuckTextView.getText().toString());
				if(addAuthorTextView.getText().toString().length() == 0){
					newLuck.setLuckAuthor("unknown author");
				}else{
					newLuck.setLuckAuthor(addAuthorTextView.getText().toString());
				}	
			}
			if(app.isOnline()){
				app.sendDataToServer(newLuck);
			}else{
				Toast.makeText(getBaseContext(), R.string.network_unavailable, Toast.LENGTH_LONG).show();
			}
		}
	};
}
