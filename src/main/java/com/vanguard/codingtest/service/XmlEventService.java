package com.vanguard.codingtest.service;

import com.vanguard.codingtest.model.Event;
import com.vanguard.codingtest.service.interfaces.IEventService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Service
public class XmlEventService implements IEventService {
    private final String FILE_LOCATION = "src/main/resources/event XML/event%s.xml";
    private final String ROOT_PATH = "/requestConfirmation/trade/varianceOptionTransactionSupplement/%s";

    @Override
    public List<Event> getEvents() {
        var list = new ArrayList<Event>();
        var factory = DocumentBuilderFactory.newInstance();
        var xPath = XPathFactory.newInstance().newXPath();

        BiFunction<Document, String, Node> transformer = (doc, path) -> {
            try {
                return (Node) xPath.compile(path).evaluate(doc, XPathConstants.NODE);
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        };

        try {
            var builder = factory.newDocumentBuilder();
            var fileExists = true;
            var fileNo = 0;
            while (fileExists) {
                var file = new File(FILE_LOCATION.formatted(fileNo++));
                fileExists = file.exists();
                if (fileExists) {
                    var reader = new FileReader(file);
                    var input = new InputSource(reader);
                    var document = builder.parse(input);
                    list.add(documentToEvent(document, transformer));
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private Event documentToEvent(Document document, BiFunction<Document, String, Node> extractor) {
        return new Event(
            extractor.apply(document, ROOT_PATH.formatted("buyerPartyReference")).getAttributes().getNamedItem("href").getNodeValue(),
            extractor.apply(document, ROOT_PATH.formatted("sellerPartyReference")).getAttributes().getNamedItem("href").getNodeValue(),
            extractor.apply(document, ROOT_PATH.formatted("equityPremium/paymentAmount/currency")).getFirstChild().getNodeValue(),
            Double.parseDouble(extractor.apply(document, ROOT_PATH.formatted("equityPremium/paymentAmount/amount")).getFirstChild().getNodeValue())
        );
    }
}
