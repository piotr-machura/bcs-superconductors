import package1.Test1;

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
        
        Test1 obiekTest1 = new Test1(4, 12);
        System.out.println(obiekTest1.returnVectorLength());
    }
}
