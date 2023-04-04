package com.example.cotransfers.model;

import java.util.Comparator;

public class TransferComparator implements Comparator<Transfer> {
    @Override
    public int compare(Transfer t1, Transfer t2) {
        if (t1.getId() > t2.getId()) {
            return -1;
        } else if (t1.getId() < t2.getId()){
            return 1;
        } else {
            return 0;
        }
    }
}
