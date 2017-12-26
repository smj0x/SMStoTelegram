package com.smodj.app.smstotelegram.Workers;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by smj on 12/13/17.
 * Display msg to user via Toaster
 */

public class Toaster {

    public static void print(Context con, String str)
    {
        Toast.makeText(con, str, Toast.LENGTH_LONG).show();
    }

}
