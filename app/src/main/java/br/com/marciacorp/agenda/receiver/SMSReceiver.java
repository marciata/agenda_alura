package br.com.marciacorp.agenda.receiver;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.marciacorp.agenda.R;
import br.com.marciacorp.agenda.dao.AlunoDAO;

/**
 * Created by marciatt on 22/07/2017.
 */
public class SMSReceiver extends BroadcastReceiver {
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // sms é guardado no intent como um pdu (protocol data unit) que é um pacote de dados
        // que trafega entre as camadas de rede, sendo que cada camada adiciona informações a mais
        // para processamento na camada correspondente do destino
        // O android vai armazenar um array de pdus que contem arrays de bytes, sendo que o primeiro
        // elemento do array é o header da mensagem
        // como um sms tem tamanho bastante limitado, se a mensagem for muito grande, esta será quebrada em
        // vários pdus de tamanho fixo
        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pduHeader = (byte[]) pdus[0];
        String format = (String) intent.getSerializableExtra("format");
        SmsMessage sms = SmsMessage.createFromPdu(pduHeader, format);
        String telefone = sms.getDisplayOriginatingAddress();

        AlunoDAO dao = new AlunoDAO(context);
        if (dao.ehAluno(telefone)){
            Toast.makeText(context, "Olha a mensagem!!", Toast.LENGTH_SHORT).show();
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }
        dao.close();
    }
}
