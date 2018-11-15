sudo apt-get update
sudo apt-get install -y nginx
sudo apt-get install -y default-jdk
sudo apt-get install -y maven
sleep 5
sudo ufw allow 'Nginx Full'
sudo ufw --force enable
sudo systemctl reload nginx
curl https://www.shiftleft.io/download/sl-latest-linux-x64.tar.gz > /tmp/sl.tar.gz && sudo tar -C /usr/local/bin -xzf /tmp/sl.tar.gz
sleep 5
sl auth --org "fcf5e7a2-973f-4cff-b35b-62163909124d" --token "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTk3Nzc3MDksImlzcyI6IlNoa$
git clone https://github.com/alokshukla1978/HelloShiftLeft.git
sleep 5
cd HelloShiftLeft
mvn clean package
sudo mv /etc/nginx/sites-available/default /etc/nginx/sites-available/default.old
sudo cat <<EOF > /etc/nginx/sites-available/default
server {
    listen 80;
    server_name _;
    location / {
        proxy_pass http://127.0.0.1:8081;
        proxy_http_version 1.1;
        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host \$host;
        proxy_cache_bypass \$http_upgrade;
     }
}
EOF
sudo systemctl reload nginx
sleep 5
java -jar target/hello-shiftleft-0.0.1.jar &
# sl run --analyze target/hello-shiftleft-0.0.1.jar --app hello-shiftleft-CD -- java -jar target/hello-shiftleft-0$
