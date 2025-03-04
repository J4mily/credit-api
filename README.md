#  🚀 Avaliação para Admissão de Desenvolvedores - Neurotech ✨

##  Olá!  Bem-vindo(a) ao meu projeto de avaliação para a Neurotech! 

Este projeto foi desenvolvido com o objetivo de criar uma API RESTful usando Java ☕ e Spring Boot , capaz de aplicar diferentes modalidades de crédito para clientes PF , seguindo critérios específicos. 

## ️ Como usar este projeto? ️

Para entender e testar este projeto, siga os passos abaixo: 

1.  **Clone o repositório:**

 [https://github.com/J4mily/credit-api](https://github.com/J4mily/credit-api)
    

3.  **Abra o projeto na sua IDE preferida:** ‍ (IntelliJ IDEA, Eclipse, etc.) 

4.  **Verifique as dependências:** Certifique-se de que todas as dependências (Maven ou Gradle) estão instaladas corretamente. 

5.  **Configure o banco de dados:** Ajuste as configurações do banco de dados no arquivo `application.properties`

6.  **Execute a aplicação Spring Boot:** Inicie a aplicação para que a API esteja disponível. 

7.  **Acesse a documentação do Swagger:** Abra o Swagger UI para visualizar e testar os endpoints da API. Está disponível em `http://localhost:8080/swagger-ui.html`. 

8.  **Teste os endpoints:** Use o Swagger UI ou ferramentas como Postman para testar os seguintes endpoints: 

    * `POST /api/clientes`: Cadastrar um novo cliente. 
    * `GET /api/clientes/{id}`: Obter detalhes de um cliente pelo ID. 
    * `GET /api/clientes/all`: Listar todos os clientes. 
    * `GET /api/clientes/{id}/avaliar-credito`: Avaliar o tipo de crédito disponível para um cliente. 
    * `GET /api/clientes/{id}/avaliar-veiculo`: Avaliar se um cliente pode financiar um veículo (Hatch ou SUV). 
    * `GET /api/clientes/eligible`: Listar clientes elegíveis para financiamento Hatch. 

9.  **Explore o código:** 🚀Sinta-se à vontade para explorar o código-fonte e entender a lógica de negócio implementada. 

##  Tecnologias utilizadas 

* Java 
* Spring Boot 
* Spring Data JPA 
* H2 Database (ou outro banco de dados) ️
* Swagger/OpenAPI 
* JUnit e Mockito 
* Lombok ️

##  Observações importantes 

* Certifique-se de que o Java JDK 17 (ou superior) e Maven/Gradle estão instalados e configurados corretamente. 
* Utilize as boas práticas de desenvolvimento de APIs RESTful. 
* Implemente testes unitários e de integração para garantir a qualidade do código. 
* Documente seu código para facilitar o entendimento. 

## Informações adicionais 

Este projeto é baseado e cumpre os requisitos que foram solicitados no repositório [https://github.com/Neurolake/challenge-java-developer](https://github.com/Neurolake/challenge-java-developer). 
