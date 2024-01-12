package util;

import java.awt.*;

public class ComponentRef {
    private static Component homeRef;

    public static void setHomeRef(Component homeRef){
        ComponentRef.homeRef = homeRef;
    }

    public static Component getHomeRef(){
        return homeRef;
    }
}
