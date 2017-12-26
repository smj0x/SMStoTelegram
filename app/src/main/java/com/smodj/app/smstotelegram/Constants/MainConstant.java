package com.smodj.app.smstotelegram.Constants;

/**
 * Created by smj on 12/9/17.
 * To hold all the Constants Used in the Project
 */

public class MainConstant {
    public static final String pkg = "com.smodj.app.smstotelegram";
    private static final String bot_id = "138892312:AAEJMlbP84LiMYcuHCZKz9kCr_LwrCo81A0";
    public static final String telegram_url = "https://api.telegram.org/bot"+bot_id+"/sendMessage";
    public static final String telegram_id_storage_key= "TelegramID";
    public static final String unsent_msgs_stack = "DELAYED_STACK";
}
