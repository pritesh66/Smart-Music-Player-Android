package smart.musicplayer.mainplayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import smart.musicplayer.mp.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainPlayer extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener,AudioEffect.OnControlStatusChangeListener,AudioEffect.OnEnableStatusChangeListener,TextToSpeech.OnInitListener {
	private boolean stopthread = true;
	private String mylist;
	public static Equalizer eq;
	public static int prev_preset;
	public static ImageButton btnPlay;
	public static ImageButton btnNext;
	public static ImageButton btnPrevious;
	public static ImageButton btnPlaylist;
	public static ImageButton btnRepeat;
	public static ImageButton btnShuffle;
	public static ImageButton btnVoice;
	private SeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private TextToSpeech tts;
	private static final int REQUEST_CODE = 1234; //need to be set to 1234 
	// Media Player Instance mp
	private static MediaPlayer mp;
	//MpService m = new MpService();

	//path to scan directories for songs
	File f = new File("/mnt");
	
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();;
	private int flag5=0;
	private Utilities utils;	//used for showing Time in sec...
	private int currentSongIndex = 0; 
	private boolean isShuffle = false;	
	private boolean isRepeat = false;
	static public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	protected int flag1=0;
	protected int flag=0;
	private String commandName= null;
	private String songName = null;
	private int flagg=0;
	private TelephonyManager tm;
	private PhoneStateListener psl;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setRequestedOrientation(1);
		setContentView(R.layout.player);
		tts = new TextToSpeech(this,this);
		//Intent iii = new Intent(this, MpService.class);
		//startService(iii);
		//startService(new Intent(this, MpService.class));
		//Log.i("hh","hh");
	

		// All player buttons
		btnVoice = (ImageButton) findViewById(R.id.btnVoice);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
		btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
		btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		
		// Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            btnVoice.setEnabled(false);
        }

        
       //Listening To Headset Button
        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        new HandsetButtonClick();
        filter.setPriority(10000);
       ((AudioManager)getSystemService(AUDIO_SERVICE)).registerMediaButtonEventReceiver(new ComponentName(this,HandsetButtonClick.class));
       
     
		// Mediaplayer
		mp = new MediaPlayer();
		new SongsManager();
		utils = new Utilities();

		Intent i = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
		i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mp.getAudioSessionId());
		i.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
		sendBroadcast(i);

