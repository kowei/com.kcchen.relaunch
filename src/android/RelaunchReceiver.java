package com.kcchen.relaunch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;

public class RelaunchReceiver extends BroadcastReceiver {
    private static final String TAG = RelaunchReceiver.class.getSimpleName();
    private boolean isLaunched = false;
    private boolean isUpdate = false;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(isLaunched){
            if(intent.getAction() == Intent.ACTION_MY_PACKAGE_REPLACED){
                isLaunched = false;
            }
            Log.e(TAG,"> RelaunchReceiver isLaunched " + isLaunched);
            return;
        }

        final String msg = "intent:" + intent + " action:" + intent.getAction();
        AssetManager asssetManager = context.getAssets();
        boolean isRelease = false;
        if(asssetManager != null){
            try {
                String[] list;
//                list = asssetManager.list("");
//                Log.e(TAG,"> root size "+list.length);
                list = asssetManager.list("www/assets");
                Log.e(TAG,"> assets size "+list.length);
                for(int i = 0;i<list.length;i++){
                    Log.e(TAG,"> "+list[i]);
                    if(list[i].equalsIgnoreCase("release")){
                        isRelease = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "onReceive: "
                + "\n intent:" + intent
                + "\n Categories:" + intent.getCategories()
                + "\n Action:" + intent.getAction()
                + "\n Data:" + intent.getDataString()
                + "\n mimeType:" + intent.getType()
                + "\n isRelease:" + isRelease
        );
        if(intent.getAction() == Intent.ACTION_MY_PACKAGE_REPLACED ||
                (intent.getAction() == Intent.ACTION_PACKAGE_ADDED && intent.getDataString().equalsIgnoreCase("package:com.kcchen.penpal"))
                ){
            isUpdate = true;
        }
        if(!isRelease && isUpdate){
            try {
                Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                Log.e(TAG,"> launch activity " + i);
                isLaunched = true;
                context.startActivity(i);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
