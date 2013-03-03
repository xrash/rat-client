package sh.xra.networking;

import java.util.ArrayList;
import java.net.InetAddress;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ArrayAdapter;

public class ScanTask extends AsyncTask<Void, InetAddress, Void>
{
    private ArrayAdapter<String> adapter;
    private ProgressBar progressBar;
    private Button button;
    private Scanner scanner = new Scanner();

    public ScanTask(ArrayAdapter<String> adapter,
                    ProgressBar progressBar,
                    Button button)
    {
        this.adapter = adapter;
        this.progressBar = progressBar;
        this.button = button;
    }

    public Void doInBackground(Void... params)
    {
        ArrayList<InetAddress> hosts = scanner.scan();
        this.progressBar.setProgress(0);
        this.progressBar.setMax(hosts.size());

        for (InetAddress host : hosts) {
            if (this.isCancelled()) {
                break;
            }

            if (UDP.knock(host)) {
                Log.d("Rat", "^^^^^^^^^^^^");
                this.publishProgress(host);
            }
            this.publishProgress();
        }

        return null;
    }

    public void onProgressUpdate(InetAddress... host)
    {
        if (host.length > 0) {
            this.adapter.add(host[0].getHostAddress());
        } else {
            this.progressBar.incrementProgressBy(1);
        }
    }

    public void onPostExecute(Void result)
    {
        this.finish();
    }

    public void onCancelled(Void result)
    {
        this.finish();
    }

    private void finish()
    {
        progressBar.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }
}
