package com.gathin.tvhome;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Guards key and printf-placeholder parity for every supported Android resource locale. */
public class LocalizationResourceParityTest {
    private static final Pattern FORMAT = Pattern.compile("%(\\d+\\$)?[sdif]");

    @Test
    public void supportedLocalesHaveIdenticalKeysAndFormatArguments() throws Exception {
        Path root = Path.of("src/main/res");
        if (!Files.isDirectory(root)) root = Path.of("launcher/app/src/main/res");

        Map<String, String> defaults = readStrings(root.resolve("values/strings.xml"));
        Map<String, String> indonesian = readStrings(root.resolve("values-id/strings.xml"));
        Map<String, String> english = readStrings(root.resolve("values-en/strings.xml"));

        assertEquals("Indonesian resource keys must match the default set", defaults.keySet(), indonesian.keySet());
        assertEquals("English resource keys must match the default set", defaults.keySet(), english.keySet());

        for (String key : defaults.keySet()) {
            String expected = placeholders(defaults.get(key));
            assertEquals("Placeholder mismatch for Indonesian key " + key, expected, placeholders(indonesian.get(key)));
            assertEquals("Placeholder mismatch for English key " + key, expected, placeholders(english.get(key)));
        }
        assertTrue("At least the launcher base string set must be present", defaults.size() >= 44);
    }

    private static Map<String, String> readStrings(Path file) throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file.toFile());
        NodeList nodes = document.getDocumentElement().getElementsByTagName("string");
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            String key = element.getAttribute("name");
            assertTrue("Duplicate string resource key: " + key, !result.containsKey(key));
            result.put(key, element.getTextContent());
        }
        return result;
    }

    private static java.util.List<String> placeholders(String value) {
        java.util.List<String> result = new ArrayList<>();
        Matcher matcher = FORMAT.matcher(value);
        while (matcher.find()) result.add(matcher.group());
        Collections.sort(result);
        return result;
    }
}
