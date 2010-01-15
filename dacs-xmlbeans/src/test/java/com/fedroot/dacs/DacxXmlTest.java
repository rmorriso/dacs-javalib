/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fedroot.dacs;

import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;
import junit.framework.TestCase;

/**
 *
 * @author rmorriso
 */
public class DacxXmlTest extends TestCase {

    public void testMarshall() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("com.fedroot.dacs");
        ObjectFactory factory = new ObjectFactory();

        DacsListJurisdictions jurisdictionList = factory.createDacsListJurisdictions();
        jurisdictionList.setDomain("fedroot.com");
        DacsJurisdiction jurisdiction = factory.createDacsJurisdiction();
        jurisdictionList.getJurisdiction().add(jurisdiction);

        jurisdiction.setJname("METALOGIC");

        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(jurisdictionList, System.out);
    }

    public void testUnmarshall() throws JAXBException {
        File jurisdictionsFile = new File("/Users/rmorriso/NetBeansProjects/dacs-xmlbeans/src/main/xsd/dacs_list_jurisdictions.xml");
        JAXBContext jc = JAXBContext.newInstance("com.fedroot.dacs");
        // use the following to validate against the schema
//        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        Schema dacsSchema = new Schema(new File("dacs_list_jurisdictions.xsd"));
        Unmarshaller um = jc.createUnmarshaller();
        DacsListJurisdictions jurisdictionsList = (DacsListJurisdictions)um.unmarshal(jurisdictionsFile);

        assertEquals(14, jurisdictionsList.getJurisdiction().size());

    }
}
