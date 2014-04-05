
package com.todolist.dao;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface DbsnDriver extends Library {        
    DbsnDriver INSTANCE = (DbsnDriver) Native.loadLibrary("lib/dbsn", DbsnDriver.class);

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

    int substCount    (String pc);
    int getSubst      (String pc, byte[] pcbuf, int buf_sze, int nom_sub);
    int addSubst      (String pc, byte[] pcres, int res_size, String pcsubst);
    int insSubst      (String pc, byte[] pcres, int res_size, int nom_sub, String pcsubst);
}