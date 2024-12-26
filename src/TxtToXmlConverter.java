import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TxtToXmlConverter {

    public static void main(String[] args) {
        String inputFile = "src\\scotmap.txt";
        String outputFile = "output.xml";

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Connections");
            doc.appendChild(rootElement);

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    int fromId = Integer.parseInt(parts[0]);
                    int toId = Integer.parseInt(parts[1]);
                    String type = parts[2];

                    // Connection element with attributes
                    Element connection = doc.createElement("connection");
                    connection.setAttribute("from_id", String.valueOf(fromId));
                    connection.setAttribute("to_id", String.valueOf(toId));
                    connection.setAttribute("type", type);
                    rootElement.appendChild(connection);
                }
            }

            // Write the content into XML file with indentation
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(outputFile));

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (IOException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
