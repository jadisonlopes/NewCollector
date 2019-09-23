package br.com.newproject.newcoletor.Tools;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util extends Activity {

    public static Preferences pf;

    public Util() {
    }

    public static String getMac(Context c){
        WifiManager manager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getMD5(String s){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(StandardCharsets.UTF_8.encode(s));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Boolean autentica(String senha, Context c){
        if ( !pf.preferences.getBoolean("valido", false) ) {
            String mac = Util.getMac(c);
            String md5 = getMD5(mac.substring(0, 2) + "palmas" + mac.substring(3, 5)).substring(0, 5);
            if ( md5.equals(senha) )
                pf.editor.putBoolean("valido",true);
            else {
                pf.editor.putBoolean("valido", false);
                Toast.makeText(c,"Senha Inválida!",Toast.LENGTH_LONG).show();
            }
            pf.editor.commit();
        }
        return pf.preferences.getBoolean("valido",false);
    }
}
