# File-parser
File-parser is a simple service that accepts a text file and returns an overall word count and a break-down of counts for each individual word in the file.

### Description
The file-parser service can be used either through a web interface or programmatically. The supported actions are 'GET' and 'POST'. 

The service determines the format of the response based on the headers in the request. In response to a GET request with headers that indicate HTML support, the service will return an HTML formatted webpage with a file upload form. A POST request to the service with html headers (and a file to parse) will return the parsed file data as HTML.

If http headers support JSON, a GET request will return a JSON response:
```
      { "status":"success",
        "service":"file upload and parse"
      }
```

POSTing a file to the service with JSON headers will return the parsing data in JSON format:
```
      { "status":"success",
        "data": {
          "parsed_file": <filename>,
          "total_word_count": <total word count>,
          "word_occurrences": [ 
            {     "word":<word>,
                  "count":<word count>
            },
            ...
           ]
          }
      }
```
      
### Dependancies
File-parser is built on the [Play Framework](https://www.playframework.com/documentation/2.3.x/) from Typesafe. In order to run, [JDK 6 or later](http://www.oracle.com/technetwork/java/javase/downloads/index.html) is required. 

Play version 2.3.7 also requires [sbt](http://www.scala-sbt.org/) version 0.13.5 or later.

### Setup
To run the application locally:
* install [JDK 6 or later](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* (optional) install [sbt](http://www.scala-sbt.org/) 0.13.5 or later 
* clone this repository
* if sbt is installed: in a console window, from the root directory of the repository type **sbt run**
* if sbt is not installed: the application can be run by typing **./activator run** from the root directory

### Usage
To interact with the service via web interface, access http://localhost:9000/ in a web browser.

To interact with the service via cURL:

Make a GET request via curl with JSON headers:

`curl -X GET --header "Accept: application/json" http://localhost:9000/`

POST a file to be parsed via curl with JSON headers: 

`curl -X POST -F textFile=@wordfile.txt --header "Accept: application/json" http://localhost:9000/`
      
### Credits
* Format of JSON responses based on (JSend)[http://labs.omniti.com/labs/jsend]
* Code to translate Play AnyContentAsMultipartFormData to Array[Bytes] for the upload tests from (G-Node/GCA-Web)[ https://github.com/G-Node/GCA-Web/blob/master/test/controller/FigureCtrlTest.scala]
