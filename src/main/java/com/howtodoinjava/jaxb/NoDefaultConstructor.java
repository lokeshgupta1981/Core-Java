package com.howtodoinjava.jaxb;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.StringReader;
import java.io.StringWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NoDefaultConstructor {

  public static void main(String[] args) throws JAXBException {
    Address address = new Address("Line 1", "Line 2", "New Delhi", "India", new PinCode("110075"));
    JAXBContext jaxbContext = JAXBContext.newInstance(Address.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    StringWriter stringWriter = new StringWriter();
    jaxbMarshaller.marshal(address, stringWriter);
    String XML = stringWriter.toString();
    System.out.println(XML);

    String xmlAddress = """
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Address>
        	<line1>Line 1</line1>
        	<line2>Line 2</line2>
        	<city>New Delhi</city>
        	<country>India</country>
        	<pinCode>
        		<code>110075</code>
        	</pinCode>
        </Address>
        """;

    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    Address newAddress = (Address) jaxbUnmarshaller.unmarshal(new StringReader(xmlAddress));
    System.out.println(newAddress);
  }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Address")
@XmlAccessorType(XmlAccessType.FIELD)
class Address {
  String line1;
  String line2;
  String city;
  String country;
  //@XmlTransient
  @XmlJavaTypeAdapter(PinCodeAdapter.class)
  PinCode pinCode;
}
@Data
class PinCode {
  String code;

  public PinCode(String code){
    this.code = code;
  }
}

@Data
class XmlPinCode {

  String code;
  public XmlPinCode(){
  }

  public XmlPinCode(String code){
    this.code = code;
  }
}

class PinCodeAdapter extends XmlAdapter<XmlPinCode, PinCode> {

  @Override
  public PinCode unmarshal(XmlPinCode v) throws Exception {
    return new PinCode(v.getCode());
  }

  @Override
  public XmlPinCode marshal(PinCode v) throws Exception {
    return new XmlPinCode(v.getCode());
  }
}
