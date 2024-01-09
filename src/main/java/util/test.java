package util;

import java.util.ArrayList;

public class test {
    static int count=0;
    test(){
        count++;
    }
    public static void main(String[] args) {
        enumTest(FileNames.CART_PRODUCTS_FILE);
        testClass(ArrayList.class);
    }

    private static void testClass(Class<ArrayList> arrayListClass) {
        System.out.println(arrayListClass.getClass().getSimpleName());

    }

    static void enumTest(FileNames names){
        System.out.println(!(names instanceof FileNames));
//        System.out.println("Letter : "+names.getLetter());
    }
}
