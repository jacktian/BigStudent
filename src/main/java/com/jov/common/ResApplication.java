package com.jov.common;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class ResApplication extends Application {
    private static List<Activity> activitys = new LinkedList<Activity>();
    private ResApplication(){}
    public static void addActivity(Activity activity) {
        activitys.add(activity);
    }
    public static void finish() {
        for (Activity activity : activitys) {
            activity.finish();
        }
        System.exit(0);
    }
}