/*		 Intent intent = new Intent(this, MainPlayer.class);
		    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		    // Build notification
		    // Actions are just fake
		    Notification noti= new NotificationCompat.Builder(this)
		        .setContentTitle("New mail from " + "test@gmail.com")
		        .setContentText("Subject").setSmallIcon(R.drawable.icon)
		        .setContentIntent(pIntent)
		        .addAction(R.drawable.icon, "Call", pIntent)
		        .addAction(R.drawable.icon, "More", pIntent)
		        .addAction(R.drawable.icon, "And more", pIntent).build();
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    // Hide the notification after its selected
		    noti.flags |= Notification.FLAG_AUTO_CANCEL;

		    notificationManager.notify(0, noti);

*/		
		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important
		if(savedInstanceState != null)
		{	songsList = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable(mylist);
			Log.i("yoyo","yuppy->->->" + songsList.size());
		}
		else
		songsList = Splash.startsongsList;	
		//songsList = songManager.getPlayList(f);
		/*
		 * On Phone State Listener
		 */
		
		//private TelephonyManager tm;
		//private PhoneStateListener psl;
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		psl  = new PhoneStateListener()
		{
			@Override
			public void onCallStateChanged(int state,String s)
			{
				switch(state)
				{
				case TelephonyManager.CALL_STATE_IDLE:
					if(flag5==2)
					{
						btnPlay.performClick();
						flag5=0;
					}
						break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					if(mp.isPlaying())
					{
						btnPlay.performClick();
						flag5=2;}
				break;
				case TelephonyManager.CALL_STATE_RINGING:
					if(mp.isPlaying())
					{	btnPlay.performClick();
					flag5=2;}
					break;
				}
			}
			
		};
		
		tm.listen(psl, PhoneStateListener.LISTEN_CALL_STATE);
		
		
		 /* Play button click event
		 plays a song and pauses a song*/		
		btnPlay.setOnClickListener(new View.OnClickListener() {
			//@Override
			public void onClick(View arg0) {
				// check for already playing
				if(mp.isPlaying()){
					if(mp!=null){
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				}else{
					// Resume song
					if(mp!=null){
						mp.start();
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}
			
			}
		});
		
		/* Voice Action button click event
		Starts Voice Recognition Intent*/
		btnVoice.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				if(mp.isPlaying())
					btnPlay.performClick();
				
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
		                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Command");
		        flag=1;
		        startActivityForResult( intent, REQUEST_CODE);
		        
			}
		});
		 /* Next button click event
		 Plays next song by taking currentSongIndex + 1 */
		btnNext.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View arg0) {
				// check if next song is there or not
				if(isShuffle)
				{
					Random rand = new Random();
					currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
					playSong(currentSongIndex);	
				}
				else
				{
					if(currentSongIndex < (songsList.size() - 1)){
					playSong(currentSongIndex + 1);
					currentSongIndex = currentSongIndex + 1;
						}
					else{
						// play first song
						playSong(0);
						currentSongIndex = 0;
					}
				}
				
			}
		});
		/*Back button click event
		 Plays previous song by currentSongIndex - 1  */
		btnPrevious.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View arg0) {
				if(currentSongIndex > 0){
					playSong(currentSongIndex - 1);
					currentSongIndex = currentSongIndex - 1;
				}else{
					// play last song
					playSong(songsList.size() - 1);
					currentSongIndex = songsList.size() - 1;
				}
				
			}
		});
		/*Button Click event for Repeat button
		 Enables repeat flag to true*/
		btnRepeat.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View arg0) {
				if(isRepeat){
					isRepeat = false;
					Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}else{
					// make repeat to true
					isRepeat = true;
					Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isShuffle = false;
					btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}	
			}
		});
		/* Button Click event for Shuffle button
		 Enables shuffle flag to true*/
		btnShuffle.setOnClickListener(new View.OnClickListener() {
			 
				public void onClick(View arg0) {
				if(isShuffle){
					isShuffle = false;
					Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}else{
					// make repeat to true
					isShuffle= true;
					Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isRepeat = false;
					btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}	
			}
		});
		/*Button Click event for Play list click event
		 Launches list activity which displays list of songs*/
		btnPlaylist.setOnClickListener(new View.OnClickListener() {
				
			public void onClick(View arg0) {
				flag1 = 1;
				Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
				startActivityForResult(i, 100);
				
			}
		});
		
	}
	
	

	/**
	 * Receiving song index from playlist view
	 * and play the song
	 * */
	
    protected void onActivityResult(int requestCode,
                                     int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100 && flag1==1){
         	 currentSongIndex = data.getExtras().getInt("songIndex");
         	 // play selected song
             playSong(currentSongIndex);
             flag1=0;
        }
        if (flag==1 && requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
        	flag=0;
        	ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        	int n=songsList.size()-1;
        	String temp = matches.get(0);
        	 	
        	Toast.makeText(getApplicationContext(), "You Said: " + temp, Toast.LENGTH_SHORT).show();
          	tts.speak("You Said " + temp, TextToSpeech.QUEUE_FLUSH, null);
          	
          	if(temp.equalsIgnoreCase("shuffle on"))
            {
            	if(!(isShuffle))
            		btnShuffle.performClick();
            }
            else if(temp.equalsIgnoreCase("shuffle off"))
            {
            	if(isShuffle)
            			btnShuffle.performClick();
            }
            else if(temp.equalsIgnoreCase("repeat on"))
            {
            	if(!(isRepeat))
            		btnRepeat.performClick();
            }
            else if(temp.equalsIgnoreCase("repeat off"))
            {
            	if(isRepeat)
            			btnRepeat.performClick();
            }
          	
          	String[] temp1 = temp.split(" ",2);
        	commandName = temp1[0];
        	if(temp.length() > 5 && commandName.equalsIgnoreCase("play"))
        	{songName = temp1[1];
        	}
        
            
            
        	       	
        if(commandName.equals("play"))
        {	
        	if(songName!=null)
        	{
        		if(songName.matches(".*\\d.*")){
        			for(int i=0;i<n;i++)
                    {
        				String songNameinList = songsList.get(i).get("songTitle");
                     	String[] tag_name = songNameinList.split(" ",2);
                     	Log.i("tag" , tag_name[0]);
                     	if(songName.equalsIgnoreCase(tag_name[0]))
                     	{
                     		playSong(i);
                     		currentSongIndex=i;
                     		Toast.makeText(getApplicationContext(), "Playing: " + tag_name[1], Toast.LENGTH_SHORT).show();
                     		tts.speak("Playing " + tag_name[1], TextToSpeech.QUEUE_ADD, null);
                     		flagg=1;
                       		break;                        
                     	}
                    }
        		}
        		else{
        			for(int i=0;i<n;i++)
                    {
                     	String songNameinList = songsList.get(i).get("songTitle");
                     	String[] tag_name = songNameinList.split(" ",2);
                     	Log.i("tag" , tag_name[1]);
                     	if(songName.equalsIgnoreCase(tag_name[1]))
                     	{
                     		playSong(i);
                     		currentSongIndex=i;
                     		Toast.makeText(getApplicationContext(), "Playing: " + tag_name[1], Toast.LENGTH_SHORT).show();
                     		tts.speak("Playing " + tag_name[1], TextToSpeech.QUEUE_ADD, null);
                     		flagg=1;
                       		break;                        
                     	}
                    }
        		}
        		
        		if (flagg==0)
        		{
                    tts.speak("Song Not Found", TextToSpeech.QUEUE_ADD, null);	
        			Toast.makeText(getApplicationContext(), "Song Not Found", Toast.LENGTH_SHORT).show();
                }
        		flagg=0;
        		songName = null;
        	}        	
        		else
        		{
        			btnPlay.performClick();
        		}
        }
        	
            else if(commandName.equalsIgnoreCase("next"))
            {
            	btnNext.performClick();
            }
            else if(commandName.equalsIgnoreCase("previous"))
            {
            	btnPrevious.performClick();
            }
            
            
            
        }
        super.onActivityResult(requestCode, resultCode, data);
        
 
    }
	
	/**
	 * Function to play a song
	 * @param songIndex - index of song
	 * */
	public void  playSong(int songIndex){
		// Play song
		try {
        	mp.reset();
			mp.setDataSource(songsList.get(songIndex).get("songPath"));
			mp.prepare();
			mp.start();
			// Displaying Song title
			String songTitle = songsList.get(songIndex).get("songTitle");
        	songTitleLabel.setText(songTitle);
			
        	// Changing Button Image to pause image
			btnPlay.setImageResource(R.drawable.btn_pause);
			
			// set Progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);
			
			// Updating progress bar
			updateProgressBar();			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Update timer on seekbar
	 * */
	
	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
	}	
	
	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
				public void run() {
			   long totalDuration = mp.getDuration();
			   long currentDuration = mp.getCurrentPosition();
			  if(!stopthread) return;
			   // Displaying Total Duration time
			   songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			   // Displaying time completed playing
			   songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
			   
			   // Updating progress bar
			   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			   //Log.d("Progress", ""+progress);
			   songProgressBar.setProgress(progress);
			   
			   // Running this thread after 100 milliseconds
		       mHandler.postDelayed(this, 100);
		   }
				
				
		};
		
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
		
	}

	/**
	 * When user starts moving the progress handler
	 * */
	
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
    }
	
	/**
	 * When user stops moving the progress hanlder
	 * */
	
    public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
		
		// forward or backward to certain seconds
		mp.seekTo(currentPosition);
		
		// update timer progress again
		updateProgressBar();
    }

	/**
	 * On Song Playing completed
	 * if repeat is ON play same song again
	 * if shuffle is ON play random song
	 * */
	
	public void onCompletion(MediaPlayer arg0) {
		
		// check for repeat is ON or OFF
		if(isRepeat){
			// repeat is on play same song again
			playSong(currentSongIndex);
		} else if(isShuffle){
			// shuffle is on - play a random song
			Random rand = new Random();
			currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
			playSong(currentSongIndex);
		} else{
			// no repeat or shuffle ON - play next song
			if(currentSongIndex < (songsList.size() - 1)){
				playSong(currentSongIndex + 1);	
				currentSongIndex = currentSongIndex + 1;
			}else{
				// play first song
				playSong(0);
				currentSongIndex = 0;
			}
		}
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(mylist , songsList);
		super.onSaveInstanceState(outState);
	}
	
	 public void onDestroy(){
		 super.onDestroy();
		 mHandler.removeCallbacks(mUpdateTimeTask, null);
	     stopthread = false;
		 if (tts != null) {
	            tts.stop();
	            tts.shutdown();
	            tts = null;
	        }
		 mp.release();
 }
	 

	 @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	
	 @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	 
		 
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//songsList = new SongsManager().getPlayList(f);
		//Intent newI = new Intent(this,Splash.class);
		//startActivity(newI);
	}
	 @Override
     public void onBackPressed() {
		    Intent intent = new Intent(Intent.ACTION_MAIN);
		 	intent.addCategory(Intent.CATEGORY_HOME);
		 	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 	startActivity(intent);
		 
	 	}
	 
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {
	        getMenuInflater().inflate(R.layout.menubar, menu);
	        return true;
	    }
	    
	    /**
	     * Event Handling for Individual menu item selected
	     * Identify single menu item by it's id
	     * */
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	        
	    	switch (item.getItemId())
	        {
	        case R.id.menu_help:
	      		Intent help = new Intent(this, HelpScreen.class);
        		startActivity(help);
            return true;
	        case R.id.menu_about:
	        		Intent i = new Intent(this, AboutDevelopers.class);
	        		startActivity(i);
	        	return true;
	       
	        case R.id.menu_equalizer:
	        	Intent ii = new Intent(this, EqualizerActivity.class);
	        	startActivity(ii);
	            return true;
	       default:
	            return super.onOptionsItemSelected(item);
	        }
	    }

		public void onEnableStatusChange(AudioEffect arg0, boolean arg1) {
			// TODO Auto-generated method stub
		}

		public void onControlStatusChange(AudioEffect arg0, boolean arg1) {
			// TODO Auto-generated method stub
			
		}

		public static Equalizer myfunc()
		{
		     eq = new Equalizer(100,mp.getAudioSessionId());
		     return eq;
		}

		public void onInit(int status) {
			 if (status == TextToSpeech.SUCCESS) {
				 
		            int result = tts.setLanguage(Locale.US);
		 
		            if (result == TextToSpeech.LANG_MISSING_DATA
		                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
		                Log.e("TTS", "This Language is not supported");
		            } else {
		              
		            }
		 
		        } else {
		            Log.e("TTS", "Initilization Failed!");
		        }
		}
}