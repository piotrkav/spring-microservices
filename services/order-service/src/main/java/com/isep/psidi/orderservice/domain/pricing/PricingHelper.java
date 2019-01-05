package com.isep.psidi.orderservice.domain.pricing;

import com.isep.psidi.orderservice.domain.color.ColorType;

public class PricingHelper {

    public static double KNITTING_PRICE_PER_KG = 0.30;
    public static double PRETREATMENT_PRICE_PER_KG = 0.50;
    public static double STAMPING_PRICE = 1.30;
    public static double FINISHING_PRICE = 0.3;
    public static double INSPECTION_PRICE = 0.15;
    public static double PACKING_PRICE = 0.10;

    public static double getColorPriceOfDyeing(ColorType colorType) {
        switch (colorType) {
            case LIGHT:
                return 0.80;
            case MEDIUM:
                return 1.10;
            case DARK:
                return 1.55;
            default:
                return 0.0;
        }
    }

}
