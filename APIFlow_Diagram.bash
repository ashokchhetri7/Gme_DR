Postman / Mobile APk
   |
   | GET /api/v1/mobile/Zero/ping
   v
Spring Cloud Gateway (localhost:8080)
   |
   | GET /api/v1/mobile/Zero/ping → https://gmeuat.gmeremit.com:5022/api/v1/mobile/Zero/ping
   v
Target Server (https://gmeuat.gmeremit.com:5022)
   |
   | Response
   v
Spring Cloud Gateway
   |
   | Response
   v
Postman / Mobile APk
