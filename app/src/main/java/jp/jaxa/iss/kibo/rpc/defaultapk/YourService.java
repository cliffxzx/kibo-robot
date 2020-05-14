package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;

import gov.nasa.arc.astrobee.android.gs.MessageType;
import gov.nasa.arc.astrobee.types.*;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        api.judgeSendStart();
        Point point = new Point(1.1, -2.2, 3.3);
        Quaternion quaternion = new Quaternion(4.4f, -5.5f, 6.6f, -7.7f); api.moveTo(point, quaternion, true);
        api.moveTo(point, quaternion, true);
        Bitmap snapshot = api.getBitmapNavCam();
        String valueX = "";
        api.judgeSendDiscoveredQR(0, valueX);
        String markerId = "";
        api.laserControl(true);
        sendData(MessageType.JSON, "data", "SUCCESS:defaultapk runPlan1");
    }

    @Override
    protected void runPlan2(){
        // write here your plan 2
    }

    @Override
    protected void runPlan3(){
        // write here your plan 3
    }

}

