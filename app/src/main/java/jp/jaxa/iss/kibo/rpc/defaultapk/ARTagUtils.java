package jp.jaxa.iss.kibo.rpc.defaultapk;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcApi;

import android.util.Log;
import android.graphics.Bitmap;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.core.CvType;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class ARTagUtils {

    private static Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_5X5_250);

    public static void judgeARTag(KiboRpcApi api) {
        String res = null;
        while(res == null) {
            try {
                List<Mat> corners = new ArrayList<>();
                Mat ids = new Mat();
                Aruco.detectMarkers(api.getMatNavCam(), dictionary, corners, ids);
                res = Integer.toString((int)ids.get(0, 0)[0]);
                Mat camaraMatrix = new Mat(3, 3, CvType.CV_64FC4);
                camaraMatrix.put(3, 3, 344.173397, 0, 630.793795, 0, 344.277922, 487.033834, 0, 0, 1);
                Mat distCoeffs = new Mat(1, 5, CvType.CV_64FC4);
                distCoeffs.put(1, 5, -0.152963, 0.017530, -0.001107, -0.000210, 0);

                Mat rvecs = new Mat(), tvecs = new Mat();
                Aruco.estimatePoseSingleMarkers(corners, 5, camaraMatrix, distCoeffs, rvecs, tvecs);

                Log.d("Seal", res);
                api.judgeSendDiscoveredAR(res);

            } catch (Exception e){
                Log.e("Seal", "Unhandled Exception", e);
            }
        }
    }

}

