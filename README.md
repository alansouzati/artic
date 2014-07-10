Artic: Metadata extractor from scientific documents
=====

Artic is a tool that automatically identifies the metadata information of a scientific paper. These information are: 
title, authors, emails, affiliations, conference name, conference date, conference location, conference year, ISBN and publisher.
This first version is strongly-dependent on OmniPage Version 18. That said, the expected input for Artic is an XML that has been generated by
OmniPage, and the expected output will be a JSON file with the metadata content.

Artic version 2.0 will target open-source tools in order to extract 
rich text information from PDFs (e.g. font size, font format, bold, italic, among others).

License: [MIT](http://www.opensource.org/licenses/mit-license.php)

Dependencies: 

*  [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html) 
*  [Omni Page Professional 18](http://www.nuance.com/for-business/by-product/omnipage/standard/index.htm)
*  [CRF++](https://code.google.com/p/crfpp/)

##How to execute##

After all the dependencies are installed and configured, execute the following steps to get the JSON file with metadata content.

* [Download](https://github.com/alansouzati/artic/releases/download/artic-1.0/artic-1.0.jar) Artic jar file
* Go to the folder where OmniPage XML files are located
* Run the following command:
```java
    java -jar artic-1.0.jar
```
* At the same location, a .json file will be created with the same name as the XML file of the given paper, which looks like this:
```json
    {
        "title": "Artic: Metadata Extractor tool",
        "authors": [
            {
                "name": "Alan Souza",
                "affiliation": "UFRGS",
                "email": "alansouzati@gmail.com"
            }
        ],
        "venues": [
            {
                "name": "DocEng",
                "publisher": "ACM",
                "date": "September 16-19",
                "year": "2014",
                "isbn": "978-1-4503-1994-2/13/04"
            }
        ]
    }
```