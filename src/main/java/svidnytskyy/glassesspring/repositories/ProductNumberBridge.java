package svidnytskyy.glassesspring.repositories;

import org.hibernate.search.bridge.StringBridge;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ProductNumberBridge implements StringBridge {

    private int modelNumberPadding = 5;
    private int lensColorPadding = 2;
    private int frameColorPadding = 2;

    public String objectToString(Object object) {
        System.out.println("MY OBJECT" + object);
        String[] rawProductNumber = (object).toString().split("_");
        System.out.println("rawProductNumber" + Arrays.toString(rawProductNumber));
        String rawModelNumber = rawProductNumber[0];
        String rawLensColor = rawProductNumber[1];
        String rawFrameColor = rawProductNumber[2];
        if (rawModelNumber.length() > modelNumberPadding)
            throw new IllegalArgumentException("Model number too big to be padded");
        if (rawLensColor.length() > lensColorPadding)
            throw new IllegalArgumentException("Lens color too big to be padded");
        if (rawFrameColor.length() > frameColorPadding)
            throw new IllegalArgumentException("Frame color too big to be padded");
        StringBuilder paddedModelNumber = new StringBuilder();
        for (int padIndex = rawModelNumber.length(); padIndex < modelNumberPadding; padIndex++) {
            paddedModelNumber.append('0');
        }
        StringBuilder paddedLensColor = new StringBuilder();
        for (int padIndex = rawLensColor.length(); padIndex < lensColorPadding; padIndex++) {
            paddedLensColor.append('0');
        }
        StringBuilder paddedFrameColor = new StringBuilder();
        for (int padIndex = rawFrameColor.length(); padIndex < frameColorPadding; padIndex++) {
            paddedFrameColor.append('0');
        }

        String newProductNumber = paddedModelNumber.append(rawModelNumber)
                .append(paddedLensColor).append(rawLensColor)
                .append(paddedFrameColor).append(rawFrameColor)
                .toString();
        System.out.println("MY NEW PN" + newProductNumber);
        return newProductNumber;
//        return "asd";
    }
}