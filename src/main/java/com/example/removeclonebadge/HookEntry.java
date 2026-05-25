package com.example.removeclonebadge;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookEntry implements IXposedHookLoadPackage {

    private static final String TAG = "RemoveCloneBadge";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Hook launcher and system UI packages
        if (lpparam.packageName.equals("com.oppo.launcher") ||
            lpparam.packageName.equals("com.android.launcher3") ||
            lpparam.packageName.equals("com.oplus.launcher")) {
            
            XposedBridge.log(TAG + ": Hooking " + lpparam.packageName);
            HookManager.hookLauncher(lpparam);
        }

        // Hook system UI for badge rendering
        if (lpparam.packageName.equals("com.android.systemui") ||
            lpparam.packageName.equals("com.oplus.systemui")) {
            
            XposedBridge.log(TAG + ": Hooking SystemUI " + lpparam.packageName);
            HookManager.hookSystemUI(lpparam);
        }
    }
}
