package com.howtodoinjava.jaxb;

import com.howtodoinjava.xml.model.Employee;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;

public class ReadWriteXML {

  public static void main(String[] args) {

    // Convert XML String to Java Object

    String xmlString = "<employee>" +
        "    <department>" +
        "        <id>101</id>" +
        "        <name>IT</name>" +
        "    </department>" +
        "    <location>India</location>" +
        "    <firstName>Lokesh</firstName>" +
        "    <id>1</id>" +
        "    <lastName>Gupta</lastName>" +
        "</employee>";

    JAXBContext jaxbContext;
    try {
      jaxbContext = JAXBContext.newInstance(Employee.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      Employee employee = (Employee) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));
      System.out.println(employee);
    } catch (JAXBException e) {
      e.printStackTrace();
    }

  }
}
