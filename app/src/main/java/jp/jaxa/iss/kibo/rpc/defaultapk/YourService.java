package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;

import org.opencv.objdetect.QRCodeDetector;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.android.gs.MessageType;
import gov.nasa.arc.astrobee.types.*;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;
import android.util.Log;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        api.judgeSendStart();
//         moveToWrapper(10.6, -4.3, 5, 0, 0, -0.7071068, 0.7071068);
//         moveToWrapper(11, -4.3, 5, 0, 0, -0.7071068, 0.7071068);
//         moveToWrapper(11, -5.7, 5, 0, 0, -0.7071068, 0.7071068);
        QRCodeDetector detector = new QRCodeDetector();
        double tag_point[][] = {
            { 11.5, -5.7, 4.5, 0, 0, 0, 1 },
            { 11, -6, 5.2, 0, -0.7071068, 0, 0.7071068 },
            { 11, -5.5, 4.7, 0, 0.7071068, 0, 0.7071068 },
            { 10.5, -7.5, 4.7, 0, 0, 1, 0 },
            { 11.5, -8, 5.2, 0, 0, 0, 1 },
            { 11, -7.7, 5.2, 0, -0.7071068, 0, 0.7071068 }
        };

        double[] p3 = new double[7];
        int i = 0;
        for(double[] w : tag_point) {
            moveToWrapper(w[0], w[1], w[2], w[3], w[4], w[5], w[6]);
            // String data = detector.detectAndDecode(api.getMatNavCam());
            // p3[i] = Float.parseFloat(data);
            // Log.d("QR Code", data);
            // api.judgeSendDiscoveredQR(i++, data);
        }

        moveToWrapper(p3[0], p3[1], p3[2], p3[3], p3[4], p3[5], p3[6]);
    }

    @Override
    protected void runPlan2(){
        api.judgeSendStart();
//         moveToWrapper(10.6, -4.3, 5, 0, 0, -0.7071068, 0.7071068);
//         moveToWrapper(11.8, -4.3, 5, 0, 0, -0.7071068, 0.7071068);
//         moveToWrapper(11.8, -6.11, 5, 0, 0, -0.7071068, 0.7071068);
//         moveToWrapper(9.8, -6.11, 5, 0, 0, -0.7071068, 0.7071068);
//         moveToWrapper(9.8, -6.77, 5, 0, 0, -0.7071068, 0.7071068);
//         moveToWrapper(11.18, -6.77, 5, 0, 0, -0.7071068, 0.7071068);
//         moveToWrapper(9.8, -9.3, 5, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11.2331 ,-5.71366 ,4.50006 , 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11.2678 ,-5.71366 ,4.50006, 0, 0, -0.7071068, 0.7071068); //Qrcode 1
            moveToWrapper(11 ,-5.50513 ,4.62898, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11 ,-5.50513 ,4.5, 0, 0, -0.7071068, 0.7071068); //Qrcode 2
            moveToWrapper(11 ,-6 ,4.5, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11 ,-6 ,5.37647, 0, 0, -0.7071068, 0.7071068); //Qrcode 3
            
        //繞牆
            moveToWrapper(10.4643 ,-6.06433,4.7, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(10.6331 ,-6.87869, 4.7 , 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11.2454 ,-6.87869, 4.7 , 0, 0, -0.7071068, 0.7071068);


            moveToWrapper(11.2454 ,-7.5, 4.7 , 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(10.6058,-7.5, 4.7, 0, 0, -0.7071068, 0.7071068 ); //Qrcode 4
            moveToWrapper(11,-7.8,5, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11.1,-7.8,5, 0, 0, -0.7071068, 0.7071068); //Qrcode 5
            moveToWrapper(11,-7.7,5, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11,-7.7,5.3, 0, 0, -0.7071068, 0.7071068); //Qrcode 6


            moveToWrapper(11.1284,-7.66963,4.6, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11.1284,-7.66963,-9.38822, 0, 0, -0.7071068, 0.7071068);
    }

    @Override
    protected void runPlan3(){
        // write here your plan 3
    }

    private void moveToWrapper(double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w){

        final int LOOP_MAX = 3;
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                                                     (float)qua_z, (float)qua_w);

        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        while(!result.hasSucceeded() || loopCounter < LOOP_MAX){
            result = api.moveTo(point, quaternion, true);
            ++loopCounter;
        }
    }
}

