package com.cakeshop.product;

public enum Cake {
    YELLOW_BUTTER_CAKE("yellow butter cake",1, 5000, 2000, 4000, 4000),
    POUND_CAKE("pound cake", 2, 3000,3000, 3000, 7000),
    RED_VELVET_CAKE("red velvet cake", 3,2000, 5000, 6000, 4000),
    CARROT_CAKE("carrot cake", 4, 3000,3000, 5000, 5000),
    SPONGE_CAKE("sponge cake",5, 6000,2000, 3000, 2000),
    GENOISE_CAKE("genoise cake",6, 3000,5000, 2000, 5000),
    CHIFON_CAKE("chiffon cake",7, 4000,4000, 4000, 6000),
    FLOURLESS_CAKE("flourless cake",8,2000, 2000, 2000, 7000),
    UPSIDE_DOWN_CAKE("upside down cake",9,5000, 3000, 4000, 3000),
    HUMMING_BIRD_CAKE("hummingbird cake",10,3000, 5000, 4500, 3000),
    FRUIT_CAKE("fruit cake",11, 1000,3000, 6000, 4000),
    SIMPLE_CAKE("simple cake",12, 1000,1000, 1000, 3000);

    private String name;
    private int id;
    private int doughDuration;
    private int creamDuration;
    private int decorationsDuration;
    private int deliveryDuration;

    Cake(String name, int id, int doughDuration, int creamDuration, int decorationsDuration, int deliveryDuration){
        this.name = name;
        this.id = id;
        this.doughDuration = doughDuration;
        this.creamDuration = creamDuration;
        this.decorationsDuration = decorationsDuration;
        this.deliveryDuration = deliveryDuration;
    };

    public int getDoughDuration() {
        return doughDuration;
    }

    public int getCreamDuration() {
        return creamDuration;
    }

    public int getDecorationsDuration() {
        return decorationsDuration;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public static Cake getCakeById(int id){
        for (Cake cake : values()){
            if(cake.id == id) return cake;
        }
        return SIMPLE_CAKE;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Cake{" +
                "name='" + name + '\'' +
                ", doughDuration=" + doughDuration +
                ", creamDuration=" + creamDuration +
                ", decorationsDuration=" + decorationsDuration +
                ", deliveryDuration=" + deliveryDuration +
                '}';
    }
}
