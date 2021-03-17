import package1.*;
import package2.*;

/**
 * The class Main
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Hello World!");

        System.out.println("HeLL0!");
        System.out.println(Test.getTrue());

        Test1 obiekTest1 = new Test1(4, 12);
        System.out.println(obiekTest1.returnVectorLength());
        System.out.println(Test2.pierwistek(81));
    }
}
