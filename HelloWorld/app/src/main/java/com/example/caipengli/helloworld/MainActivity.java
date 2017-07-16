package com.example.caipengli.helloworld;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mTalkbackTv;
    public static final String TALKBACK_Default = "com.android.talkback.TalkBackPreferencesActivity";
//                                   com.google.android.marvin.talkback.TalkBackPreferencesActivity
//                                     com.samsung.android.app.talkback.TalkBackPreferencesActivity
//    上面这个系统相关　但是后缀都是用　TalkBackPreferencesActivity
    public static final String TALKBACK = "TalkBackPreferencesActivity";

    private static AccessibilityManager.AccessibilityStateChangeListener mListener;
    private static TouchExplorationChangeListenerImple mTouchListener;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i("cpl", "on handle ");
            if (isOpenTalkback()) {
                Toast.makeText(MainActivity.this, "talkback open", Toast.LENGTH_LONG).show();
            }
            Log.i("cpl", "on handle finish ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTalkbackTv = (TextView) this.findViewById(R.id.tv_talkback);
        AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        boolean isAccessibilityEnabled = am.isEnabled();
        boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
        List<AccessibilityServiceInfo> spokenList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN);//说
        List<AccessibilityServiceInfo> audibleList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_AUDIBLE);//听觉
        List<AccessibilityServiceInfo> genricList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);//通用
        List<AccessibilityServiceInfo> visualList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_VISUAL);//视觉
        List<AccessibilityServiceInfo> hapticList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_HAPTIC);//触觉
        List<AccessibilityServiceInfo> brailleList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_BRAILLE);//盲文
        List<AccessibilityServiceInfo> allmaskList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);//all

        List<AccessibilityServiceInfo> allinstale = am.getInstalledAccessibilityServiceList();

        for (AccessibilityServiceInfo as : spokenList) {
            if (isAccessibilityEnabled && isExploreByTouchEnabled && as.getSettingsActivityName().contains(TALKBACK)) {
                Toast.makeText(this, "talkback is active", Toast.LENGTH_LONG).show();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\r\n").append("isAccessibilityEnabled : ").append(isAccessibilityEnabled).append("\r\n");
        sb.append("\r\n").append("isExploreByTouchEnabled : ").append(isExploreByTouchEnabled).append("\r\n");
        sb.append("\r\n").append("spoken list :").append("\r\n");
        for (AccessibilityServiceInfo as : spokenList) {
            Log.i("cpl", "" + as.getSettingsActivityName());
            sb.append(as.getSettingsActivityName())
                    .append("  ").append(as.getResolveInfo().resolvePackageName)
//                    .append("  ").append(as.getResolveInfo().activityInfo.toString())
                    .append("\r\n");
        }
        sb.append("\r\n").append("audibleList list :").append("\r\n");
        for (AccessibilityServiceInfo as : audibleList) {
            sb.append(as.getSettingsActivityName())
                    .append("  ").append(as.getResolveInfo().resolvePackageName)
//                    .append("  ").append(as.getResolveInfo().activityInfo.toString())
                    .append("\r\n");
        }
        sb.append("\r\n").append("genricList list :").append("\r\n");
        for (AccessibilityServiceInfo as : genricList) {
            sb.append(as.getSettingsActivityName())
                    .append("  ").append(as.getResolveInfo().resolvePackageName)
//                    .append("  ").append(as.getResolveInfo().activityInfo.toString())
                    .append("\r\n");
        }
        sb.append("\r\n").append("visualList list :").append("\r\n");
        for (AccessibilityServiceInfo as : visualList) {
            sb.append(as.getSettingsActivityName())
                    .append("  ").append(as.getResolveInfo().resolvePackageName)
//                    .append("  ").append(as.getResolveInfo().activityInfo.toString())
                    .append("\r\n");
        }
        sb.append("\r\n").append("hapticList list :").append("\r\n");
        for (AccessibilityServiceInfo as : hapticList) {
            sb.append(as.getSettingsActivityName())
                    .append("  ").append(as.getResolveInfo().resolvePackageName)
//                    .append("  ").append(as.getResolveInfo().activityInfo.toString())
                    .append("\r\n");
        }
        sb.append("\r\n").append("brailleList list :").append("\r\n");
        for (AccessibilityServiceInfo as : brailleList) {
            sb.append(as.getSettingsActivityName())
                    .append("  ").append(as.getResolveInfo().resolvePackageName)
//                    .append("  ").append(as.getResolveInfo().activityInfo.toString())
                    .append("\r\n");
        }
        sb.append("\r\n").append("allmaskList list :").append("\r\n");
        for (AccessibilityServiceInfo as : allmaskList) {
            sb.append(as.getSettingsActivityName())
                    .append("  ").append(as.getResolveInfo().resolvePackageName)
//                    .append("  ").append(as.getResolveInfo().activityInfo.toString())
                    .append("\r\n");
        }
        sb.append("\r\n").append("allinstale list :").append("\r\n");
        for (AccessibilityServiceInfo as : allinstale) {
            Log.i("cpl", " " + as.getSettingsActivityName());
            sb.append(as.getSettingsActivityName())
                    .append("  ").append(as.getResolveInfo().resolvePackageName)
//                    .append("  ").append(as.getResolveInfo().activityInfo.toString())
                    .append("\r\n");
        }

        mTalkbackTv.setText(sb.toString());

        observeTheTalkBack();
        observerTouch();
        compateListener();
    }

    private void observerTouch() {
        if (mTouchListener == null) {
            mTouchListener = new TouchExplorationChangeListenerImple();
            AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
            am.addTouchExplorationStateChangeListener(mTouchListener);
        }
    }

    private void observeTheTalkBack() {
        AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (mListener == null) {
            mListener = new AccessibilityStateChangeListenerImple();
            am.addAccessibilityStateChangeListener(mListener);
        }
    }

    public boolean isOpenTalkback() {
        boolean isOpen = false;
        AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        boolean isAccessibilityEnabled = am.isEnabled();
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
//            Log.i("cpl", "isAccessibilityEnabled : " + isAccessibilityEnabled + "   isExploreByTouchEnabled : " + isExploreByTouchEnabled);
//            if (!(isAccessibilityEnabled && isExploreByTouchEnabled)) {
//                Log.i("cpl", "one of the two boolean false");
//                return false;
//            }
            List<AccessibilityServiceInfo> spokenList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN);
        Log.i("cpl", "isAccessibilityEnabled : " + isAccessibilityEnabled + "   isExploreByTouchEnabled : " + isExploreByTouchEnabled);
        Log.i("cpl", "spokenList size " + spokenList.size());
        Log.i("cpl", "FEEDBACK_AUDIBLE size " + am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_AUDIBLE).size());
        Log.i("cpl", "FEEDBACK_HAPTIC size " + am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_HAPTIC).size());

        for (AccessibilityServiceInfo as : spokenList) {
                Log.i("cpl", " " + as.getSettingsActivityName());
                if (as.getSettingsActivityName().contains(TALKBACK)) {
                    return true;
                }
            }
//        } else { //low than 4.0
//
//        }
        return false;
    }

    private void compateListener() {
        AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);

        AccessibilityManagerCompat.addAccessibilityStateChangeListener(am, new AccesssService());
    }

    private class AccessibilityStateChangeListenerImple implements AccessibilityManager.AccessibilityStateChangeListener {
        @Override
        public void onAccessibilityStateChanged(boolean enabled) {
            Log.i("cpl", "the AccessibilityManager change " + enabled);

            mHandler.sendEmptyMessageDelayed(1, 300);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private class TouchExplorationChangeListenerImple implements AccessibilityManager.TouchExplorationStateChangeListener {
        @Override
        public void onTouchExplorationStateChanged(boolean enabled) {
            Log.i("cpl", "the TouchExplorationChangeListenerImple change " + enabled);
            mHandler.sendEmptyMessageDelayed(1, 300);
        }
    }
}
