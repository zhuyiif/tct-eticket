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
        byte[] key4Sm4 = new byte[]{0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33,
                0x33};
        String b64PrivateKey = "XuxhxbX4Q4bq2/A3l+c9eYXqxzbt8aTByXMCFmmXy8g=";
        String b64Seed = "gAFJJAEBAAABEgECAwQKCAECAwQEACECkq2TQngNdUnfhGCq5AwFss45/BCWb0/r9a5eN2n9/8oBf+Xatmw/85W/cq8XuQ9nBYWfpN/FnkYoNM/D7d59YboYvAVAy0M64yQx+WHyaYfTBlD/+Wu1DGAb6X9bH1ONMDAwMDAxNTkyMTI4MTMzNzAwMDAwMDAwNzMAAATSAAAE0gEAAAEDg5D16brCGei6blfjLZIgheMAYNWm/zIuDQ03opboD3Fbh1RvAlgAFWbHuTZrDZHYDCCCaLhlSkyXxvpI7ZllnXMvbvdU+OH4NB1CxaRTSpbWt2Q9wmq+H4uOVVKds9wvYtB2G+25yMk=";
        byte[] key4Sm2Decoded = Base64.decode(b64PrivateKey, Base64.NO_WRAP);
        byte[] key4Sm2 = CryptUtils.sm4Dec(key4Sm2Decoded, key4Sm2Decoded.length, key4Sm4);
        byte[] seed4Sm2Decoded = Base64.decode(b64Seed, Base64.NO_WRAP);
        ByteBuffer byteBuffer = ByteBuffer.allocate(seed4Sm2Decoded.length+500);
        long seconds = System.currentTimeMillis()/1000;
        byte buffer[] = {(byte) ((seconds >> 24) & 0xFF), (byte) ((seconds >> 16) & 0xFF), (byte) ((seconds >> 8) & 0xFF), (byte) (seconds & 0xFF)};
        byteBuffer.put(seed4Sm2Decoded).put(buffer);
        byte[] resultOfSm2 = CryptUtils.sm2Sign(byteBuffer.array(), seed4Sm2Decoded.length+4, key4Sm2, key4Sm2.length);
        byteBuffer.put((byte) 0x15).put(resultOfSm2);
        String b64SM2SignMsg = Base64.encodeToString(byteBuffer.array(), 0, byteBuffer.position(), Base64.NO_WRAP);
        Log.d("Code Sign", b64SM2SignMsg);
        ImageView qrImage = getView().findViewById(R.id.qr_code);
//        qrImage.setImageBitmap(AwesomeQRCode.create(b64SM2SignMsg, 150, 10, 0.1f, Color.BLACK, Color.WHITE, null, false, true));
        qrImage.setImageBitmap(QRCode.from(b64SM2SignMsg).bitmap());
        super.onResume();
    }
}
