export SHIFTLEFT_SEC_BLOCK_ATTACKS=$1
echo "Running with blocking mode: $1"
sl run -- java -jar target/hello-shiftleft-0.0.1.jar &
sleep 60
curl http://localhost:8081/customers/1
curl http://localhost:8081/customers/
curl http://localhost:8081/patients
curl http://localhost:8081/account
curl http://localhost:8081/account/1
http://localhost:8081/search/user?foo=new java.lang.ProcessBuilder({'/bin/bash','-c','echo $SHIFTLEFT_ORG_TOKEN>/tmp/hacked'}).start()
sleep 20
