# File-parser
File-parser is a simple service that accepts a text file and returns an overall word count and a break-down of counts for each individual word in the file.

### Description
The file-parser application can either be used through a web interface or programmatically. The supported actions are 'GET' and 'POST'. 

If http headers support returning html, when the application receives a GET request it will return a webpage with a form that allows the user to upload a file to be parsed. When a file is POSTed from the web interface the result of parsing the file will be displayed in an html page.

If http headers support JSON, a GET request will return the application status in JSON format:
```
      { "status":"success",
        "service":"file upload and parse"
      }
```
POSTing a file to the service with JSON accept headers will result in file data returned in JSON format:
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

Play version 2.3.7 also requires sbt version 0.13.5 or later.

### Setup
To run the application locally:
      * install [JDK 6 or later](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
      * install sbt 0.13.5 or later (optional)
      * clone this repository
      * in a console window, from the root directory of the repository type **sbt run**
      * alternatively, if sbt is not installed, the application can be run by typing **./activator run** from the root directory
      

### Usage
curl -X GET --header "Accept: application/json" http://localhost:9000/
curl -X POST -F textFile=@wordfile.txt --header "Accept: application/json" http://localhost:9000/
      
### Credits
JSON based on JSend: http://labs.omniti.com/labs/jsend
test for upload: https://github.com/G-Node/GCA-Web/blob/master/test/controller/FigureCtrlTest.scala
