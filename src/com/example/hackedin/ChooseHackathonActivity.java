package com.example.hackedin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ChooseHackathonActivity extends Activity {

	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosehackathon);

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Hackathon");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(final List<ParseObject> hackathonList, ParseException e) {
				if (e == null) {
					final ArrayList<String> hackathonNames = new ArrayList<String>();
					final ListView listChooseHackathon = (ListView)findViewById(R.id.listChooseHackathon);
					for (ParseObject o : hackathonList)
						hackathonNames.add(o.getString("name"));
					ArrayAdapter<String> aa = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, hackathonNames);
					listChooseHackathon.setAdapter(aa);
					listChooseHackathon.setOnItemClickListener(new AdapterView.OnItemClickListener() {				
						public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
							Intent i = new Intent(context, HackathonInfoActivity.class);
							Bundle b = new Bundle();
							b.putString("hackathon_id", hackathonList.get(position).getObjectId());
							b.putString("user_id", getIntent().getExtras().getString("user_id"));
							i.putExtras(b);
							startActivity(i);
						}
					});
				}
				else
					alertMessage("Error", "Error retrieving hackathons", true);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.onCreate(null);
	}

	public void alertMessage(String title, String message, final boolean finish) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (finish)
					finish();
				else
					dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}
