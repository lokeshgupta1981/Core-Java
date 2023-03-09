package com.howtodoinjava.xml.sax;

import java.util.ArrayList;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UserParserHandler extends DefaultHandler {

  //This is the list which shall be populated while parsing the XML.
  private ArrayList userList = new ArrayList();

  //As we read any XML element we will push that in this stack
  private Stack elementStack = new Stack();

  //As we complete one user block in XML, we will push the User instance in userList
  private Stack objectStack = new Stack();

  public void startDocument() throws SAXException {
    //System.out.println("start of the document   : ");
  }

  public void endDocument() throws SAXException {
    //System.out.println("end of the document document     : ");
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    //Push it in element stack
    this.elementStack.push(qName);

    //If this is start of 'user' element then prepare a new User instance and push it in object stack
    if ("user".equals(qName)) {
      //New User instance
      User user = new User();

      //Set all required attributes in any XML element here itself
      if (attributes != null && attributes.getLength() == 1) {
        user.setId(Integer.parseInt(attributes.getValue(0)));
      }
      this.objectStack.push(user);
    }
  }

  public void endElement(String uri, String localName, String qName) throws SAXException {
    //Remove last added  element
    this.elementStack.pop();

    //User instance has been constructed so pop it from object stack and push in userList
    if ("user".equals(qName)) {
      User object = (User) this.objectStack.pop();
      this.userList.add(object);
    }
  }

  /**
   * This will be called everytime parser encounter a value node
   */
  public void characters(char[] ch, int start, int length) throws SAXException {
    String value = new String(ch, start, length).trim();

    if (value.length() == 0) {
      return; // ignore white space
    }

    //handle the value based on to which element it belongs
    if ("firstName".equals(currentElement())) {
      User user = (User) this.objectStack.peek();
      user.setFirstName(value);
    } else if ("lastName".equals(currentElement())) {
      User user = (User) this.objectStack.peek();
      user.setLastName(value);
    }
  }

  /**
   * Utility method for getting the current element in processing
   */
  private String currentElement() {
    return (String) this.elementStack.peek();
  }

  //Accessor for userList object
  public ArrayList getUsers() {
    return userList;
  }
}
