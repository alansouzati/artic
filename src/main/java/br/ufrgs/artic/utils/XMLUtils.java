package br.ufrgs.artic.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to perform common XML operations
 */
public class XMLUtils {

    /**
     * This method extracts the list of elements from the @param element that contains the @param tagName.
     *
     * @param tagName the tag name used to get the list of elements
     * @param element the dom element to look for the tags with the given name
     * @return list of elements that have the @param tagName
     */
    public static List<Element> getElementsByTagName(String tagName, Element element) {
        List<Element> elements = new ArrayList<>();

        NodeList nodeList = element.getElementsByTagName(tagName);

        if (nodeList != null && nodeList.getLength() > 0) {

            for (int index = 0; index < nodeList.getLength(); index++) {
                Node currentNode = nodeList.item(index);

                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    elements.add((Element) currentNode);
                }
            }
        }
        return elements;
    }
}
