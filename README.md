Pra executar a API basta ter o Docker instalado

Entao rodar o comando:

docker-compose up -d

A imagem será criada, as dependencias instaladas, o banco ficará disponivel, o build será criado e a API será executada

assim que as imagens estiverem up, podemos acessar  swagger da API

http://localhost:8080/swagger-ui/index.html


Exemplo de request para o endpoint POST

{
    "merchantCode": "123e4567-e89b-12d3-a456-426614174000",
    "amount": 100.00,
    "description": "Mercearia do Eliton",
    "paymentMethod": "debit",
    "cardNumber": "0000 0000 0000 0000",
    "cardHolderName": "ELITON TESTE",
    "cardExpirationDate": "01/29",
    "cardCvv": "123"
}

As demais requests GET o proprio swagger terá as informações.

Para conferir os dados do banco, basta usar um client e conectar ao endereço:

mongodb://localhost:27017

Eu aconselho o MongoDB Compass.