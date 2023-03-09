package com.howtodoinjava.xml.stax;

import com.howtodoinjava.xml.model.Employee;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ReadXmlWithCursor {

  public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
    //All read employees objects will be added to this list
    List<Employee> employeeList = new ArrayList<>();

    //Create Employee object. It will get all the data using setter methods.
    //And at last, it will stored in above 'employeeList'
    Employee employee = null;

    File file = new File("employees.xml");
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLStreamReader streamReader = factory.createXMLStreamReader(new FileReader(file));

    while (streamReader.hasNext()) {
      //Move to next event
      streamReader.next();

      //Check if its 'START_ELEMENT'
      if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
        //employee tag - opened
        if (streamReader.getLocalName().equalsIgnoreCase("employee")) {

          //Create new employee object asap tag is open
          employee = new Employee();

          //Read attributes within employee tag
          if (streamReader.getAttributeCount() > 0) {
            String id = streamReader.getAttributeValue(null, "id");
            employee.setId(Integer.valueOf(id));
          }
        }

        //Read name data
        if (streamReader.getLocalName().equalsIgnoreCase("name")) {
          employee.setFirstName(streamReader.getElementText());
        }

        //Read title data
        if (streamReader.getLocalName().equalsIgnoreCase("title")) {
          employee.setLastName(streamReader.getElementText());
        }
      }

      //If employee tag is closed then add the employee object to list
      if (streamReader.getEventType() == XMLStreamReader.END_ELEMENT) {
        if (streamReader.getLocalName().equalsIgnoreCase("employee")) {
          employeeList.add(employee);
        }
      }
    }
    //Verify read data
    System.out.println(employeeList);
  }
}