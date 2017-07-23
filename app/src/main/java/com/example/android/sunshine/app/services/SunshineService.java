package com.example.android.sunshine.app.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SunshineService extends IntentService {

    private static final String TAG="BADABOOOO";


    public SunshineService() {
        super("SunshineService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SunshineService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // TODO: Handle action Foo
        Log.v(TAG, TAG);

    }

}
