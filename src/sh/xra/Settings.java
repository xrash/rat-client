package sh.xra;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.CheckBox;
import android.widget.AdapterView.OnItemClickListener;

import sh.xra.networking.ScanTask;
import sh.xra.networking.UDP;

public class Settings extends Activity
{
    private EditText host;
    private EditText port;
    private ScanTask scanTask;
    private ListView serversList;
    private ProgressBar progressBar;
    private SeekBar multiplier;
    private CheckBox naturalScrolling;
    private CheckBox brightnessReduction;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        this.host = (EditText) findViewById(R.id.host);
        this.port = (EditText) findViewById(R.id.port);
        this.progressBar = (ProgressBar) findViewById(R.id.progress);
        this.serversList = (ListView) findViewById(R.id.servers);
        this.multiplier = (SeekBar) findViewById(R.id.multiplier);
        this.naturalScrolling = (CheckBox) findViewById(R.id.natural_scrolling);
        this.brightnessReduction = (CheckBox) findViewById(R.id.brightness_reduction);
    }

    public void scanForServers(View view)
    {
        view.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.VISIBLE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item);
        this.serversList.setAdapter(adapter);
        this.serversList.setClickable(true);
        this.serversList.setOnItemClickListener(new ServerClickListener(this.host,this.port));

        this.scanTask = new ScanTask(adapter, this.progressBar, (Button) view);
        this.scanTask.execute();
    }

    public void cancelScan(View view)
    {
        this.scanTask.cancel(true);
    }

    public void onStart()
    {
        super.onStart();

        this.host.setText(Options.getHost());
        this.port.setText(String.valueOf(Options.getPort()));
        this.multiplier.setProgress(Options.getMultiplier() - 1);
        this.naturalScrolling.setChecked(Options.getNaturalScrolling());
        this.brightnessReduction.setChecked(Options.getBrightnessReduction());
    }

    public void onPause()
    {
        super.onPause();

        Options.setHost(this.host.getText().toString());
        Options.setPort(Integer.valueOf(this.port.getText().toString()));
        Options.setMultiplier(this.multiplier.getProgress() + 1);
        Options.setNaturalScrolling(this.naturalScrolling.isChecked());
        Options.setBrightnessReduction(this.brightnessReduction.isChecked());

        UDP.configure(Options.getHost(),
                      Options.getPort());
    }

    private class ServerClickListener implements OnItemClickListener
    {
        private EditText host;
        private EditText port;

        public ServerClickListener(EditText host, EditText port)
        {
            this.host = host;
            this.port = port;
        }

        public void onItemClick(AdapterView parent,
                                View view,
                                int position,
                                long id)
        {
            this.host.setText(((TextView) view).getText().toString());
            this.port.setText("4500");
        }
    }
}
