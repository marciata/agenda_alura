package br.com.marciacorp.agenda;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import br.com.marciacorp.agenda.dao.AlunoDAO;
import br.com.marciacorp.agenda.helper.FormularioHelper;
import br.com.marciacorp.agenda.modelo.Aluno;

public class FormularioActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CAMERA = 567;
    private FormularioHelper helper;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        helper = new FormularioHelper(this);
        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");
        if (aluno != null){
            helper.getViewFromAluno(aluno);
        }

        Button botaoCamera = (Button) findViewById(R.id.formulario_botao_camera);
        botaoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent implicita para tirar fotos
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File filePath = new File(caminhoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath));
                startActivityForResult(intentCamera, REQUEST_CODE_CAMERA);
            }
        });

    }

    // vai ser chamado depois de finalizada a ação disparada pelo startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_CODE_CAMERA){
                helper.carregaImagem(caminhoFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                Aluno aluno = helper.getAlunoFromView();
                AlunoDAO dao = new AlunoDAO(this);
                if (aluno.getId() != null){
                    dao.update(aluno);
                }else{
                    dao.insereAluno(aluno);
                }
                dao.close();
                Toast.makeText(FormularioActivity.this, "Aluno "+ aluno.getNome() +" salvo!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
