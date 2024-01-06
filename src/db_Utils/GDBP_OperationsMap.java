package db_Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class GDBP_OperationsMap {



    private static final HashMap<String, Integer> map = new HashMap<>();

    static {
        //---Digital handshake---
        map.put("CON_REQ_NOD", 1);
        map.put("CON_REQ_CLI", 1<<1);
        map.put("CON_ACC", 1 << 2);

        //--DB_Client actions---
        map.put("SET_VAL", 1 << 3);
        map.put("GET_VAL", 1 << 4);
        map.put("FIND_KEY", 1 << 5);
        map.put("GET_MAX", 1 << 6);
        map.put("GET_MIN", 1 << 7);
        map.put("REWRITE", 1 << 8);
        map.put("KILL", 1 << 9);

        //--Internal communication--
        map.put("CURR_PROC", 1 << 10);
        map.put("CURR_FREE", 1 << 11);
        /*map.put("N_FIND_KEY", 1 << 11);
        map.put("N_GET_MAX", 1 << 12);
        map.put("N_GET_MIN", 1 << 13);
        map.put("N_REWRITE", 1 << 14);
        map.put("N_KILL", 1 << 15);*/

    }

    public static HashMap<String, Integer> getMap(){
        return map;
    }

    private static final HashMap<Integer, String> r_map = new HashMap<>();

    static {
        //---Digital handshake---
        r_map.put(1, "CON_REQ_NOD");
        r_map.put(1 << 1, "CON_REQ_CLI");
        r_map.put(1 << 2, "CON_ACC");

        //--DB_Client actions---
        r_map.put(1 << 3, "SET_VAL");
        r_map.put(1 << 4, "GET_VAL");
        r_map.put(1 << 5, "FIND_KEY");
        r_map.put(1 << 6, "GET_MAX");
        r_map.put(1 << 7, "GET_MIN");
        r_map.put(1 << 8, "REWRITE");
        r_map.put(1 << 9, "KILL");

        //--Internal communication--
        r_map.put(1 << 10, "CURR_PROC");
        r_map.put(1 << 11, "CURR_FREE");
        /*map.put(1 << 12, "N_FIND_KEY");
        map.put(1 << 13, "N_GET_MAX");
        map.put(1 << 14, "N_GET_MIN");
        map.put(1 << 15, "N_REWRITE");
        map.put(1 << 16, "N_KILL");*/
    }

    public static HashMap<Integer, String> get_r_map() {
        return r_map;
    }

    private static final Set<Integer> noArgsOps = new HashSet<>();

    static
    {
        noArgsOps.add(0);
        noArgsOps.add(1);
        noArgsOps.add(1 << 1);
        noArgsOps.add(1 << 5);
        noArgsOps.add(1 << 6);
        noArgsOps.add(1 << 8);
        noArgsOps.add(1 << 9);
        noArgsOps.add(1 << 10);

    }
    public static Set<Integer> getNoArgsOps() {
        return noArgsOps;
    }

    private static final Set<Integer> oneArgOps = new HashSet<>();
    static {
        oneArgOps.add(1 << 5);
        oneArgOps.add(1 << 4);
    }

    public static Set<Integer> getOneArgOps(){
        return oneArgOps;
    }

    private static final Set<Integer> twoArgOps = new HashSet<>();
    static {
        twoArgOps.add(1 << 3);
        twoArgOps.add(1 << 8);
    }

    public static Set<Integer> getTwoArgOps(){
        return twoArgOps;
    }



}
