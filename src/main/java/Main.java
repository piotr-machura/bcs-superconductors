/**
 * The class Main
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
import org.jtransforms.fft.DoubleFFT_1D;

public class Main {
    public static void main(String[] args) {
        DoubleFFT_1D fft = new DoubleFFT_1D(10);
        System.out.println(fft);
    }
}
