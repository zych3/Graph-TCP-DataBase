package db_Utils;

import java.util.HashMap;

public abstract class ClientInputValidator {

    public static boolean Validate(String input)
    {
        String[] words = input.split(" ");
        return Validate(words);
    }
    public static boolean Validate(String[] words_input){
        if(words_input.length < 4 || words_input.length > 6)
            return false;

        boolean validOp = ClientInputTranslator.getTranslator().containsKey(words_input[3]);
        return validOp;
    }
}
