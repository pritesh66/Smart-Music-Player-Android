package smart.musicplayer.mainplayer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;


public class MpService extends Service{
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	File path = new File("/mnt");
	@Override
	 public void onCreate() {
        super.onCreate();
	}




    public int onStartCommand(Intent intent, int flags, int startId) {
        getPlayList(path);
        return 1;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {
    	
    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {
     
    }

    @Override
    public void onLowMemory() {

    }
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

    public ArrayList<HashMap<String, String>> getPlayList(File f){
		//File home = new File(MEDIA_PATH);
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			Log.i("path"," " + f);
			File[] ftemp = f.listFiles();
		if (f.listFiles(new FileExtensionFilter()).length > 0) {
			
			for (int i=0;i<ftemp.length;i++) {
				File file = ftemp[i];
				if(file.isFile())
				{
					if(file.getName().endsWith(".mp3") || file.getName().endsWith(".MP3"))
					{
						HashMap<String, String> song = new HashMap<String, String>();
						song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
						song.put("songPath", file.getPath());
						songsList.add(song);
				
					}
				}
				else if(file.isDirectory() && file.canRead())
				{
						getPlayList(file);
				}
				// Adding each song to SongList
				
			}
		 }  
		}
		// return songs list array
		return songsList;
	}
	
	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}	
	
	public ArrayList<HashMap<String, String>> getMe(){
		return songsList;
	}
}
