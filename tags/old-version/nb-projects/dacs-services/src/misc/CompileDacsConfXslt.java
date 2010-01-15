package misc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;

/*
 * TODO: make ant task to generate classes and from xsl/*.xslt and
 * create library lib/xsl.jar
 */

/*
 * Run this file to generate class com.fedroot.dacs.xsl.DacsConfXslt
 * from xsl/dacs_conf.xslt.
 *
 * NOTE - DO NOT PUT THE COMPILE CLASS IN THE SAME PACKAGE AS THE
 * XSLTC CLASSES THAT IT GENERATES
 */
public class CompileDacsConfXslt {
    
    // Public constants for attributes supported by the XSLTC TransformerFactory.
    public final static String TRANSLET_NAME = "translet-name";
    public final static String DESTINATION_DIRECTORY = "destination-directory";
    public final static String PACKAGE_NAME = "package-name";
    public final static String JAR_NAME = "jar-name";
    public final static String GENERATE_TRANSLET = "generate-translet";
    public final static String AUTO_TRANSLET = "auto-translet";
    public final static String USE_CLASSPATH = "use-classpath";
    public final static String DEBUG = "debug";
    public final static String ENABLE_INLINING = "enable-inlining";
    public final static String INDENT_NUMBER = "indent-number";
    
    public static void main(String[] args) throws Exception{
        // System.setProperty( "javax.xml.transform.TransformerFactory", "org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
        // System.out.println(System.getProperty("javax.xml.transform.TransformerFactory"));
        
        File xmlfile = new File("H:/projects/dacs-javalib/projects/dacs-services/xsl/dacs_conf_test.xml");

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(xmlfile);
        
        File stylefile = new File("H:/projects/dacs-javalib/projects/dacs-services/xsl/dacs_conf.xslt");
        // Get an input stream for the XSL stylesheet
	StreamSource stylesheet = new StreamSource(stylefile);
        
        TransformerFactory transfact = TransformerFactory.newInstance();
        transfact.setAttribute(TRANSLET_NAME, "DacsConfXslt");
        transfact.setAttribute(GENERATE_TRANSLET, true);
        transfact.setAttribute(PACKAGE_NAME,"com.fedroot.dacs.xsl");
        transfact.setAttribute(DESTINATION_DIRECTORY, "H:/projects/dacs-javalib/projects/dacs-services/build/classes");
        // transform using stylesheet
        Templates templates = transfact.newTemplates(stylesheet);
        Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(stylefile));
        // Transformer transformer = templates.newTransformer();

        ByteArrayOutputStream outstrm = new ByteArrayOutputStream();
        // StreamResult outdoc = new StreamResult(System.out);
        StreamResult outdoc = new StreamResult(outstrm);
        StreamSource xmlsource = new StreamSource(xmlfile);
        transformer.transform(xmlsource, outdoc);
        transformer.reset();
        System.out.println(outstrm.toString());
    }
}