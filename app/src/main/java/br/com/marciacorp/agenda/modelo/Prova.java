package br.com.marciacorp.agenda.modelo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marciatt on 24/07/2017.
 */
public class Prova implements Serializable {
    private String materia;
    private String data;
    private List<String> topicos;

    public Prova(String materia, String data, List<String> topicos) {
        this.materia = materia;
        this.data = data;
        this.topicos = topicos;
    }

    public String getMateria() {
        return materia;
    }

    public String getData() {
        return data;
    }

    public List<String> getTopicos() {
        return topicos;
    }

    @Override
    public String toString() {
        return this.getMateria();
    }
}
