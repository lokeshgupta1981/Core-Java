package com.howtodoinjava.jaxb;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class XmlTypeAdapterOverInterface {

  public static void main(String[] args) throws JAXBException {
    JAXBContext jaxbContext = JAXBContext.newInstance(BatchJob.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    StringWriter stringWriter = new StringWriter();
    jaxbMarshaller.marshal(new BatchJob(LocalDateTime.now()), stringWriter);
    String XML = stringWriter.toString();
    System.out.println(XML);

    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    BatchJob newBatchJob = (BatchJob) jaxbUnmarshaller.unmarshal(new StringReader(XML));
    System.out.println(newBatchJob);
  }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "BatchJob")
@XmlAccessorType(XmlAccessType.FIELD)
class BatchJob {

  @XmlJavaTypeAdapter(TemporalAdapter.class)
  Temporal startTime;
}

class TemporalAdapter extends XmlAdapter<String, Temporal> {

  @Override
  public Temporal unmarshal(String v) throws Exception {
    return LocalDateTime.parse(v);
  }

  @Override
  public String marshal(Temporal v) throws Exception {
    return v.toString();
  }
}



