
package com.todolist.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface DbsnLibrary extends Library {        
    DbsnLibrary INSTANCE = (DbsnLibrary) Native.loadLibrary("lib/dbsn", DbsnLibrary.class);

    int createDBSN    (String fil_name);
    int openDBSN      (String fil_name);
    int packDBSN      (String fil_name);
    int flushDBSN     (int dbhadr); 
    int closeDBSN     (int dbhadr);
    int setNom        (int dbhadr, int new_nom);
    int countFragm    (int dbhadr);
    int getFragm      (int dbhadr, byte[] fragm, int bufsize);
    int setFragm      (int dbhadr, String fragm);
    int addFragm      (int dbhadr, String fragm);
    int delFragm      (int dbhadr);
    int cutFragm      (int dbhadr);
}