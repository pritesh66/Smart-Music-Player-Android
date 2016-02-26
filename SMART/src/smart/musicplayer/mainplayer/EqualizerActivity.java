package smart.musicplayer.mainplayer;

import java.util.ArrayList;
import java.util.List;

import smart.musicplayer.mp.R;
import android.app.Activity;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class EqualizerActivity extends Activity {

	private Spinner spinner1;
	private int l;
	private Equalizer eq;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eq = MainPlayer.myfunc();
		eq.setEnabled(true);
		Log.i("Effect"," " + eq.getEnabled());
		Log.i("No. of bands","" + eq.getNumberOfBands());
		setContentView(R.layout.equalizer);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		addListenerOnButton();
		addListenerOnSpinnerItemSelection();
		spinner1.setSelection(MainPlayer.prev_preset);
	}

	public void addListenerOnSpinnerItemSelection(){
			spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}
	
	//get the selected dropdown list value
	public void addListenerOnButton() {
		List<String> list = new ArrayList<String>();
		int n = eq.getNumberOfPresets();
		for(int i=n;i>=1;i--)
		{
			list.add(eq.getPresetName((short)(n - i)));
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);

	}
	
	@Override
	public void onPause()
	{
		l=spinner1.getSelectedItemPosition();
		//Log.i("position of current preset","This is at" + l);
		super.onPause();
		MainPlayer.prev_preset=l;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		eq.release();
	}
	

}