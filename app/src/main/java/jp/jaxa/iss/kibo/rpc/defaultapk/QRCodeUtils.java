package jp.jaxa.iss.kibo.rpc.defaultapk;

import java.util.*;
import java.lang.Thread;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcApi;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.android.gs.MessageType;
import gov.nasa.arc.astrobee.types.*;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.NotFoundException;
import com.google.zxing.*;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class QRCodeUtils {
  private static QRCodeReader reader = new QRCodeReader();
  private static QRCodeMultiReader multiReader = new QRCodeMultiReader();
  private static HashMap<String, Integer> judgeid = new HashMap<String, Integer>() {{
    put("pos_x", 0);
    put("pos_y", 1);
    put("pos_z", 2);
    put("qua_x", 3);
    put("qua_y", 4);
    put("qua_z", 5);
   }};

  public static Hashtable p3 = new Hashtable();

  public static void judgeQRCode(KiboRpcApi api) {
      try {
        String res = getQRCodeStr(api.getBitmapNavCam());
        Log.d("Seal", res);

        String[] arr = res.split("(,|\\s)");
        String id = arr[0];
        double n = Double.parseDouble(arr[1]);

        p3.put(id, n);
        api.judgeSendDiscoveredQR(judgeid.get(id), res);
      } catch(Exception e){
          Log.w("Seal", "QRCode Scan Failed", e);
      }
  }

  public static void judgeMultiQRCode(KiboRpcApi api) {
    try {
      String[] results = getMultiQRCodeStr(api.getBitmapNavCam());
      for(int w = 0; w < results.length; ++w) {
        String res = results[w];
        String[] arr = res.split("(,|\\s)");
        String id = arr[0];
        double n = Double.parseDouble(arr[1]);

        p3.put(id, n);
        api.judgeSendDiscoveredQR(judgeid.get(id), res);
        Log.d("Seal", res);
      }
    } catch(Exception e){
      Log.w("Seal", "QRCode Scan Failed", e);
    }
  }

  public static String getQRCodeStr(Bitmap m) throws Exception {
      int width = m.getWidth();
      int height = m.getHeight();
      int[] pixels = new int[width * height];
      m.getPixels(pixels, 0, width, 0, 0, width , height);
      RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
      BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
      String str = reader.decode(bitmap1).getText();
      return str;
  }

  public static String[] getMultiQRCodeStr(Bitmap m) throws Exception {
      int width = m.getWidth();
      int height = m.getHeight();
      int[] pixels = new int[width * height];
      m.getPixels(pixels, 0, width, 0, 0, width , height);
      RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
      BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
      com.google.zxing.Result results[] = multiReader.decodeMultiple(bitmap1);
      String[] resultsStr = new String[results.length];

      for(int w = 0; w < results.length; ++w) {
        resultsStr[w] = results[w].getText();
      }

      return resultsStr;
  }
}

