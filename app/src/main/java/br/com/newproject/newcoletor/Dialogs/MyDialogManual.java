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

public class MyDialogManual extends AppCompatDialogFragment {
    private EditText idDMEdCodigo;
    private EditText idDMEdQtde;
    private Button idDMBtConfirmar;
    private Button idDMBtCancelar;
    private MyDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_manual, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        idDMEdCodigo = view.findViewById(R.id.idDMEdCodigo);
        idDMEdQtde = view.findViewById(R.id.idDMEdQtde);
        idDMBtConfirmar = view.findViewById(R.id.idDMBtConfirmar);
        idDMBtCancelar = view.findViewById(R.id.idDMBtCancelar);
        idDMBtConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = idDMEdCodigo.getText().toString();
                String qtde   = idDMEdQtde.getText().toString();
                if (qtde.equals("") || codigo.equals("")){
                    return;
                }
                HomeActivity.string = codigo;
                listener.setQtde(Float.valueOf(qtde));
                dialog.dismiss();
            }
        });

        idDMBtCancelar.setOnClickListener(new View.OnClickListener() {
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
        void setQtde(Float qtde);
    }
}
