package lucks.com;

import lucks.com.model.Luck;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditLucksActivity extends Activity {

	private Luck luck;
	private LucksApp app;
	
	private EditText addLuckTextView;
	private EditText addAuthorTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_luck);
		
		((Button) findViewById(R.id.okButton)).setOnClickListener(okListener);
		((Button) findViewById(R.id.cancelButton)).setOnClickListener(cancelListener);
		
		addLuckTextView = (EditText) findViewById(R.id.editLuck);
		addAuthorTextView = (EditText) findViewById(R.id.editAuthor);
		app = (LucksApp) getApplication();
		
		Bundle extras = getIntent().getExtras();
		
		luck = new Luck();
		if(extras != null){
			luck.setId(extras.getInt("luckId"));
			luck.setLuckText(extras.getString("luck"));
			luck.setLuckAuthor(extras.getString("author"));
			luck.setLuckLanguageId(extras.getInt("language"));
		}
		
		addLuckTextView.setText(luck.getLuckText());
		addAuthorTextView.setText(luck.getLuckAuthor());
	}
	
	OnClickListener cancelListener = new OnClickListener() {
		public void onClick(View v) {
				finish();
		}
	};
	
	OnClickListener okListener = new OnClickListener() {
		public void onClick(View v) {
			luck.setLuckText(addLuckTextView.getText().toString());
			luck.setLuckAuthor(addAuthorTextView.getText().toString());
			
			app.sendDataToServer(luck);
		}
	};
}
