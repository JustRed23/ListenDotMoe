import Utils.Logger;

import java.util.Arrays;

public class Tester {

    public static void main(String[] args) {
        ListenDotMoe ldm = new ListenDotMoe();
        if (Arrays.asList(args).contains("debug"))
            Logger.debug = true;
        ldm.start();
    }
}
