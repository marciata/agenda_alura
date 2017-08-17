package br.com.marciacorp.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import br.com.marciacorp.agenda.converter.AlunosConverter;
import br.com.marciacorp.agenda.dao.AlunoDAO;

/**
 * Created by marciatt on 23/07/2017.
 */
// A classe AsyncTask indica uma operacao que será executada em background
public class EnviaAlunoTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private ProgressDialog dialog;

    public EnviaAlunoTask(Context context) {
        this.context = context;
    }

    // esse método será executada na thread principal antes de comecar a operacao de segundo plano
    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde", "Enviando notas..", true, true);
    }

    // aqui fica a operacao que sera executada em background. O tipo do retorno desse metodo é o terceiro parametro do
    // generics no AsyncTask. O parametro desse metodo é o primeiro parametro do AsyncTask. Nesse caso nao foi utilizado por
    // isso é void. A declaracao original é AsynTask<Object, Object, Object>
    @Override
    protected String doInBackground(Void... params) {
        AlunoDAO dao = new AlunoDAO(context);
        AlunosConverter converter = new AlunosConverter();
        String json = converter.convertToJson(dao.buscaAlunos());
        System.out.println(json);

        WebClient client = new WebClient();
        String resposta = client.post(json);
        return resposta;
    }

    // O parametro recebido nesse método é o retorno do metodo doInBackground
    // Esse método será executado na thread principal e depois do metodo doInBackground
    @Override
    protected void onPostExecute(String resposta) {
        dialog.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
    }
}
