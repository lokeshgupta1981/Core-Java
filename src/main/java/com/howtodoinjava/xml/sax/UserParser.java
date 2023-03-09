package com.howtodoinjava.xml.sax;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class UserParser {

  public ArrayList parseXml(InputStream in) {
    //Create an empty link of users initially
    ArrayList<User> users = new ArrayList<>();
    try {
      //Create default handler instance
      UserParserHandler handler = new UserParserHandler();

      //Create parser from factory
      XMLReader reader = XMLReaderFactory.createXMLReader();

      //Register handler with parser
      reader.setContentHandler(handler);

      //Create an input source from the XML input stream
      InputSource source = new InputSource(in);

      //parse the document
      reader.parse(source);

      //populate the parsed users list in above created empty list; You can return from here also.
      users = handler.getUsers();

    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {

    }
    return users;
  }
}
