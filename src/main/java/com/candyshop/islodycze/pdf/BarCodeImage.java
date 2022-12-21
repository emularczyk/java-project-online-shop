package com.candyshop.islodycze.pdf;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

public class BarCodeImage {

    public static byte[] getBarCodeImage(String text,int width, int height){
        try{
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put (EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer writer = new Code128Writer();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.CODE_128,width,height);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix,"png",byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }catch (Exception e){
            return null;
        }
    }
}
