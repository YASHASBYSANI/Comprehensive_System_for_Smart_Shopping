package com.sourcey.smartshopping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * Created by yashas on 24-05-2017.
 */

public class HomePageActivity extends AppCompatActivity {
    public static HomePageActivity instance;
    Button start;
    dbHandler myDB;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        instance = this;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        myDB = new dbHandler(this);
        start = (Button) findViewById(R.id.startshopping);
        Button check_in = (Button) findViewById(R.id.checkin);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    Intent scan = new Intent(HomePageActivity.this, ScanProductActivity.class);
                    finish();
                    startActivity(scan);
                }
                else
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(HomePageActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(HomePageActivity.this);
                    }
                    builder.setTitle("Enter Store!")
                            .setMessage("Please Enter Store First")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent enter = new Intent(HomePageActivity.this,EnterStoreActivity.class);
                                    finish();
                                    startActivity(enter);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatecheckin())
                {
                    Intent intent = new Intent(HomePageActivity.this, EnterStoreActivity.class);
                    finish();
                    startActivity(intent);
                }
                else
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(HomePageActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(HomePageActivity.this);
                    }
                    builder.setTitle("Continue!")
                            .setMessage("You Are Already In Store! Continue Shopping?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(HomePageActivity.this, ScanProductActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });
        Button cart1 = (Button) findViewById(R.id.paybills);
        cart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, ViewListContents.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout: {
                // do your sign-out stuff
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(HomePageActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(HomePageActivity.this);
                }
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LoginActivity.session.setLogin(false);
                                Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
            case R.id.profile: {
                finish();
                Intent prof = new Intent(HomePageActivity.this, Profile.class);
                startActivity(prof);
                break;
            }
            case R.id.wallet: {
                finish();
                Intent wallet = new Intent(HomePageActivity.this, WalletActivity.class);
                startActivity(wallet);
                break;
            }
            case R.id.exit: {
                String addamt = "0";
                String storeid = "0";
                String checkin = "0";
                SharedPreferences check = getSharedPreferences("checkin", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = check.edit();
                edit.putString("ckeckin", checkin);
                edit.commit();
                SharedPreferences store = getSharedPreferences("storeid", Context.MODE_PRIVATE);
                edit = store.edit();
                edit.putString("shopid", storeid);
                edit.commit();
                Log.d("shop",storeid);
                Log.d("check",checkin);
                myDB.deleteall();
                finish();
                System.exit(0);
            }
        }
        return false;
    }
    public boolean validatecheckin()
    {
        boolean valid = true;
        SharedPreferences store = getSharedPreferences("storeid", Context.MODE_PRIVATE);
        String storeid = store.getString("shopid","");
        if(!storeid.matches("0"))
        {
            valid = false;
        }
        else
        {
            valid = true;
        }
        return valid;
    }
    public static HomePageActivity getInstance() {
        return instance;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean validate()
    {
        boolean valid = true;
        SharedPreferences store = getSharedPreferences("storeid", Context.MODE_PRIVATE);
        String storeid = store.getString("shopid","");
        if(storeid.matches("0"))
        {
            valid = false;
        }
        else
        {
            valid = true;
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(HomePageActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(HomePageActivity.this);
        }
        builder.setTitle("Exit")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
