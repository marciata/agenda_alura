package br.com.marciacorp.agenda;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by marciatt on 26/07/2017.
 */
public class Localizador implements GoogleApiClient.ConnectionCallbacks, LocationListener {

    private final GoogleApiClient client;
    private final MapaFragment mapaFragment;

    public Localizador(Context context, MapaFragment mapa) {
        client = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();

        client.connect();

        this.mapaFragment = mapa;
    }

    // essa operação é assincrona e vai enviar uma atualização o tempo todo
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = new LocationRequest();
        // atualização do gps somente se o deslocamento for no mínimo 50 metros
        request.setSmallestDisplacement(50);
        // atualização do gps em intervalos de 1 em 1 segundo (1000 milisegundos)
        // o gps vai ser atualizado se o deslocamento for maior que 50 metros E a ultima atualização for a mais de 1 segundo atras
        request.setInterval(1000);
        // define a prioridade, se é a bateria, a precisao, balancear entre os dois, etc. Nesse caso foi a precisao
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng localizacao = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLng(localizacao);
        mapaFragment.centralizaEm(localizacao);
    }
}
