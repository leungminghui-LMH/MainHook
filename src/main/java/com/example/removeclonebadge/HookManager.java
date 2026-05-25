package com.example.removeclonebadge;

import android.os.Build;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookManager {

    private static final String TAG = "RemoveCloneBadge";

    /**
     * Hook launcher app to remove clone/space badges
     */
    public static void hookLauncher(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // Hook ShortcutInfo class to remove badge count/notification
            hookShortcutInfoBadge(lpparam);
            
            // Hook AppInfo class
            hookAppInfoBadge(lpparam);
            
            // Hook icon rendering methods
            hookIconBadgeRendering(lpparam);
            
            XposedBridge.log(TAG + ": Launcher hooks applied successfully");
        } catch (Throwable e) {
            XposedBridge.log(TAG + ": Error hooking launcher: " + e.getMessage());
            XposedBridge.log(e);
        }
    }

    /**
     * Hook ShortcutInfo to remove badge
     */
    private static void hookShortcutInfoBadge(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> shortcutInfoClass = XposedHelpers.findClass(
                    "com.android.launcher3.model.data.ShortcutInfo", lpparam.classLoader);

            // Hook getNotificationCount method
            XposedHelpers.findAndHookMethod(shortcutInfoClass, "getNotificationCount",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            param.setResult(0);
                        }
                    });

            XposedBridge.log(TAG + ": Hooked ShortcutInfo.getNotificationCount");
        } catch (Throwable e) {
            XposedBridge.log(TAG + ": Could not hook ShortcutInfo: " + e.getMessage());
        }
    }

    /**
     * Hook AppInfo to remove badge
     */
    private static void hookAppInfoBadge(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> appInfoClass = XposedHelpers.findClass(
                    "com.android.launcher3.model.data.AppInfo", lpparam.classLoader);

            // Hook getNotificationCount method
            XposedHelpers.findAndHookMethod(appInfoClass, "getNotificationCount",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            param.setResult(0);
                        }
                    });

            XposedBridge.log(TAG + ": Hooked AppInfo.getNotificationCount");
        } catch (Throwable e) {
            XposedBridge.log(TAG + ": Could not hook AppInfo: " + e.getMessage());
        }
    }

    /**
     * Hook icon badge rendering
     */
    private static void hookIconBadgeRendering(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // Hook FastBitmapDrawable or BitmapDrawable for badge rendering
            Class<?> fastBitmapDrawableClass = null;
            try {
                fastBitmapDrawableClass = XposedHelpers.findClass(
                        "com.android.launcher3.graphics.FastBitmapDrawable", lpparam.classLoader);
            } catch (Throwable e) {
                fastBitmapDrawableClass = XposedHelpers.findClass(
                        "com.android.launcher3.FastBitmapDrawable", lpparam.classLoader);
            }

            // Hook draw method to skip badge drawing
            XposedHelpers.findAndHookMethod(fastBitmapDrawableClass, "drawBadge",
                    android.graphics.Canvas.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            param.setResult(null);
                        }
                    });

            XposedBridge.log(TAG + ": Hooked FastBitmapDrawable.drawBadge");
        } catch (Throwable e) {
            XposedBridge.log(TAG + ": Could not hook badge rendering: " + e.getMessage());
        }
    }

    /**
     * Hook SystemUI for badge rendering
     */
    public static void hookSystemUI(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // Hook notification badge related classes
            hookNotificationBadge(lpparam);
            
            // Hook status bar icon badge
            hookStatusBarIconBadge(lpparam);
            
            XposedBridge.log(TAG + ": SystemUI hooks applied successfully");
        } catch (Throwable e) {
            XposedBridge.log(TAG + ": Error hooking SystemUI: " + e.getMessage());
            XposedBridge.log(e);
        }
    }

    /**
     * Hook notification badge rendering
     */
    private static void hookNotificationBadge(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // Try to find BadgeDrawable class
            Class<?> badgeDrawableClass = null;
            try {
                badgeDrawableClass = XposedHelpers.findClass(
                        "com.android.systemui.statusbar.notification.stack.NotificationIconContainer", 
                        lpparam.classLoader);
            } catch (Throwable e1) {
                try {
                    badgeDrawableClass = XposedHelpers.findClass(
                            "com.oplus.systemui.statusbar.notification.BadgeDrawable", 
                            lpparam.classLoader);
                } catch (Throwable e2) {
                    // Try alternative paths
                }
            }

            if (badgeDrawableClass != null) {
                // Hook draw methods to prevent badge rendering
                XposedHelpers.findAndHookMethod(badgeDrawableClass, "draw",
                        android.graphics.Canvas.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                param.setResult(null);
                            }
                        });
                
                XposedBridge.log(TAG + ": Hooked notification badge rendering");
            }
        } catch (Throwable e) {
            XposedBridge.log(TAG + ": Could not hook notification badge: " + e.getMessage());
        }
    }

    /**
     * Hook status bar icon badge
     */
    private static void hookStatusBarIconBadge(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> statusBarIconViewClass = XposedHelpers.findClass(
                    "com.android.systemui.statusbar.phone.StatusBarIconView", lpparam.classLoader);

            // Hook setBadgeCount or setNotification method
            try {
                XposedHelpers.findAndHookMethod(statusBarIconViewClass, "setBadgeCount",
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                param.args[0] = 0;
                            }
                        });
            } catch (Throwable e) {
                // Method might not exist in all versions
            }

            XposedBridge.log(TAG + ": Hooked StatusBarIconView");
        } catch (Throwable e) {
            XposedBridge.log(TAG + ": Could not hook StatusBarIconView: " + e.getMessage());
        }
    }
}
