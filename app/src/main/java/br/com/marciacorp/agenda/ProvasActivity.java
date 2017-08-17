package br.com.marciacorp.agenda;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import br.com.marciacorp.agenda.modelo.Prova;

public class ProvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.replace(R.id.frame_principal, new ListaProvasFragment());

        if (isLandscape()){
            tx.replace(R.id.frame_secundario, new DetalhesProvaFragment());
        }

        tx.commit();

    }

    private boolean isLandscape() {
        return getResources().getBoolean(R.bool.modoPaisagem);
    }

    public void selecionaProva(Prova prova) {
        FragmentManager manager = getSupportFragmentManager();
        if (!isLandscape()){
            FragmentTransaction tx = manager.beginTransaction();
            DetalhesProvaFragment detalhesProvaFragment = new DetalhesProvaFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("prova", prova);
            detalhesProvaFragment.setArguments(bundle);
            tx.replace(R.id.frame_principal, detalhesProvaFragment);
            // para que o android empilhe o q foi feito antes de montar esse fragment, dessa forma eu consigo fazer com
            // que, ao clicar no botao back, ele volte a exibir o fragment da lista de provas, ao inves de matar a
            // activity e voltar pra lista de alunos, q é o comportamento padrao.
            // o parametro desse método é um label para identificar essa operação
            tx.addToBackStack(null);

            tx.commit();
        }else{
            // no modo paisagem eu nao preciso voltar o fragment pq tanto o fragment de prova qto de detalhes de prova estão
            // sendo exibidos juntos, entao quando clicar no botao voltar do celular, quero q realmente mate a activity de provas
            // e volte pra lista de alunos
            DetalhesProvaFragment detalhesFragment = (DetalhesProvaFragment) manager.findFragmentById(R.id.frame_secundario);
            detalhesFragment.populaCamposCom(prova);
        }
    }
}
