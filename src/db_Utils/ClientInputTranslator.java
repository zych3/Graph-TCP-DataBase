package db_Utils;

import java.util.HashMap;

public abstract class ClientInputTranslator {
    private static final HashMap<String, String> translator = new HashMap<>();
    static {
        translator.put("set-value", "SET_VAL");
        translator.put("get-value", "GET_VAL");
        translator.put("find-key", "FIND_KEY");
        translator.put("get-max", "GET_MAX");
        translator.put("get-min", "GET_MIN");
        translator.put("new-record", "REWRITE");
        translator.put("terminate", "KILL");
    }

    public static String translateToGDBP(String input){
        return translator.get(input);
    }
    public static HashMap<String, String> getTranslator(){
        return translator;
    }
}
