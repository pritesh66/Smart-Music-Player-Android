//Sanket
package smart.musicplayer.mainplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;


public class HandsetButtonClick extends BroadcastReceiver {

public HandsetButtonClick() {
    super();
}

@Override
public void onReceive(Context context, Intent intent) {
    String intentAction = intent.getAction();
    Log.i("info",intentAction);
    
    if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
    	return;
    }
    
    KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
    if (event == null) {
    	return;
    }
    
    int action = event.getAction();
    if (action == KeyEvent.ACTION_DOWN && event.isLongPress()) {
        // do something
        	Log.i("pressred", "yes");
        	MainPlayer.btnVoice.performClick();
        }
       abortBroadcast();
}

}

