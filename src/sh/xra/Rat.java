package sh.xra;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import sh.xra.controllers.Controller;
import sh.xra.controllers.TouchController;
import sh.xra.networking.UDP;

public class Rat extends Activity
{
    private Controller controller;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Options.initiate(this.getSharedPreferences("rat", 0));

        UDP.configure(Options.getHost(), Options.getPort());

        this.controller = new TouchController();
        startActivity(new Intent(this, TouchController.class));  
    }
}
