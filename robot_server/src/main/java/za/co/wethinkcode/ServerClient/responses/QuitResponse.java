//package za.co.wethinkcode.robotworldserver;
//
//import org.json.simple.JSONObject;
//import za.co.wethinkcode.robotworldserver.world.AbstractWorld;
//
//import java.io.PrintStream;
//
//public class QuitResponse extends Response {
//    protected QuitResponse(String robotName) {
//        super("quit", robotName);
//    }
//
//    @Override
//    public boolean process(AbstractWorld world, PrintStream out) {
//        return false;
//    }
//
//    @Override
//    protected JSONObject composeResponse(AbstractWorld world) {
//        return null;
//    }
//
//    @Override
//    boolean process(AbstractWorld world) {
//        return false;
//    }
//}
