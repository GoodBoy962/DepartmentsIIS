package com.dbtoxml;

import com.dbtoxml.service.XmlWriterService;

import java.io.IOException;

public class XmlWriter {

    public static void main(String[] args) throws IOException {
        String fileName = args[0];
        new XmlWriterService().write(fileName);
    }
}
