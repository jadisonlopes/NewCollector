package br.com.newproject.newcoletor.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.newproject.newcoletor.R;

public class MyDialog extends AppCompatDialogFragment {
    private EditText idDQEdQtde;
    private Button idDQBtConfirmar;
    private Button idDQBtCancelar;
    private MyDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_qtde, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        idDQEdQtde = view.findViewById(R.id.idDQEdQtde);
        idDQBtConfirmar = view.findViewById(R.id.idDQBtConfirmar);
        idDQBtCancelar = view.findViewById(R.id.idDQBtCancelar);
        idDQBtConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qtde = idDQEdQtde.getText().toString();
                if (qtde.equals("")){
                    return;
                }
                listener.setQtde(Float.valueOf(qtde));
                dialog.dismiss();
            }
        });

        idDQBtCancelar.setOnClickListener(new View.OnClickListener() {
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
