O Objetivo dessa POC era a de realizar um mercado autonomo capaz de integrar com o PicPay. Através desse frontend os usuários poderiam cadastrar um produto para a loja e então através de um menu selecionar o item para compra.
Ao selecionar o valor referente a compra é enviado a um dispositivo IOT, que é capaz de gerar o QRCode a partir da URL gerada.
Então com o aplicativo basta somente realizar o pagamento.

Para a POC foram criados 

um case para o dispositivo
![alt text](./case.png)

O dispositivo para mostrar o qrCode
![alt text](./device.jpg)

e um microserviço escrito em C para o código do embarcado