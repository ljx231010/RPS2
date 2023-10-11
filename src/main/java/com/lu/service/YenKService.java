package com.lu.service;

import java.util.List;

public interface YenKService {
    List<String> yunK(String startCompound1, String endCompound1, int k);

    int[][] createNetwork();
}
