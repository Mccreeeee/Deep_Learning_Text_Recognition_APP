package com.example.dell.myui.RecognizeType;

import android.app.Service;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;
import com.baidu.ocr.sdk.model.WordSimple;

import java.io.File;

public class RecognizeType {
    public interface  TypeListener{
        public void onResult(String result);
    }

    //识别银行卡账号
    public static void recBankCard(Context context, String filePath, final TypeListener typeListener){
        BankCardParams param = new BankCardParams();
        param.setImageFile(new File(filePath));
        OCR.getInstance(context).recognizeBankCard(param, new OnResultListener<BankCardResult>() {
            @Override
            public void onResult(BankCardResult bankCardResult) {
                String result = String.format("卡号：%s\n类型：%s\n发卡行：%s",
                        bankCardResult.getBankCardNumber(),
                        bankCardResult.getBankCardType(),
                        bankCardResult.getBankName());
                typeListener.onResult(result);
            }

            @Override
            public void onError(OCRError ocrError) {
                typeListener.onResult(ocrError.getMessage());
            }
        });
    }

    //识别手写体
    public static void recHandWriting(Context context,String filePath,final TypeListener typeListener){
        OcrRequestParams params = new OcrRequestParams();
        params.setImageFile(new File(filePath));
        OCR.getInstance(context).recognizeHandwriting(params, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult ocrResponseResult) {
                typeListener.onResult(ocrResponseResult.getJsonRes());

            }

            @Override
            public void onError(OCRError ocrError) {
                typeListener.onResult(ocrError.getMessage());

            }
        });
    }

    //网络图片文字识别(用于文档图片和pdf图片)
    public static void recWebimage(Context context,String filePath,final TypeListener typeListener){
        GeneralBasicParams params = new GeneralBasicParams();
        params.setDetectDirection(true);
        params.setImageFile(new File(filePath));
        OCR.getInstance(context).recognizeGeneralEnhanced(params, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult generalResult) {
                StringBuffer sb = new StringBuffer();
                for (WordSimple wordSimple : generalResult.getWordList()) {
                    WordSimple word = wordSimple;
                    sb.append(word.getWords());
                    sb.append("\n");
                }
                typeListener.onResult(generalResult.getJsonRes());
            }

            @Override
            public void onError(OCRError ocrError) {
                typeListener.onResult(ocrError.getMessage());

            }
        });

    }
}
