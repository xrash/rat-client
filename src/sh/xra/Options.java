package sh.xra;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Options
{
    private static SharedPreferences sharedPreferences;
    private static Editor editor;

    public static void initiate(SharedPreferences sharedPreferences)
    {
        Options.sharedPreferences = sharedPreferences;
        Options.editor = sharedPreferences.edit();
    }

    public static boolean getNaturalScrolling()
    {
        return Options.sharedPreferences.getBoolean("natural_scrolling", false);
    }

    public static void setNaturalScrolling(boolean naturalScrolling)
    {
        Options.editor.putBoolean("natural_scrolling", naturalScrolling);
        editor.commit();
    }

    public static boolean getBrightnessReduction()
    {
        return Options.sharedPreferences.getBoolean("brightness_reduction", false);
    }

    public static void setBrightnessReduction(boolean brightnessReduction)
    {
        Options.editor.putBoolean("brightness_reduction", brightnessReduction);
        editor.commit();
    }

    public static int getMultiplier()
    {
        return Options.sharedPreferences.getInt("multiplier", 1);
    }

    public static void setMultiplier(int multiplier)
    {
        Options.editor.putInt("multiplier", multiplier);
        editor.commit();
    }

    public static String getHost()
    {
        return Options.sharedPreferences.getString("host", "");
    }

    public static void setHost(String host)
    {
        Options.editor.putString("host", host);
        editor.commit();
    }

    public static int getPort()
    {
        return Options.sharedPreferences.getInt("port", 4500);
    }

    public static void setPort(int port)
    {
        Options.editor.putInt("port", port);
        editor.commit();
    }
}
