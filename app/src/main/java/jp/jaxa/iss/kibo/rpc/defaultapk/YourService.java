package jp.jaxa.iss.kibo.rpc.defaultapk;

import java.lang.Thread;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.android.gs.MessageType;
import gov.nasa.arc.astrobee.types.*;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.NotFoundException;
import com.google.zxing.*;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        api.judgeSendStart();

        //QR Code 1
        moveToWrapper(11.2331, -5.71366, 4.50006, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.2678, -5.71366, 4.50006, 0, 0, -0.7071068, 0.7071068);
        QRCodeUtils.judgeQRCode(api);

        //QR Code 2
        moveToWrapper(11, -5.50513, 4.62898, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11, -5.50513, 4.5, 0, 0, -0.7071068, 0.7071068);
        QRCodeUtils.judgeQRCode(api);

        //QR Code 3
        moveToWrapper(11, -6, 4.5, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11, -6, 5.37647, 0, 0, -0.7071068, 0.7071068);
        QRCodeUtils.judgeQRCode(api);

        //繞牆
        moveToWrapper(10.4643, -6.06433, 4.7, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(10.6331, -6.87869, 4.7, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.2454, -6.87869, 4.7, 0, 0, -0.7071068, 0.7071068);

        //QR Code 4
        moveToWrapper(11.2454, -7.5, 4.7, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(10.6058, -7.5, 4.7, 0, 0, -0.7071068, 0.7071068 );
        QRCodeUtils.judgeQRCode(api);

        //QR Code 5
        moveToWrapper(11, -7.8, 5, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.1, -7.8, 5, 0, 0, -0.7071068, 0.7071068);
        QRCodeUtils.judgeQRCode(api);

        //QR Code 6
        moveToWrapper(11, -7.7, 5, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11, -7.7, 5.3, 0, 0, -0.7071068, 0.7071068);
        QRCodeUtils.judgeQRCode(api);

        Log.d("Seal", QRCodeUtils.p3.toString());

        moveToWrapper(11.1284, -7.66963, 4.6, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.1284, -9.4, 4.6, 0, 0, -0.7071068, 0.7071068);
        api.judgeSendFinishSimulation();
    }

    @Override
    protected void runPlan2(){
    }

    @Override
    protected void runPlan3(){
        // write here your plan 3
    }

    private void moveToWrapper(double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w){
        final int RETRY_MAX = 20;
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                                                     (float)qua_z, (float)qua_w);

        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        String TAG = "Seal";
        while(!result.hasSucceeded() && loopCounter <= RETRY_MAX){
            ++loopCounter;

            try {
                Thread.sleep(200);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            Log.d(TAG, "MoveTo->Counter:" + loopCounter + "Result->Status:" + result.getStatus());
            result = api.moveTo(point, quaternion, true);
        }
    }
}

