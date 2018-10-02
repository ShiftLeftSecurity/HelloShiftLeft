sl run -- java -jar target/hello-shiftleft-0.0.1.jar &
sleep 60
curl http://localhost:8081/customers/1
curl http://localhost:8081/customers/
curl http://localhost:8081/patients
curl http://localhost:8081/account
curl http://localhost:8081/account/1
sleep 20
