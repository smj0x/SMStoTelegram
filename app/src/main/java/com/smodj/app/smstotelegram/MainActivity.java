package com.smodj.app.smstotelegram;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.intentfilter.androidpermissions.PermissionManager;
import com.smodj.app.smstotelegram.Constants.MainConstant;
import com.smodj.app.smstotelegram.Workers.*;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singleton;


public class MainActivity extends AppCompatActivity {
    EditText telegram_id_field;
    Storage store = new Storage(this);
    String telegram_id_storage_value;
    Button save,givePermissions;
    TextView step1data, step3data, permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Init
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        telegram_id_field = findViewById(R.id.editText);
        telegram_id_storage_value = store.read(MainConstant.telegram_id_storage_key);
        save = findViewById(R.id.save);
        givePermissions = findViewById(R.id.givePermissions);
        step1data = findViewById(R.id.step1data);
        step1data.setMovementMethod(LinkMovementMethod.getInstance());
        step3data = findViewById(R.id.step3data);
        step3data.setMovementMethod(LinkMovementMethod.getInstance());
        permissions = findViewById(R.id.permissions);


        //Setting Id to field if it already exist's
        if(telegram_id_storage_value!=null){
            telegram_id_field.setText(telegram_id_storage_value);
            save.setText("Change");
        }
        //Checking if permissions exist or NOT
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissions.setText("Please provide required permissions!");
            permissions.setTextColor(getResources().getColor(R.color.colorRed));
        }
        else{
            givePermissions.setEnabled(false);
            permissions.setText("Required permissions are present");
            permissions.setTextColor(getResources().getColor(R.color.colorGreen));
        }




    }
    public void save(View view){
        String telegram_id = telegram_id_field.getText().toString();
        if(!telegram_id.isEmpty()){
            if(telegram_id_storage_value!=null) {
                if (!telegram_id_storage_value.equals(telegram_id)) {
                    store.write(MainConstant.telegram_id_storage_key, telegram_id);
                    Toaster.print(this, "ID Saved");
                } else {
                    Toaster.print(this, "ID Already Exists");
                }
            }
           else{
                store.write(MainConstant.telegram_id_storage_key, telegram_id);
                Toaster.print(this, "ID Saved");
            }
        }
        else{
            Toaster.print(this,"Field is Empty");
        }
    }

    public void givePermissions(View view) {

        final PermissionManager permissionManager = PermissionManager.getInstance(this);
        permissionManager.checkPermissions( singleton(Manifest.permission.READ_SMS), new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                permissionManager.checkPermissions( singleton(Manifest.permission.RECEIVE_SMS), new PermissionManager.PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toaster.print(MainActivity.this,"Permissions Granted");
                        givePermissions.setEnabled(false);
                        permissions.setText("Required permissions are present");
                        permissions.setTextColor(getResources().getColor(R.color.colorGreen));
                    }

                    @Override
                    public void onPermissionDenied() {
                        Toaster.print(MainActivity.this,"Permissions Denied");
                        permissions.setText("Please provide required permissions!");
                        permissions.setTextColor(getResources().getColor(R.color.colorRed));

                    }
                });
            }

            @Override
            public void onPermissionDenied() {

            }
        });
    }
}
