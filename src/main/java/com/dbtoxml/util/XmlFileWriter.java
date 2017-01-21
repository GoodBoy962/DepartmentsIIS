package com.dbtoxml.util;

import com.dbtoxml.model.Department;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author aleksandrpliskin on 20.01.17.
 */
public class XmlFileWriter {

    private static final Logger LOGGER = Logger.getLogger(XmlFileWriter.class);
    private static final String XML_POSTFIX = ".xml";

    public static void write(final String fileName, final List<Department> departments) {
        try {
            Document doc = generateDocument(departments);
            saveToXml(doc, fileName);
        } catch (TransformerException | ParserConfigurationException e) {
            LOGGER.error("Problems with parsing", e);
        }
    }

    private static Document generateDocument(List<Department> departments)
            throws ParserConfigurationException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("departments");
        doc.appendChild(root);
        departments.forEach(department -> {
            Element dep = doc.createElement("department");
            root.appendChild(dep);
            dep.setAttribute("depCode", department.getDepCode());
            dep.setAttribute("depJob", department.getDepJob());
            dep.setAttribute("description", department.getDescription());
        });
        return doc;
    }

    private static void saveToXml(final Document doc, final String fileName)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        File file = new File(fileName + XML_POSTFIX);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
        LOGGER.debug("File " + fileName + " was saved!");
    }

}
