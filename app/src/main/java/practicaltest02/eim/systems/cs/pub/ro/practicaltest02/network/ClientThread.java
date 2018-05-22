package practicaltest02.eim.systems.cs.pub.ro.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.general.Constants;
import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.general.Utilities;

/**
 * Created by student on 22.05.2018.
 */

public class ClientThread extends Thread {

    private int port;
    private String myquery;
    private TextView resultView;

    private Socket socket;

    public ClientThread(int port, String myquery, TextView resultVIew) {
        this.port = port;
        this.myquery = myquery;
        this.resultView = resultVIew;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost", port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(myquery);
            printWriter.flush();
            String info;
            while ((info = bufferedReader.readLine()) != null) {
                final String resultInfo = info;
                resultView.post(new Runnable() {
                    @Override
                    public void run() {
                        resultView.setText(resultInfo);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
