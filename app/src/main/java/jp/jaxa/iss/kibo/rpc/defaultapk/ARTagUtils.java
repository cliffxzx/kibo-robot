package jp.jaxa.iss.kibo.rpc.defaultapk;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcApi;

import android.util.Log;
import android.graphics.Bitmap;

import org.opencv.aruco.Aruco;
import org.opencv.core.Mat;
import org.opencv.aruco.Dictionary;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class ARTagUtils implements Runnable {

    private Dictionary dictionary;
    private KiboRpcApi api;

    public ARTagUtils(KiboRpcApi api) {
        this.api = api;
        dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_5X5_250);
    }

    @Override
    public void run() {
        while (true) {
            judgeARTag(api);
        }
    }

    public void judgeARTag(KiboRpcApi api) {
        try {
            System.gc();
            String res = getARTagStr(api.getMatNavCam());

            Log.d("Seal", res);
            api.judgeSendDiscoveredAR(res);

        } catch (Exception e){
            Log.e("Seal", "Unhandled Exception", e);
        }
    }

    public String getARTagStr(Mat m) throws Exception {
        List<Mat> corners = new ArrayList<>();
        Mat ids = new Mat();
        Aruco.detectMarkers(m, dictionary, corners, ids);
        return Integer.toString((int)ids.get(0, 0)[0]);
    }
}

