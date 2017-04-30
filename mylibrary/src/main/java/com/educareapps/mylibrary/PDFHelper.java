package com.educareapps.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Probook 440 on 2/5/2017.
 */

public class PDFHelper {
    Activity activity;
    File makeFile;

    public PDFHelper(Activity activity) {
        this.activity = activity;
    }

    /////////////////******************************* HELPER METHOD ARE START HERE ***************************//////////////////////////////////////////////////

    /// take screenshot method used in generatePdf method
    @NonNull
    private Image takeScreenShot(Document doc) throws BadElementException, IOException {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        float scaler = ((doc.getPageSize().getWidth() - doc.leftMargin()
                - doc.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
        image.scalePercent(scaler);
        image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
        return image;
    }

    private int newWidth = 100;
    private int newHeight = 100;

    // convert bitmap to itext liberay image formate to attach on pdf docs
    private Image convertBitmapToImage(Document doc, Bitmap bitmap) throws IOException, BadElementException {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        float scaler = ((doc.getPageSize().getWidth() - doc.leftMargin()
                - doc.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
        image.scalePercent(scaler);
        image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
        return image;
    }

    /// return file that created for save
    public File getMakeFile() {
        return makeFile;
    }

    //// view the generated pdf
    public void viewPDF() {
        File file = this.getMakeFile();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }

    /// method that check the external storage is readonly or not
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    /// method that check the external storage space is available or not
    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    /*  image resize and get circular method*/
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.e("Progress: ", String.valueOf(height) + "," + String.valueOf(width));

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Log.e("New Progress: ", String.valueOf(scaleHeight) + "," + String.valueOf(scaleWidth));
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width <= 1 ? 10 : width, height <= 1 ? 10 : height, matrix, false);
        //  bm.recycle();
        bm = null;
        return resizedBitmap;
    }
/////////////////******************************* HELPER METHOD ARE ENDS HERE ***************************//////////////////////////////////////////////////


    ///////////////////////************************** STATISTIC METHOD FOR VEGAN TRAVEL APP *******************///////////////////////////////////////////////////

    /*METHOD THAT GENERATE PDF U HAVE TO PROVIDE
    TITLE, SUBTITLE , AND ALL SCREEN SHOT LIST TO PRODUCE A PDF
    PDF WILL SAVE IN DOWNLOAD FOLDER 
     */
    private boolean isPdfGenerate = false;

    public boolean generateStatisticPdf(String userName, Bitmap userPhoto, String title, String subTitle, String details) {
        Document doc = new Document();

        if (isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            makeFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), userName + "_" + System.currentTimeMillis() + ".pdf");

            Log.e("makefile: ", makeFile.getAbsolutePath());
            try {
                PdfWriter.getInstance(doc, new FileOutputStream(makeFile));
                doc.open();
                Image screenShotImage = convertBitmapToImage(doc, userPhoto);
                screenShotImage.scaleAbsolute(100f, 100f);
                screenShotImage.setBorder(Rectangle.BOX);
                screenShotImage.setBorderColor(BaseColor.ORANGE);
                screenShotImage.setBorderWidth(30);
                doc.add(screenShotImage);

                Paragraph para1 = new Paragraph(title);
                para1.setAlignment(Paragraph.ALIGN_CENTER);
                para1.setSpacingAfter(20);
                doc.add(para1);
                doc.add(new Phrase("\n"));

                Paragraph para2 = new Paragraph(subTitle);
                para1.setAlignment(Paragraph.ALIGN_CENTER);
                para1.setSpacingAfter(20);
                doc.add(para2);
                doc.add(new Phrase("\n"));

                Paragraph para3 = new Paragraph(details);
                para1.setAlignment(Paragraph.ALIGN_CENTER);
                para1.setSpacingAfter(20);
                doc.add(para3);
                doc.close();
                isPdfGenerate = true;
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return isPdfGenerate;
    }

}
