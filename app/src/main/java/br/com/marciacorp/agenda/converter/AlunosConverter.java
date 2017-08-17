package br.com.marciacorp.agenda.converter;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import br.com.marciacorp.agenda.modelo.Aluno;

/**
 * Created by marciatt on 23/07/2017.
 */
public class AlunosConverter {
    public String convertToJson(List<Aluno> alunos) {
        // objeto para montagem do json
        JSONStringer js = new JSONStringer();
        try {
            // abre { para iniciar um objeto json
            js.object();
            // monta uma chave contendo um array de dados listAlunos: [
            js.key("list").array();
            // monta uma chave { aluno: [
            js.object().key("aluno").array();
            for (Aluno a : alunos){
                // {
                js.object();
                // monta uma propriedade nome: Nome do aluno
                js.key("nome").value(a.getNome());
                // monta uma propriedade nota: 10
                js.key("nota").value(a.getNota());
                // }
                js.endObject();
            }
            js.endArray();
            js.endObject();
            // fecha o array listAlunos ]
            js.endArray();
            // fecha } para fechar um objeto json
            js.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return js.toString();
        /*
        * O retorno vai ser
        * {
        *   "list": [
        *       {
        *           "aluno": [
        *               "nome": "nome do aluno",
        *               "nota": 10
        *           ]
        *       }
        *   ]
        * }
        *
        * */
    }
}
