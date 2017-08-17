package br.com.marciacorp.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

import br.com.marciacorp.agenda.adapter.ListAlunosAdapter;
import br.com.marciacorp.agenda.converter.AlunosConverter;
import br.com.marciacorp.agenda.dao.AlunoDAO;
import br.com.marciacorp.agenda.modelo.Aluno;

public class ListaAlunosActivity extends AppCompatActivity {

    public static final int RECEIVE_SMS = 111;
    public static final int CALL_PHONE = 123;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);
        list = (ListView)findViewById(R.id.lista_alunos);

        if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Aluno aluno = (Aluno) list.getItemAtPosition(position);
                // intent explicita - indicando explicitamente pra onde ir
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intent.putExtra("aluno", aluno);
                startActivity(intent);
            }
        });

        Button botaoCadastrarAlunos = (Button) findViewById(R.id.lista_novo_aluno);
        assert botaoCadastrarAlunos != null;
        botaoCadastrarAlunos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vaiProForm = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(vaiProForm);
            }
        });

        registerForContextMenu(list);
    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscaAlunos();

//        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        ListAlunosAdapter adapter = new ListAlunosAdapter(this, alunos);

        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) list.getItemAtPosition(info.position);

        MenuItem ligar = menu.add("Ligar");
        ligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE);
                }else{
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intent);
                }
                return false;
            }
        });

        MenuItem mandarSMS = menu.add("Enviar SMS");
        Intent intentMandarSMS = new Intent(Intent.ACTION_VIEW);
        // os protocolos que devem ser utilizados para cada tipo de ação está na documentacao do android
        // https://developer.android.com/training/basics/intents/sending.html
        intentMandarSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        mandarSMS.setIntent(intentMandarSMS);

        MenuItem visualizarMapa = menu.add("Visualizar no mapa");
        Intent intentVisualizarMapa = new Intent(Intent.ACTION_VIEW);
        intentVisualizarMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        visualizarMapa.setIntent(intentVisualizarMapa);

        MenuItem visitarSite = menu.add("Visitar Site");
        // intent implicita - indicando apenas a ação que deseja realizar. O Android procurará a activity correta para tratar a ação
        Intent intentVisitarSite = new Intent(Intent.ACTION_VIEW);
        String url = aluno.getSite();
        if (!aluno.getSite().startsWith("http")){
            url = "http://" + url;
        }
        intentVisitarSite.setData(Uri.parse(url));
        // o set já vai implementar o onclicklistener e fazer um startActivity
        visitarSite.setIntent(intentVisitarSite);

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.delete(aluno);
                dao.close();
                Toast.makeText(ListaAlunosActivity.this, "Aluno "+ aluno.getId() +" deletado", Toast.LENGTH_SHORT).show();
                carregaLista();
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // esse método sera chamado logo apos o usuario conceder ou negar permissoes. Qualquer permissao necessaria na aplicacao cairá
        // nesse metodo, por isso existe um requestCode, que identificará qual permissao exatamente foi concedida/negada
        if (requestCode == CALL_PHONE){
            if (isPermissionGranted(permissions, grantResults, Manifest.permission.CALL_PHONE)){
                System.out.println("Permissao de chamada telefonica concedida");
            }else{
                System.out.println("Permissao de chamada telefonica negada");
            }
        }
    }

    private boolean isPermissionGranted(String[] permissions, int[] grantResult, String permission){
        for(int index = 0; index < permissions.length; index++){
            if (permissions[index].equals(permission)){
                if (grantResult[index] == PackageManager.PERMISSION_GRANTED){
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    // criando um menu na janela da lista de alunos
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_enviar_notas:{
                // essa classe será executada em background, em outra thread pois o android nao
                // aceita que uma operação que pode demorar, nesse caso o acesso a internet, seja
                // executada na thread principal
                new EnviaAlunoTask(this).execute();
                break;
            }
            case R.id.menu_baixar_provas: {
                Intent intent = new Intent(this, ProvasActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_acessar_mapa: {
                Intent intent = new Intent(this, MapaActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
