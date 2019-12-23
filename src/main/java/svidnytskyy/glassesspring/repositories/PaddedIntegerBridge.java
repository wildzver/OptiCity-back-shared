package svidnytskyy.glassesspring.repositories;

import org.hibernate.search.bridge.StringBridge;

public class PaddedIntegerBridge implements StringBridge {

    private int padding = 10;

    public String objectToString(Object object) {
        String rawInteger = ((Integer) object).toString();
        if (rawInteger.length() > padding)
            throw new IllegalArgumentException("Number too big to be padded");
        StringBuilder paddedInteger = new StringBuilder();
        for (int padIndex = rawInteger.length(); padIndex < padding; padIndex++) {
            paddedInteger.append('0');
        }
        return paddedInteger.append( rawInteger ).toString();
    }
}