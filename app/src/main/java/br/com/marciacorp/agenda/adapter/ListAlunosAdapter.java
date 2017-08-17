package br.com.marciacorp.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.marciacorp.agenda.R;
import br.com.marciacorp.agenda.modelo.Aluno;

/**
 * Created by marciatt on 21/07/2017.
 */
public class ListAlunosAdapter extends BaseAdapter {

    private final Context context;
    private final List<Aluno> alunos;

    public ListAlunosAdapter(Context context, List<Aluno> alunos){
        this.context = context;
        this.alunos = alunos;
    }

    @Override
    public int getCount() {
        return alunos!=null?alunos.size():0;
    }

    @Override
    public Object getItem(int position) {
        return alunos!=null?alunos.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return alunos!=null?alunos.get(position).getId():null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Aluno aluno = alunos.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;
        if (view == null){
            view = inflater.inflate(R.layout.item_lista, parent, false);
        }

        TextView campoNome = (TextView) view.findViewById(R.id.item_lista_nome);
        campoNome.setText(aluno.getNome());

        TextView campoTelefone = (TextView) view.findViewById(R.id.item_lista_telefone);
        campoTelefone.setText(aluno.getTelefone());

        TextView campoEndereco = (TextView) view.findViewById(R.id.item_lista_endereco);
        if (campoEndereco != null){
            campoEndereco.setText(aluno.getEndereco());
        }

        TextView campoSite = (TextView) view.findViewById(R.id.item_lista_site);
        if (campoSite != null){
            campoSite.setText(aluno.getSite());
        }

        ImageView campoFoto = (ImageView) view.findViewById(R.id.item_lista_foto);
        String caminhoFoto = aluno.getFoto();
        if (caminhoFoto != null){
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

            campoFoto.setImageBitmap(bitmap);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return view;
    }
}
