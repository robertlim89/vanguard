package com.vanguard.codingtest.service;

import com.vanguard.codingtest.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(
    properties = {
        "application.event.location=testLocation"
    })
class XmlEventServiceTest {
    @Test
    public void test_getEvents() throws IOException, SAXException {
        BiFunction<Document, String, Node> transformer = (BiFunction<Document, String, Node>) mock(BiFunction.class);

        var buyerNode = mock(Node.class, RETURNS_DEEP_STUBS);
        when(buyerNode.getAttributes().getNamedItem("href").getNodeValue()).thenReturn("Buyer");
        when(transformer.apply(any(Document.class), endsWith("buyerPartyReference"))).thenReturn(buyerNode);

        var sellerNode = mock(Node.class, RETURNS_DEEP_STUBS);
        when(sellerNode.getAttributes().getNamedItem("href").getNodeValue()).thenReturn("Seller");
        when(transformer.apply(any(Document.class), endsWith("sellerPartyReference"))).thenReturn(sellerNode);

        var currencyNode = mock(Node.class, RETURNS_DEEP_STUBS);
        when(currencyNode.getFirstChild().getNodeValue()).thenReturn("AUD");
        when(transformer.apply(any(Document.class), endsWith("currency"))).thenReturn(currencyNode);

        var amountNode = mock(Node.class, RETURNS_DEEP_STUBS);
        when(amountNode.getFirstChild().getNodeValue()).thenReturn("12.3");
        when(transformer.apply(any(Document.class), endsWith("amount"))).thenReturn(amountNode);

        var documentBuilder = mock(DocumentBuilder.class);
        when(documentBuilder.parse(any(InputSource.class))).thenReturn(mock(Document.class));

        var sut = new XmlEventService();
        var events = new ArrayList<Event>();
        sut.processFile(documentBuilder, transformer, mock(InputSource.class), events);

        assertEquals(1, events.size());
        assertThat(events.getFirst())
                .extracting("sellerParty", "buyerParty", "premiumCurrency", "premiumAmount", "id")
                .containsExactly("Seller", "Buyer", "AUD", 12.3D, null);

        verify(transformer).apply(any(Document.class), eq("/requestConfirmation/trade/varianceOptionTransactionSupplement/sellerPartyReference"));
        verify(transformer).apply(any(Document.class), eq("/requestConfirmation/trade/varianceOptionTransactionSupplement/buyerPartyReference"));
        verify(transformer).apply(any(Document.class), eq("/requestConfirmation/trade/varianceOptionTransactionSupplement/equityPremium/paymentAmount/currency"));
        verify(transformer).apply(any(Document.class), eq("/requestConfirmation/trade/varianceOptionTransactionSupplement/equityPremium/paymentAmount/amount"));
    }

}