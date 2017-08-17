package br.com.marciacorp.agenda;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.com.marciacorp.agenda.dao.AlunoDAO;
import br.com.marciacorp.agenda.modelo.Aluno;

/**
 * Created by marciatt on 26/07/2017.
 */
public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mapa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // solicita ao servico do google para sincronizar o mapa
        getMapAsync(this);
    }

    // esse método será chamado quando o mapa estiver pronto
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mapa = googleMap;
        LatLng posicaoDaEscola = pegaLocalizacao("Rua Vergueiro 3185, Vila Mariana, São Paulo");
        if (posicaoDaEscola != null){
            centralizaEm(posicaoDaEscola);
        }

        AlunoDAO dao = new AlunoDAO(getContext());
        List<Aluno> alunos = dao.buscaAlunos();
        for (Aluno a : alunos){
            LatLng latLng = pegaLocalizacao(a.getEndereco());
            if (latLng != null){
                // para colocar um pin no mapa em cada localizacao de cada aluno
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLng);
                marker.title(a.getNome());
                marker.snippet(String.valueOf(a.getNota()));
                googleMap.addMarker(marker);
            }
        }

        dao.close();

        new Localizador(getContext(), MapaFragment.this);
    }

    private LatLng pegaLocalizacao(String endereco) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            // transformar um endereco em uma localizacao baseada em latitude e longitude. O segundo parametro indica quantos enderecos devem ser retornados
            List<Address> locationName = geocoder.getFromLocationName(endereco, 1);
            if (!locationName.isEmpty()){
                LatLng posicao = new LatLng(locationName.get(0).getLatitude(), locationName.get(0).getLongitude());
                return posicao;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void centralizaEm(LatLng localizacao) {
        if (mapa != null){
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(localizacao, 17);
            mapa.moveCamera(update);

        }
    }
}
