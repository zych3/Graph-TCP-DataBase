package db_Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class GDBP_OperationsMap {



    private static final HashMap<String, Integer> map = new HashMap<>();

    static {
        //---Digital handshake---
        map.put("CON_REQ_NOD", 0);
        map.put("CON_REQ_CLI", 1);
        map.put("CON_ACC", 2);

        //--DB_Client actions---
        map.put("SET_VAL", 3);
        map.put("GET_VAL", 4);
        map.put("FIND_KEY", 5);
        map.put("C_GET_MAX", 6);
        map.put("C_GET_MIN", 7);
        map.put("REWRITE", 8);
        map.put("KILL", 9);

        //--Internal communication--
        map.put("CURR_PROC", 10);
        map.put("CURR_FREE", 11);
        map.put("KILL_SELF", 12);
        map.put("VOID_DONE", 13);
        map.put("N_GET_MAX", 14);
        map.put("N_GET_MIN", 15);
        map.put("INT_DONE", 16);
        map.put("PAIR_DONE", 17);


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
        r_map.put(1 << 12, "KILL_SELF");

    }

    public static HashMap<Integer, String> get_r_map() {
        return r_map;
    }

    private static final Set<Integer> noArgsOps = new HashSet<>();

    static {
        noArgsOps.add(0);
        noArgsOps.add(1);
        noArgsOps.add(2);
        noArgsOps.add(3);
        noArgsOps.add(6);
        noArgsOps.add(7);
        noArgsOps.add(9);
        noArgsOps.add(12);
        noArgsOps.add(13);
    }
    public static Set<Integer> getNoArgsOps() {
        return noArgsOps;
    }

    private static final Set<Integer> oneArgOps = new HashSet<>();
    static {
        oneArgOps.add(4);
        oneArgOps.add(5);
        oneArgOps.add(14);
        oneArgOps.add(15);
        oneArgOps.add(16);
    }

    public static Set<Integer> getOneArgOps(){
        return oneArgOps;
    }

    private static final Set<Integer> twoArgOps = new HashSet<>();
    static {
        twoArgOps.add(3);
        twoArgOps.add(8);
        twoArgOps.add(17);
    }

    public static Set<Integer> getTwoArgOps(){
        return twoArgOps;
    }



}
