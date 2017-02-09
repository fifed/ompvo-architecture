package com.fifed.architecture.app.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Fedir on 26.07.2016.
 */
public class AdapterRecyclerViewUtils {
    public static String getCorectKindOfWordSubjectsOrderRU(String numberSubjects) {
       String number = numberSubjects.replace(" ","");
        if(number.length() > 2) {
            StringBuilder strbildnumber = new StringBuilder(number);
            StringBuilder num = new StringBuilder();
            num.append(strbildnumber.charAt(strbildnumber.length() - 2)).append(strbildnumber.charAt(strbildnumber.length() - 1));
            number = num.toString();
        }
        int num = Integer.parseInt(number);
        if (num == 11) {
            return numberSubjects + " товаров";
        } else if (num == 12) {
            return numberSubjects + " товаров";
        } else if (num == 13) {
            return numberSubjects + " товаров";
        } else if (num == 14) {
            return numberSubjects + " товаров";
        } else if (num % 10 == 1) {
            return  numberSubjects + " товар";
        } else if (num % 10 == 2) {
            return  numberSubjects + " товара";
        } else if (num%10 == 3){
           return numberSubjects + " товара";
        }  else if(num%10 == 4) {
            return numberSubjects + " товара";
        } else return numberSubjects + " товаров";
    }
    public static String getSumTextWithGaps(String text){
        if(text.length() < 4) return text;
        StringBuilder changingText = new StringBuilder(text);
        for(int i = changingText.length(), count = 1; i >= 0;   i--, count++){
            if((count) % 4  == 0){
                changingText.insert(i, " ");
                count++;
            }
        }
        return changingText.toString();
    }
    public static String getMoneyFormat(double source) {
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setGroupingSeparator(' ');
        custom.setDecimalSeparator(',');
        DecimalFormat decimal_formatter = new DecimalFormat("#,##0.##", custom);
        decimal_formatter.setGroupingSize(3);
        return decimal_formatter.format(source).replace(",", ".");
    }
}
