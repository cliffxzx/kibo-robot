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

public class ARTagUtils {

    private static Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_5X5_250);

    public static void judgeARTag(KiboRpcApi api) {
        String res = null;
        while(res == null) {
            try {
                res = getARTagStr(api.getMatNavCam());

                Log.d("Seal", res);
                api.judgeSendDiscoveredAR(res);

            } catch (Exception e){
                Log.e("Seal", "Unhandled Exception", e);
            }
        }
    }

    public static String getARTagStr(Mat m) throws Exception {
        List<Mat> corners = new ArrayList<>();
        Mat ids = new Mat();
        Aruco.detectMarkers(m, dictionary, corners, ids);
        return Integer.toString((int)ids.get(0, 0)[0]);
    }
}

