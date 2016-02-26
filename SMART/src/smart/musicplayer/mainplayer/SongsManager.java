package smart.musicplayer.mainplayer;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;
import android.util.Log;

public class SongsManager {
	int tag = 1;
	// SDCard Path
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	// Constructor
	public SongsManager(){
	}
  
	/**
	 * Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 * */
	public ArrayList<HashMap<String, String>> getPlayList(File f){
		//File home = new File(MEDIA_PATH);
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			File[] ftemp = f.listFiles();
			//Log.i("path"," "+f);
		if (f.listFiles(new FileExtensionFilter()).length > 0 || true) {
			
			for (int i=0;i<ftemp.length;i++) {
				File file = ftemp[i];
				
				if(file.isFile())
				{
					if(file.getName().endsWith(".mp3") || file.getName().endsWith(".MP3") || file.getName().endsWith(".WMA"))
					{
						HashMap<String, String> song = new HashMap<String, String>();
						song.put("songTitle", tag++ + " " + file.getName().substring(0, (file.getName().length() - 4)));
						song.put("songPath", file.getPath());
						songsList.add(song);
				
					}
				}
				else if(file.isDirectory() && file.canRead())
				{
					if(file.getName().equalsIgnoreCase("asec") || file.getName().equalsIgnoreCase("obb") || file.getName().equalsIgnoreCase("secure"))
						Log.i("androiddir", "Unwanted Dir");
					else
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
	}