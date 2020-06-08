package jp.jaxa.iss.kibo.rpc.defaultapk;

import java.util.*;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcApi;

import android.os.AsyncTask;
import android.util.Log;
import android.graphics.Bitmap;

import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.NotFoundException;
import com.google.zxing.*;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class QRCodeUtils {

    private HashMap<Integer, Double> p3;
    private QRCodeReader reader;
    private HashMap<String, Integer> idMap;
    private Map<DecodeHintType, Object> decodeHints;
    private KiboRpcApi api;

    public QRCodeUtils(KiboRpcApi api){
        this.api = api;
        reader = new QRCodeReader();
        idMap = new HashMap<String, Integer>() {{
            put("pos_x", 0);
            put("pos_y", 1);
            put("pos_z", 2);
            put("qua_x", 3);
            put("qua_y", 4);
            put("qua_z", 5);
        }};

        p3 = new HashMap<>();
        decodeHints = new HashMap<>();
        decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
    }

    public void judgeQRCode(KiboRpcApi api) {
        int loopCount = 0, maxTry = 100;
        while(loopCount++ < maxTry){
            try {
                String res = getQRCodeStr(api.getBitmapNavCam());

                Log.d("Seal", res);
                Log.d("Seal", api.getImu().getOrientation().toString());

                String[] arr = res.split(", ");
                int id = idMap.get(arr[0]);

                if(!p3.containsKey(id)) {
                    double n = Double.parseDouble(arr[1]);
                    p3.put(id, n);
                    api.judgeSendDiscoveredQR(id, res);
                    break;
                }
                Thread.sleep(100);
            } catch (FormatException e) {
                Log.w("Seal", "FormatException", e);
            } catch (ChecksumException e) {
                Log.w("Seal", "ChecksumException", e);
            } catch (NotFoundException e) {
                Log.i("Seal", "No QRCode found");
            } catch (Exception e){
                Log.e("Seal", "Unhandled Exception", e);
            }
        }
    }

    public String getQRCodeStr(Bitmap m) throws FormatException, ChecksumException, NotFoundException {
        int width = m.getWidth();
        int height = m.getHeight();
        int[] pixels = new int[width * height];
        m.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        String str = reader.decode(bitmap, decodeHints).getText();
        return str;
    }

    public double[] getP3(){
        double[] arr = new double[6];
        for(int i = 0 ; i < 6 ; i++ ){
            arr[i] = p3.get(i);
        }
        return arr;
    }
}

