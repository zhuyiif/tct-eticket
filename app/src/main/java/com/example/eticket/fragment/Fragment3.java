package com.example.eticket.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.eticket.R;
import com.example.eticket.local.CryptUtils;
import com.github.sumimakito.awesomeqr.AwesomeQRCode;

import net.glxn.qrgen.android.QRCode;

import java.nio.ByteBuffer;


public class Fragment3 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.layout_car_code, container, false);
    }

    @Override
    public void onResume() {
        String b64PrivateKey = "d7G9phB/vnx+VQxkaUycF9VnaXSd6vu2iJNsjXFbnm4=";
        String b64Seed = "gAFJJAEBAAAEEgECAwQKCAECAwQEACEC0qP9WCKN44F1c78/f1BCDIogcBn0Qoda2wWDxJ8YPsHI++QO22QnPwMzrw13TUYm0q59vmDPQZ0cLPf+tEfrN+haSlsUyNtNeJuTRDnSRShXTAjkRNbYSDhYnEVEr0MhAAAAAAAAAAAAAAADtPt5OQAAAAAAAAAAAAMAAATSAAAE0gEAAAEDZ+VfGIsssdDvF19N7DMxKSQqtvXUVOd0V7hj1dbVXupbfoXaAlgAABVt+W9ayRnfLHLVjS6DBFT6O1qor98A7vJ+nQnES/cNNAg5dYyRyHom+iYgIserX1ebAQEw0FaxpeAFR+cno4E=";
        byte[] key4Sm2Decoded = Base64.decode(b64PrivateKey, Base64.NO_WRAP);
        byte[] key4Sm2 = CryptUtils.sm4Dec(key4Sm2Decoded, key4Sm2Decoded.length);
        byte[] seed4Sm2Decoded = Base64.decode(b64Seed, Base64.NO_WRAP);
        ByteBuffer byteBuffer = ByteBuffer.allocate(seed4Sm2Decoded.length+500);
        long seconds = System.currentTimeMillis()/1000;
        byte buffer[] = {(byte) ((seconds >> 24) & 0xFF), (byte) ((seconds >> 16) & 0xFF), (byte) ((seconds >> 8) & 0xFF), (byte) (seconds & 0xFF)};
        byteBuffer.put(seed4Sm2Decoded).put(buffer);
        Log.d("Base64 of key4Sm2", Base64.encodeToString(key4Sm2, Base64.NO_WRAP));
        Log.d("TestSM Sign", byte2hex(CryptUtils.sm2Sign(seed4Sm2Decoded, seed4Sm2Decoded.length, key4Sm2, key4Sm2.length)));
        byte[] resultOfSm2 = CryptUtils.sm2Sign(byteBuffer.array(), seed4Sm2Decoded.length+4, key4Sm2, key4Sm2.length);
        byteBuffer.put((byte) 0x15).put(resultOfSm2);
        String b64SM2SignMsg = Base64.encodeToString(byteBuffer.array(), 0, byteBuffer.position(), Base64.NO_WRAP);
        Log.d("Code Sign", b64SM2SignMsg);
        ImageView qrImage = getView().findViewById(R.id.qr_code);
//        qrImage.setImageBitmap(AwesomeQRCode.create(b64SM2SignMsg, 150, 10, 0.1f, Color.BLACK, Color.WHITE, null, false, true));
        qrImage.setImageBitmap(QRCode.from(b64SM2SignMsg).bitmap());
        super.onResume();
    }

    private static String byte2hex(byte [] buffer){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFF).toUpperCase();
            if(temp.length() == 1){
                temp = "0" + temp;
            }
            stringBuilder.append(temp);
        }

        return stringBuilder.toString();

    }
}
