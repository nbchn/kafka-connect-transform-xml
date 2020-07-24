package com.github.jcustenborder.kafka.connect.transform.xml;

import com.google.common.collect.ImmutableMap;
import java.nio.file.Files;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.data.Schema;

import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class FromXMLWithXSLTest {

    Transformation<SourceRecord> transform;

    @BeforeEach
    public void before() {
        String xsdPath = "file:src/test/resources/com/github/jcustenborder/kafka/connect/transform/xml/cd_catalog_transformed_schema.xsd";
        String xsltPath = "file:src/test/resources/com/github/jcustenborder/kafka/connect/transform/xml/cd_catalog_transformer.xsl";
        Map<String, Object> settings = new HashMap<>();
        settings.put("schema.path", xsdPath);
        settings.put("xslt.transformer.path", xsltPath);
        this.transform = new FromXml.Value<>();
        this.transform.configure(settings);
    }

    @AfterEach
    public void after() {
        this.transform.close();
    }

    @Test
    public void apply() throws Exception {
        String inputTransformerPath = "src/test/resources/com/github/jcustenborder/kafka/connect/transform/xml/cd_catalog.xml";

        final String payload = new String(Files.readAllBytes(Paths.get(inputTransformerPath)));
        SourceRecord target = transform.apply(buildSourceRecord(payload));

        System.out.println(target.toString());
    }

    private static SourceRecord buildSourceRecord(String payload) {
        return new SourceRecord(Collections.emptyMap(), Collections.emptyMap(), "sample",
                Schema.OPTIONAL_STRING_SCHEMA, payload);
    }

}
