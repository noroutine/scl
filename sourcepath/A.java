import java.util.*;

public class X {

    public <X> X(X x) {
        System.out.println("generic constructor");
    }

    public X(X x) {
        System.out.println("simple constructor");
    }

    public X() {
    }

    public static void main(String args[]) {
        X x = new X(new Integer(5));
        X x1 = new X(x);

        for (continue; ; ) {
            break;
        }
    }
}