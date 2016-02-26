package smart.musicplayer.mainplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import smart.musicplayer.mp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Splash extends Activity {
	static public ArrayList<HashMap<String, String>> startsongsList = new ArrayList<HashMap<String, String>>();
	File f = new File("/mnt");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Hides the titlebar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.splash);

		Thread mythread = new Thread() {
			public void run() {
				try {
					startsongsList = new SongsManager().getPlayList(f);
					}
				catch(Exception e) {}
				finally {
					Intent intent = new Intent(Splash.this, MainPlayer.class);
					startActivity(intent);
				}
			}
		};
		mythread.start();
	}
	
}