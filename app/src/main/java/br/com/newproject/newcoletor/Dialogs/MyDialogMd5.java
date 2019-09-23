package br.com.newproject.newcoletor.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.newproject.newcoletor.HomeActivity;
import br.com.newproject.newcoletor.R;

public class MyDialogMd5 extends AppCompatDialogFragment {
    private EditText idD5EdCodigo;
    private Button idD5BtConfirmar;
    private Button idD5BtCancelar;
    private MyDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_md5, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        idD5EdCodigo = view.findViewById(R.id.idD5EdCodigo);
        idD5BtConfirmar = view.findViewById(R.id.idD5BtConfirmar);
        idD5BtCancelar = view.findViewById(R.id.idD5BtCancelar);
        idD5BtConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = idD5EdCodigo.getText().toString();
                if (codigo.equals("")){
                    return;
                }
                listener.setText(codigo);
                dialog.dismiss();
            }
        });

        idD5BtCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MyDialogListener) context;
    }

    public interface MyDialogListener{
        void setText(String codigo);
    }
}
