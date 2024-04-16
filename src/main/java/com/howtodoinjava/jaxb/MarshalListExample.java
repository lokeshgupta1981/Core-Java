package com.howtodoinjava.jaxb;

import com.howtodoinjava.xml.model.Employee;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

public class MarshalListExample {

  public static void main(String[] args) throws JAXBException {
    Employees employees = new Employees();

    //Add the employees in list
    Employee emp1 = new Employee(1, "Lokesh", "Gupta", null, null);
    Employee emp2 = new Employee(1, "John", "McLean", null, null);
    employees.getEmployees().add(emp1);
    employees.getEmployees().add(emp2);

    //Write to XML
    JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    //Marshal the employees list in console
    jaxbMarshaller.marshal(employees, System.out);

    //Marshal the employees list in file
    jaxbMarshaller.marshal(employees, new File("out.xml"));

    //unmarshal example
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

    //We had written this file in marshalling example
    Employees emps = (Employees) jaxbUnmarshaller.unmarshal(new File("out.xml"));

    for (Employee emp : emps.getEmployees()) {

      System.out.println(emp.getId());
      System.out.println(emp.getFirstName());
    }
  }
}

@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
class Employees {

  @XmlElement(name = "employee")
  private List<Employee> employees = new ArrayList<>();

  public List<Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(List<Employee> employees) {
    this.employees = employees;
  }
}
