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
        api.judgeSendFinishSimulation();
        api.judgeSendFinishISS();
        api.shutdownFactory();
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

