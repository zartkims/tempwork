package com.example.caipengli.helloworld;

import android.accessibilityservice.AccessibilityService;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * Created by caipengli on 16/8/7.
 */
public class AccesssService extends AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat {
    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        Log.i("cpl", "AccessibilityStateChangeListenerCompat change : " + enabled);
        AccessibilityManager.TouchExplorationStateChangeListener tc;
//        AccessibilityManagerCompat.getEnabledAccessibilityServiceList()
    }

}
