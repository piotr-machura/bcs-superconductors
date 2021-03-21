/**
 * The class Complex.
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
public class Complex {

    /* The real part */
    private double re;
    /* The imaginary part */
    private double im;
    /* The sqrt(-1) */
    public static final Complex I = new Complex(0, 1);

    /* CONSTRUCTORS */
    /* ------------ */

    /**
     * Complex.
     *
     * @param re the real part
     * @param im the imaginary part
     */
    public Complex(final double re, final double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Complex with imaginary part 0.
     *
     * @param re the real part
     */
    public Complex(final double re) {
        this.re = re;
        this.im = 0;
    }

    /**
     * Complex 0.
     *
     * @param re the real part
     */
    public Complex() {
        this.re = 0;
        this.im = 0;
    }

    /**
     * String representation.
     *
     * @return string representation of Complex
     */
    public String toString() {
        if (im == 0)
            return re + "";
        if (re == 0)
            return im + "i";
        if (im < 0)
            return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    /* GETTERS & SETTERS */
    /* ----------------- */

    /**
     * Gets the real part.
     *
     * @return the real part
     */
    public double getRe() {
        return this.re;
    }

    /**
     * Sets the real part.
     *
     * @param re the real part
     */
    public void setRe(double re) {
        this.re = re;
    }

    /**
     * Gets the imaginary part.
     *
     * @return the imaginary part
     */
    public double getIm() {
        return this.im;
    }

    /**
     * Sets the imaginary part.
     *
     * @param im the imaginary part
     */
    public void setIm(double im) {
        this.im = im;
    }

    /* BASIC MATH OPERATIONS */
    /* --------------------- */

    /**
     * Complex addition.
     *
     * @param z the Complex to add
     * @return the sum of this and z
     */
    public Complex plus(Complex z) {
        return new Complex(this.re + z.re, this.im + z.im);
    }

    /**
     * Complex subtraction.
     *
     * @param z the Complex to subtract
     * @return the difference of this and z
     */
    public Complex minus(Complex z) {
        return new Complex(this.re - z.re, this.im - z.im);
    }

    /**
     * Complex multiplication.
     *
     * @param z the Complex to multiply by
     * @return the product of this and z
     */
    public Complex times(Complex z) {
        return new Complex(this.re * z.re - this.im * z.im, this.re * z.im + this.im * z.re);
    }

    /**
     * Complex division.
     *
     * @param z the Complex to divide by
     * @return this divided by z
     * @throws ArithmeticException for division by 0
     */
    public Complex div(Complex z) throws ArithmeticException {
        if ((z.re == 0) && (z.im == 0)) {
            throw new ArithmeticException("Cannot divide by 0.");
        }

        double real = (this.re * z.re) + (this.im * z.im);
        real /= (z.re * z.re + z.im * z.im);
        double imag = (this.im * z.re) - (this.re * z.im);
        imag /= (z.re * z.re + z.im * z.im);

        return new Complex(real, imag);
    }

    /**
     * Complex conjugate.
     *
     * @return the complex conjugate of this.
     **/
    public Complex conjugate() {
        return new Complex(this.re, -this.im);
    }

    /**
     * Complex modulus.
     *
     * @return the modulus of this.
     **/
    public double modulus() {
        return Math.sqrt(this.re * this.re + this.im * this.im);
    }

    /**
     * Complex modulus squared.
     *
     * @return the modulus of this squared.
     **/
    public double modulus2() {
        return this.re * this.re + this.im * this.im;
    }

    /**
     * Complex phase.
     *
     * @return the phase [0, 2pi]
     * @throws ArithmeticException for 0+0i
     */
    public double phase() throws ArithmeticException {
        double y = Math.abs(this.im);
        double x = Math.abs(this.re);
        if (this.re > 0 && this.im == 0) {
            return 0;
        } else if (this.re > 0 && this.im == 0) {
            return 0;
        } else if (this.re > 0 && this.im > 0) {
            return Math.atan(y / x);
        } else if (this.re == 0 && this.im > 0) {
            return 0.5 * Math.PI;
        } else if (this.re < 0 && this.im > 0) {
            return Math.atan(x / y) + Math.PI / 2;
        } else if (this.re < 0 && this.im == 0) {
            return Math.PI;
        } else if (this.re < 0 && this.im < 0) {
            return Math.atan(y / x) + Math.PI;
        } else if (this.re == 0 && this.im < 0) {
            return 1.5 * Math.PI;
        } else if (this.re > 0 && this.im < 0) {
            return Math.atan(x / y) + 1.5 * Math.PI;
        } else {
            throw new ArithmeticException("Phase undefined for point " + this);
        }
    }

    /**
     * The nth derivative of complex signal.
     *
     * @param signal the signal to compute derivative for
     * @param n      the derivative number
     * @return the nth derivative as a complex signal
     **/
    public static Complex[] nthDerivative(Complex[] signal, int n) {
        // TODO: Implement this
        return null;
    }
}
