package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;

import org.opencv.objdetect.QRCodeDetector;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.android.gs.MessageType;
import gov.nasa.arc.astrobee.types.*;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;
import android.util.Log;
import java.lang.Thread;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.zxing.NotFoundException;
import com.google.zxing.*;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        api.judgeSendStart();
            double[] p3 = new double[7];
            moveToWrapper(11.2331 ,-5.71366 ,4.50006 , 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11.2678 ,-5.71366 ,4.50006, 0, 0, -0.7071068, 0.7071068); //Qrcode 1
            p3[0] = Double.parseDouble(getNavCamQRCodeStr(api.getBitmapNavCam()).split(" ")[1]);
            moveToWrapper(11 ,-5.50513 ,4.62898, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11 ,-5.50513 ,4.5, 0, 0, -0.7071068, 0.7071068); //Qrcode 2
            p3[1] = Double.parseDouble(getNavCamQRCodeStr(api.getBitmapNavCam()).split(" ")[1]);
            moveToWrapper(11 ,-6 ,4.5, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11 ,-6 ,5.37647, 0, 0, -0.7071068, 0.7071068); //Qrcode 3
            p3[2] = Double.parseDouble(getNavCamQRCodeStr(api.getBitmapNavCam()).split(" ")[1]);
        //繞牆
            moveToWrapper(10.4643 ,-6.06433,4.7, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(10.6331 ,-6.87869, 4.7 , 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11.2454 ,-6.87869, 4.7 , 0, 0, -0.7071068, 0.7071068);

            moveToWrapper(11.2454 ,-7.5, 4.7 , 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(10.6058,-7.5, 4.7, 0, 0, -0.7071068, 0.7071068 ); //Qrcode 4
            p3[3] = Double.parseDouble(getNavCamQRCodeStr(api.getBitmapNavCam()).split(" ")[1]);
            moveToWrapper(11,-7.8,5, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11.1,-7.8,5, 0, 0, -0.7071068, 0.7071068); //Qrcode 5
            p3[4] = Double.parseDouble(getNavCamQRCodeStr(api.getBitmapNavCam()).split(" ")[1]);
            moveToWrapper(11,-7.7,5, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11,-7.7,5.3, 0, 0, -0.7071068, 0.7071068); //Qrcode 6
            p3[6] = Double.parseDouble(getNavCamQRCodeStr(api.getBitmapNavCam()).split(" ")[1]);
            Log.d("Seal", "P3: " + p3.toString());

            moveToWrapper(11.1284,-7.66963,4.6, 0, 0, -0.7071068, 0.7071068);
            moveToWrapper(11.1284,-9.4,4.6, 0, 0, -0.7071068, 0.7071068);
        api.judgeSendFinishSimulation();
    }

    @Override
    protected void runPlan2(){
    }

    @Override
    protected void runPlan3(){
        // write here your plan 3
    }

    private static QRCodeReader reader = new QRCodeReader();
    private static String getNavCamQRCodeStr(Bitmap m) {
        int width = m.getWidth();
        int height = m.getHeight();
        int[] pixels = new int[2 * width * height];
        m.getPixels(pixels,0,0,0,0, width , height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        try {
            String str = reader.decode(bitmap1).getText();
            Log.d("Seal", str);
            return str;
        } catch (NotFoundException | ChecksumException | FormatException e) {
            Log.w("Seal", "error can't detect qrcode");
        }
        return null;
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
        String TAG = "seal:";
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

