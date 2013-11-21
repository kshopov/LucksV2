package lucks.com;

import java.util.ArrayList;
import java.util.List;

import lucks.com.model.Language;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SelectLanguageActivity extends ListActivity {
	LucksApp app;
	List<Language> list = new ArrayList<Language>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_lang);
		
		app = (LucksApp) getApplication();

		ArrayAdapter<Language> adapter = new InteractiveArrayAdapter(this, getLanguages());
		setListAdapter(adapter);

		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				app.getDataManager().updateCheckedLanguages(list);
				finish();
			}
		});

		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// nothing to do just return to main activity
				finish();
			}
		});
	}

	private List<Language> getLanguages() {
		list = app.getDataManager().getAvailableLanguages();
		
		return list;
	}

	public class InteractiveArrayAdapter extends ArrayAdapter<Language> {

		private final List<Language> list;
		private final Activity context;

		public InteractiveArrayAdapter(Activity context, List<Language> list) {
			super(context, R.layout.rowbuttonlayout, list);
			this.context = context;
			this.list = list;
		}

		class ViewHolder {
			protected TextView text;
			protected CheckBox checkbox;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				LayoutInflater inflator = context.getLayoutInflater();
				view = inflator.inflate(R.layout.rowbuttonlayout, null);
				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.text = (TextView) view.findViewById(R.id.label);
				viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);

				viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								Language element = (Language) viewHolder.checkbox.getTag();
								element.setIsChecked(buttonView.isChecked());
							}
						});

				view.setTag(viewHolder);
				viewHolder.checkbox.setTag(list.get(position));
			} else {
				view = convertView;
				((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
			}
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.text.setText(list.get(position).getLang());
			holder.checkbox.setChecked(list.get(position).getIsChecked());
			return view;
		}
	}
}