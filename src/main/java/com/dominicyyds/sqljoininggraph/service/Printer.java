package com.dominicyyds.sqljoininggraph.service;

import com.dominicyyds.sqljoininggraph.entity.JoinEntry;

import java.util.Collection;

public interface Printer {

    void printJoinEntries(Collection<JoinEntry> joinEntries);

}
