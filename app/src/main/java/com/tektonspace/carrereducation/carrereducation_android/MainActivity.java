package com.tektonspace.carrereducation.carrereducation_android;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.util.List;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends UnityPlayerActivity {
    private static final String INTENT_STATUS_UNITY = "STATUS_UNITY";
    private static final String INTENT_STATUS_ANDROID = "STATUS_ANDROID";
    private static final String TAG = "TEKTON_MAIN";

    private Handler mCommandHandler = null;
    private Handler getStatusHandler = null;

    int currStatus = -1;
    @Override
    public void onCreate (Bundle savedInstanceState) {
        try{
            currStatus = -1;
            Intent intentFromOculus = getIntent();
            currStatus = intentFromOculus.getIntExtra(INTENT_STATUS_ANDROID, -1);

            Log.w(TAG, "tekton_onCreate   currStatus :  "  + currStatus);
            getStatusHandler = new Handler();
            final String finalCurrStatus = String.valueOf(currStatus);

            getStatusHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.w(TAG, "tekton_onCreate   getData  " + finalCurrStatus);
                    UnityPlayer.UnitySendMessage("ReceiveMessage", "getData", finalCurrStatus);
                }
            }, 1000L);
        }
        catch (Exception ex){
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }
        super.onCreate (savedInstanceState);
    }

    private void StartAndroid()
    {
        Intent change = new Intent();
        change.setAction("com.oculus.oculus360videossdk.changeToAndroid");
        change.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        change.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        change.putExtra(INTENT_STATUS_UNITY, ++currStatus);
        Log.w(TAG, "tekton_unity_intent " + currStatus);
        startActivity(change);
        overridePendingTransition(R.anim.fade_in,R.anim.hold);
    }
    @Override
    protected void onResume(){
        super.onResume();

        try{
            Intent intentFromOculus = getIntent();
            currStatus = intentFromOculus.getIntExtra(INTENT_STATUS_ANDROID, -1);

            Log.w(TAG, "tekton_onResume   currStatus :  "  + currStatus);
            getStatusHandler = new Handler();
            final String finalCurrStatus = String.valueOf(currStatus);

            Log.w(TAG, "tekton_onResume   getData  " + finalCurrStatus);
            UnityPlayer.UnitySendMessage("ReceiveMessage", "Callfromnative", finalCurrStatus);
        }
        catch (Exception ex){
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
    }

}


