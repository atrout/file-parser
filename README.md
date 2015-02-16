# File-parser
File-parser is a simple service that accepts a text file and returns an overall word count and a break-down of counts for each individual word in the file.

### Description
GET: describe api, 
      POST: submit file, response: parsed file information. 
      
### Dependancies
Play framework from typesafe

### Setup

### Usage
curl -X GET --header "Accept: application/json" http://localhost:9000/
curl -X POST -F textFile=@wordfile.txt --header "Accept: application/json" http://localhost:9000/
      
### Credits
JSON based on JSend: http://labs.omniti.com/labs/jsend
test for upload: https://github.com/G-Node/GCA-Web/blob/master/test/controller/FigureCtrlTest.scala
