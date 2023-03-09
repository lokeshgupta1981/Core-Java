package com.howtodoinjava.xml;

import com.howtodoinjava.xml.model.Employee;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomParserExample {

  public static void main(String[] args) throws Exception {
    Document document = readXMLDocumentFromFile("c:/temp/employees.xml");
    List<Employee> employees = parseXmlToPOJO("c:/temp/employees.xml");

    //Verify XML Content

    //Here comes the root node
    Element root = document.getDocumentElement();
    System.out.println(root.getNodeName());

    //Get all employees
    NodeList nList = document.getElementsByTagName("employee");
    System.out.println("============================");

    for (int temp = 0; temp < nList.getLength(); temp++) {
      Node node = nList.item(temp);
      System.out.println("");    //Just a separator
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        //Print each employee's detail
        Element eElement = (Element) node;
        System.out.println("Employee id : " + eElement.getAttribute("id"));
        System.out.println(
            "First Name : " + eElement.getElementsByTagName("firstName").item(0).getTextContent());
        System.out.println(
            "Last Name : " + eElement.getElementsByTagName("lastName").item(0).getTextContent());
        System.out.println(
            "Location : " + eElement.getElementsByTagName("location").item(0).getTextContent());
      }
    }
  }

  public static Document readXMLDocumentFromFile(String fileNameWithPath) throws Exception {

    //Get Document Builder
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();

    //Build Document
    Document document = builder.parse(new File(fileNameWithPath));

    //Normalize the XML Structure; It's just too important !!
    document.getDocumentElement().normalize();

    return document;
  }

  public static List<Employee> parseXmlToPOJO(String fileName) throws Exception {

    List<Employee> employees = new ArrayList<Employee>();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(new File(fileName));
    document.getDocumentElement().normalize();

    NodeList nList = document.getElementsByTagName("employee");

    for (int temp = 0; temp < nList.getLength(); temp++) {

      Node node = nList.item(temp);
      if (node.getNodeType() == Node.ELEMENT_NODE) {

        Element eElement = (Element) node;
        Employee employee = new Employee();
        employee.setId(Integer.parseInt(eElement.getAttribute("id")));
        employee.setFirstName(eElement.getElementsByTagName("firstName").item(0).getTextContent());
        employee.setLastName(eElement.getElementsByTagName("lastName").item(0).getTextContent());
        employee.setLocation(eElement.getElementsByTagName("location").item(0).getTextContent());

        //Add Employee to list
        employees.add(employee);
      }
    }
    return employees;
  }

  private static void visitChildNodes(NodeList nList) {

    for (int temp = 0; temp < nList.getLength(); temp++) {

      Node node = nList.item(temp);
      if (node.getNodeType() == Node.ELEMENT_NODE) {

        System.out.println("Node Name = " + node.getNodeName() + "; Value = " + node.getTextContent());
        //Check all attributes
        if (node.hasAttributes()) {

          // get attributes names and values
          NamedNodeMap nodeMap = node.getAttributes();
          for (int i = 0; i < nodeMap.getLength(); i++) {

            Node tempNode = nodeMap.item(i);
            System.out.println("Attr name : " + tempNode.getNodeName() + "; Value = " + tempNode.getNodeValue());
          }
          if (node.hasChildNodes()) {
            //We got more children; Let's visit them as well
            visitChildNodes(node.getChildNodes());
          }
        }
      }
    }
  }

}
