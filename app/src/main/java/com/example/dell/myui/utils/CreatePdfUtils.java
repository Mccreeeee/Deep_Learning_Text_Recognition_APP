package com.example.dell.myui.utils;

import android.support.annotation.NonNull;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.codec.PngImage;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePdfUtils {
    private Document document;

    // savePath:保存pdf的路径
    public CreatePdfUtils(String savePath) throws FileNotFoundException, DocumentException {
        //创建新的PDF文档：A4大小，左右上下边框均为0
        document = new Document(PageSize.A4,50,50,30,30);
        //获取PDF书写器
        PdfWriter.getInstance(document, new FileOutputStream(savePath));
        //打开文档
        document.open();
    }

    public void close(){
        if (document.isOpen()) {
            document.close();
        }
    }

    // 添加图片到pdf中，这张图片在pdf中居中显示
    // imgPath:图片的路径
    // imgWidth：图片在pdf中所占的宽
    // imgHeight：图片在pdf中所占的高
    public CreatePdfUtils addImageToPdfCenterH(@NonNull String imgPath, float imgWidth, float imgHeight) throws IOException, DocumentException {
        //获取图片
        Image img = Image.getInstance(imgPath);
        img.setAlignment(Element.ALIGN_CENTER);
        img.scaleToFit(imgWidth,imgHeight);
        //添加到PDF文档
        document.add(img);

        return this;
    }

    public CreatePdfUtils addPngToPdf(Base64.InputStream inputStream) throws DocumentException, IOException {
        Image img = PngImage.getImage(inputStream);
        img.setAlignment(Element.ALIGN_CENTER);
        //添加到PDF文档
        document.add(img);
        return this;
    }

    // 添加文本到pdf中
    public CreatePdfUtils addTextToPdf(String content) throws DocumentException {
        Paragraph elements = new Paragraph(content, setChineseFont());
        elements.setAlignment(Element.ALIGN_BASELINE);
//        elements.setIndentationLeft(55);  //设置距离左边的距离
        document.add(elements); // result为保存的字符串
        return this;
    }

    // 给pdf添加个标题，居中黑体
    public CreatePdfUtils addTitleToPdf(String title){
        try {
            Paragraph elements = new Paragraph(title, setChineseTiltleFont(18));
            elements.setAlignment(Element.ALIGN_CENTER);
            document.add(elements); // result为保存的字符串
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return this;
    }

    private Font setChineseFont() {
        return setChineseFont(12);
    }

    private Font setChineseFont(int size) {
        BaseFont bf;
        Font fontChinese = null;
        try {
            // STSong-Light : Adobe的字体
            // UniGB-UCS2-H : pdf 字体
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bf, size, Font.NORMAL);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }

    private Font setChineseTiltleFont(int size) {
        BaseFont bf;
        Font fontChinese = null;
        try {
            // STSong-Light : Adobe的字体
            // UniGB-UCS2-H : pdf 字体
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bf, size, Font.BOLD);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }

}
