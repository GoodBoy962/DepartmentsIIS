package com.dbtoxml;

import com.dbtoxml.service.SynchronizerService;

import java.io.IOException;

/**
 * @author aleksandrpliskin on 21.01.17.
 */
public class Synchronizer {

    public static void main(String[] args) throws IOException {
        String fileName = args[0];
        new SynchronizerService().synchronize(fileName);
    }

}
