package com.dbtoxml;

import com.dbtoxml.service.XmlWriterService;

import java.io.IOException;
import java.util.Scanner;

public class XmlWriter {

    public static void main(String[] args) throws IOException {
//        String fileName = args[0];
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();
        new XmlWriterService().write(fileName);
    }
}
