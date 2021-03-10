import Utils.Logger;

public class Tester {

    public static void main(String[] args) {
        Logger.debug = true;
        ListenDotMoe ldm = new ListenDotMoe();
        ldm.start();
    }
}
