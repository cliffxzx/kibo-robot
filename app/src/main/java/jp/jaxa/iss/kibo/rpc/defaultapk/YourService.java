package jp.jaxa.iss.kibo.rpc.defaultapk;

import java.lang.Thread;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.*;
import android.util.Log;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        api.judgeSendStart();

        QRCodeUtils task = new QRCodeUtils(api);

        //QR Code pos_z 2
        moveToWrapper(11.236, -5.512, 4.499, 0.498, 0.489, -0.493, 0.519);

        task.judgeQRCode(api);

        //QR Code pos_x 0
        moveToWrapper(11.346, -5.714, 4.499, 0.007, 0.01, -0.194, 0.981);

        task.judgeQRCode(api);

        //QR Code pos_y 1
        moveToWrapper(10.892, -6.006, 5.4, 0.474, -0.498, 0.54, 0.486);

        task.judgeQRCode(api);

        //繞牆
        moveToWrapper(10.4643, -6.06433, 5, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(10.6331, -6.7, 5, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.32, -6.7, 5, 0, 0, -0.7071068, 0.7071068);

        //QR Code qua_y
        moveToWrapper(11.30, -8.009, 4.998, -0.057, 0.016, 0.366, 0.929);

        task.judgeQRCode(api);

        //QR Code qua_x
        moveToWrapper(10.524, -7.523, 4.712, -0.132, 0.041, 0.956, 0.256);

        task.judgeQRCode(api);

        //QR Code qua_z
        moveToWrapper(10.994, -7.691, 5.333, 0.001, -0.457, 0.066, 0.887);

        task.judgeQRCode(api);

        moveToWrapper(11.0565,-7.7,4.62372,0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.0565,-9.27526,4.62372,0, 0, -0.7071068, 0.7071068);

        runFinal(task.getP3());
    }

    @Override
    protected void runPlan2(){
        api.judgeSendStart();
        //QRcode 1 2 3
        moveToWrapper(11,-6,5.3, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11,-6,5.3, 0.375, 0.000,0.927,0.000);

        //繞牆
        moveToWrapper(10.425,-6,4.7, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(10.425,-6.75325,4.7, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.5161,-6.75325,4.7, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.5161,-7.5065,4.7, 0, 0, -0.7071068, 0.7071068);

        //QRcode qua
        moveToWrapper(10.6,-7.5065,4.7, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.5161,-7.5065,4.7,-0.322, 0.000,0.947,0);


        moveToWrapper(11.0217,-7.5065,4.67696, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.0217,-9.3,4.67696, 0, 0, -0.7071068, 0.7071068);
        api.judgeSendFinishSimulation();
    }

    @Override
    protected void runPlan3(){
        api.judgeSendStart();

        new QRCodeAsyncTask(new QRCodeAsyncTask.AsyncResponse(){
            @Override
            public void processFinish(HashMap<Integer, Double> result) {
                final double[] p3 = new double[6];
                for(int i = 0 ; i < 6 ; i++ ){
                    p3[i] = result.get(i);
                }
                runFinal(p3);
            }
        }).execute(api);

        //QR Code pos_z
        moveToWrapper(11.2331, -5.5, 4.50006, 0.5, 0.5, -0.5, 0.5);

        //QR Code pos_x
        moveToWrapper(11.35, -5.71366, 4.50006, 0, 0, 0, 1);

        //QR Code pos_y
        moveToWrapper(10.9, -6, 5.4, 0.5, -0.5, 0.5, 0.5);

        //繞牆
        moveToWrapper(10.4643, -6.06433, 5, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(10.6331, -6.7, 5, 0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.32, -6.7, 5, 0, 0, -0.7071068, 0.7071068);

        //QR Code qua_y
        moveToWrapper(11.32, -8, 5, 0, 0, 0, 1);

        //QR Code qua_x
        moveToWrapper(10.5, -7.5, 4.7, 0, 0, 1, 0);

        //QR Code qua_z
        moveToWrapper(11, -7.7, 5.35, 0.5, -0.5, 0.5, 0.5);

        moveToWrapper(11.0565,-7.7,4.62372,0, 0, -0.7071068, 0.7071068);
        moveToWrapper(11.0565,-9.27526,4.62372,0, 0, -0.7071068, 0.7071068);
    }

    private void runFinal(double[] p3){
        Log.d("Seal", Arrays.toString(p3));

        double qua_w = Math.sqrt(1 - Math.pow(p3[3], 2) + Math.pow(p3[4], 2) + Math.pow(p3[5], 2));

        moveToWrapper(p3[0], p3[1], p3[2], p3[3], p3[4], p3[5], qua_w);

        ARTagUtils.judgeARTag(api);
        api.laserControl(true);
        api.judgeSendFinishSimulation();
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

            Log.d(TAG, "MoveTo->Point:" + point.toString() + ",Counter:"+loopCounter+" Result->Status:" + result.getStatus());
            result = api.moveTo(point, quaternion, true);
        }
    }
}

