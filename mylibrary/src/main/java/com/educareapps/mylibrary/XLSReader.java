package com.educareapps.mylibrary;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.TextView;

import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by ibrar on 4/25/2017.
 */

public class XLSReader {

    public static void order(Context context, TextView textView) {
        try {

            AssetManager am = context.getAssets();// If this line gives you ERROR then try AssetManager am=getActivity().getAssets();
            InputStream is = am.open("client.xls");
            Workbook wb = Workbook.getWorkbook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            int col = s.getColumns();
            String xx = "";
            for (int i = 0; i < row; i++) {
                for (int c = 0; c < col; c++) {
                    Cell z = s.getCell(c, i);
                    xx = xx + z.getContents();

                }
                xx = xx + "\n";
            }
            textView.setText(xx);
        } catch (Exception e) {

        }

    }

}
