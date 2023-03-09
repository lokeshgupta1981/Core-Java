package com.howtodoinjava.xml.stax;

import com.howtodoinjava.xml.model.Employee;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class ReadXmlWithIterator {

  public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
    File file = new File("employees.xml");

    // Instance of the class which helps on reading tags
    XMLInputFactory factory = XMLInputFactory.newInstance();

    // Initializing the handler to access the tags in the XML file
    XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(file));

    //All read employees objects will be added to this list
    List<Employee> employeeList = new ArrayList<>();

    //Create Employee object. It will get all the data using setter methods.
    //And at last, it will stored in above 'employeeList'
    Employee employee = null;

    // Checking the availability of the next tag
    while (eventReader.hasNext()) {
      XMLEvent xmlEvent = eventReader.nextEvent();

      if (xmlEvent.isStartElement()) {
        StartElement startElement = xmlEvent.asStartElement();

        //As soo as employee tag is opened, create new Employee object
        if ("employee".equalsIgnoreCase(startElement.getName().getLocalPart())) {
          employee = new Employee();
        }

        //Read all attributes when start tag is being read
        @SuppressWarnings("unchecked")
        Iterator<Attribute> iterator = startElement.getAttributes();

        while (iterator.hasNext()) {
          Attribute attribute = iterator.next();
          QName name = attribute.getName();
          if ("id".equalsIgnoreCase(name.getLocalPart())) {
            employee.setId(Integer.valueOf(attribute.getValue()));
          }
        }

        //Now everytime content tags are found;
        //Move the iterator and read data
        switch (startElement.getName().getLocalPart()) {
          case "name":
            Characters nameDataEvent = (Characters) eventReader.nextEvent();
            employee.setFirstName(nameDataEvent.getData());
            break;

          case "title":
            Characters titleDataEvent = (Characters) eventReader.nextEvent();
            employee.setFirstName(titleDataEvent.getData());
            break;
        }
      }

      if (xmlEvent.isEndElement()) {
        EndElement endElement = xmlEvent.asEndElement();

        //If employee tag is closed then add the employee object to list;
        //and be ready to read next employee data
        if ("employee".equalsIgnoreCase(endElement.getName().getLocalPart())) {
          employeeList.add(employee);
        }
      }
    }

    System.out.println(employeeList); //Verify read data
  }
}
