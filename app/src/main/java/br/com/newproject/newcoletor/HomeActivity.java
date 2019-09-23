package br.com.newproject.newcoletor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import br.com.newproject.newcoletor.Dialogs.MyDialog;
import br.com.newproject.newcoletor.Dialogs.MyDialogManual;
import br.com.newproject.newcoletor.Dialogs.MyDialogMd5;
import br.com.newproject.newcoletor.Objects.ObjectCodBarra;
import br.com.newproject.newcoletor.Tools.Preferences;
import br.com.newproject.newcoletor.Tools.Util;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class HomeActivity extends AppCompatActivity implements MyDialog.MyDialogListener, MyDialogManual.MyDialogListener, MyDialogMd5.MyDialogListener {

    private TextView idHTxCodBarra;
    private Button idHBtManual;
    private Button idHBtUnd;
    private Button idHBtQtde;
    private Button idHBtExportacao;
    private Button idHBtLimpar;
    private ListView idHLsCodigos;
    private ZXingScannerView scannerView;
    private static boolean checked;
    public static boolean permiss;
    public static String string = "Codigo de Barras";
    private static ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        idHTxCodBarra = findViewById(R.id.idHTxCodBarra);
        idHBtManual = findViewById(R.id.idHBtManual);
        idHBtUnd = findViewById(R.id.idHBtUnd);
        idHBtQtde = findViewById(R.id.idHBtQtde);
        idHBtExportacao = findViewById(R.id.idHBtExportacao);
        idHBtLimpar = findViewById(R.id.idHBtLimpar);
        idHLsCodigos = findViewById(R.id.idHLsCodigos);

        Util.pf = new Preferences(this.getSharedPreferences("Autenticacao", this.MODE_PRIVATE));
        solicitaPermiss(Manifest.permission.ACCESS_WIFI_STATE);
        if (permiss) {
            PreencherList();
            idHTxCodBarra.setText(string);
            idHBtQtde.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Scannear(v);
                    checked = false;
                }
            });
            idHBtUnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Scannear(v);
                    checked = true;
                }
            });

            idHBtManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialog(1);
                }
            });

            idHBtExportacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Util.pf.preferences.getBoolean("valido", false))
                        Salvar();
                    else {
                        openDialog(3);
                    }
                }
            });

            idHLsCodigos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    list.remove(i);
                    PreencherList();
                    return false;
                }
            });

            idHBtLimpar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.clear();
                    PreencherList();
                }
            });
        } else
            Toast.makeText(this, "Aguardando código de Aticação!", Toast.LENGTH_LONG).show();
    }

    private void PreencherList() {
        if (list != null) {
            final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            idHLsCodigos.setAdapter(adapter);
        }
    }

    public void Scannear(View view) {
        solicitaPermiss(Manifest.permission.CAMERA);
        if (permiss) {
            scannerView = new ZXingScannerView(this);
            scannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                @Override
                public void handleResult(Result result) {
                    string = result.getText();
                    setContentView(R.layout.activity_home);
                    if (checked) {
                        setQtde(1f);
                    } else {
                        openDialog(2);
                    }
                    stopCamera();
                }
            });
            setContentView(scannerView);
            scannerView.startCamera();
        }
    }

    private boolean stopCamera() {
        if (scannerView != null) {
            scannerView.stopCamera();
            scannerView = null;
            recreate();
            return true;
        } else
            return false;
    }

    public void openDialog(Integer codigo) {
        switch (codigo) {
            case 1:
                MyDialogManual myDialogM = new MyDialogManual();
                myDialogM.setCancelable(false);
                myDialogM.show(getSupportFragmentManager(), "");
                break;
            case 2:
                MyDialog myDialog = new MyDialog();
                myDialog.setCancelable(false);
                myDialog.show(getSupportFragmentManager(), "");
                break;
            case 3:
                MyDialogMd5 myDialog5 = new MyDialogMd5();
                myDialog5.setCancelable(false);
                myDialog5.show(getSupportFragmentManager(), "");
                break;
        }
    }

    @Override
    public void setQtde(Float qtde) {
        ObjectCodBarra codBarra = new ObjectCodBarra(string, qtde);
        list.add(codBarra.getNome() + " - " + String.format("%.0f", codBarra.getQtde()));
        PreencherList();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setText(String codigo) {
        Util.autentica(codigo, this);
    }

    @Override
    public void onBackPressed() {
        if (!stopCamera())
            this.moveTaskToBack(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stopCamera();
    }

    private void solicitaPermiss(String permiss) {
        this.permiss = false;
        if (ContextCompat.checkSelfPermission(this, permiss) != 0)
            ActivityCompat.requestPermissions(this, new String[]{permiss}, 1);
        else
            this.permiss = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    finish();
                else
                    permiss = true;
            }
        }
    }

    private void Salvar() {
        solicitaPermiss(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permiss) {
            String message;
            FileOutputStream fo;
            try {
                File folder = new File(Environment.getExternalStorageDirectory() + "/PalmasLuz");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                fo = new FileOutputStream(Environment.getExternalStorageDirectory() + "/PalmasLuz/Exportacao.txt");
                String conteudo[];
                for (String s : list) {
                    conteudo = s.replace(" ", "").split("-");
                    fo.write((conteudo[0] + "," + conteudo[1] + "\n").getBytes());
                }
                fo.close();
                message = "Arquivo criado: " + folder.getPath() + "/Exportacao.txt";
            } catch (FileNotFoundException e) {
                message = e.getMessage();
            } catch (IOException e) {
                message = e.getMessage();
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            PreencherList();
        }
    }
}
