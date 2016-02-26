package smart.musicplayer.mainplayer;

import android.media.audiofx.Equalizer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class CustomOnItemSelectedListener implements OnItemSelectedListener {
	private Equalizer eq;
	public static short preset1;
	public static short preset2;
	public static short preset3;
	public static short preset4;
	public static short preset5;
	public static short preset6;
	public static short preset7;
	public static short preset8;
	public static short preset9;
	public static short preset0;
	private String preset;
	
	public CustomOnItemSelectedListener()
	{
	     eq = MainPlayer.myfunc();
		 preset0= (short)((eq.getNumberOfPresets()) - 10);
	     preset1= (short)((eq.getNumberOfPresets()) - 1);
  		 preset2= (short)((eq.getNumberOfPresets()) - 2);
  		 preset3= (short)((eq.getNumberOfPresets()) - 3);
  		 preset4= (short)((eq.getNumberOfPresets()) - 4);
  		 preset5= (short)((eq.getNumberOfPresets()) - 5);
  		 preset6= (short)((eq.getNumberOfPresets()) - 6);
  		 preset7= (short)((eq.getNumberOfPresets()) - 7);
  		 preset8= (short)((eq.getNumberOfPresets()) - 8);
  		 preset9= (short)((eq.getNumberOfPresets()) - 9);

	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		 
	preset= parent.getItemAtPosition(pos).toString();
   		    		 
		
	if(preset.equals(eq.getPresetName(preset0)))
		 {
			try{
   		eq.usePreset(preset0);
           short preset = eq.getCurrentPreset();
           String name = eq.getPresetName(preset);
           Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
           }
   	catch (Exception e)
   	{
   		Log.i("getPresetEx",e.toString());
   		eq.release();
   	}
		 }
		else if(preset.equals(eq.getPresetName(preset1)))
   		 {
   			try{
        		eq.usePreset(preset1);
                short preset = eq.getCurrentPreset();
                String name = eq.getPresetName(preset);
                Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
                }
        	catch (Exception e)
        	{
        		Log.i("getPresetEx",e.toString());
        		eq.release();
        	}
   		 }
   		 else if(preset.equals(eq.getPresetName(preset2)))
   		 {
   			try{
        		eq.usePreset(preset2);
                short preset = eq.getCurrentPreset();
                String name = eq.getPresetName(preset);
                Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
                }
        	catch (Exception e)
        	{
        		Log.i("getPresetEx",e.toString());
        		eq.release();
        	} 
   		 }
   		else if(preset.equals(eq.getPresetName(preset3)))
  		 {
  			try{
       		eq.usePreset(preset3);
               short preset = eq.getCurrentPreset();
               String name = eq.getPresetName(preset);
               Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
               }
       	catch (Exception e)
       	{
       		Log.i("getPresetEx",e.toString());
       		eq.release();
       	} 
  		 }
   		else if(preset.equals(eq.getPresetName(preset4)))
  		 {
  			try{
       		eq.usePreset(preset4);
               short preset = eq.getCurrentPreset();
               String name = eq.getPresetName(preset);
               Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
               }
       	catch (Exception e)
       	{
       		Log.i("getPresetEx",e.toString());
       		eq.release();
       	} 
  		 }
   		else if(preset.equals(eq.getPresetName(preset5)))
  		 {
  			try{
       		eq.usePreset(preset5);
               short preset = eq.getCurrentPreset();
               String name = eq.getPresetName(preset);
               Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
               }
       	catch (Exception e)
       	{
       		Log.i("getPresetEx",e.toString());
       		eq.release();
       	} 
  		 }
   		else if(preset.equals(eq.getPresetName(preset6)))
  		 {
  			try{
       		eq.usePreset(preset6);
               short preset = eq.getCurrentPreset();
               String name = eq.getPresetName(preset);
               Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
               }
       	catch (Exception e)
       	{
       		Log.i("getPresetEx",e.toString());
       		eq.release();
       	} 
  		 }
   		else if(preset.equals(eq.getPresetName(preset7)))
  		 {
  			try{
       		eq.usePreset(preset7);
               short preset = eq.getCurrentPreset();
               String name = eq.getPresetName(preset);
               Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
               }
       	catch (Exception e)
       	{
       		Log.i("getPresetEx",e.toString());
       		eq.release();
       	} 
  		 }
   		else if(preset.equals(eq.getPresetName(preset8)))
  		 {
  			try{
       		eq.usePreset(preset8);
               short preset = eq.getCurrentPreset();
               String name = eq.getPresetName(preset);
               Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
               }
       	catch (Exception e)
       	{
       		Log.i("getPresetEx",e.toString());
       		eq.release();
       	} 
  		 }
   		else if(preset.equals(eq.getPresetName(preset9)))
  		 {
  			try{
       		eq.usePreset(preset9);
               short preset = eq.getCurrentPreset();
               String name = eq.getPresetName(preset);
               Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
               }
       	catch (Exception e)
       	{
       		Log.i("getPresetEx",e.toString());
       		eq.release();
       	} 
  		 }
	}
		

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}


}