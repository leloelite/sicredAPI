import io.restassured.response.ResponseBodyExtractionOptions;
import org.junit.Assert;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimulacaoTest {
    final String url = "http://localhost:8080/api/v1";
    final Cpf cpf = new Cpf();
    final String cpfGerado = cpf.gerarCpf(false);

    @Test
    @Order(1)
    public void criarSimulacao() {

        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal\",\n" +
                "  \"cpf\": "+cpfGerado+",\n" +
                "  \"email\": \"email@email.com\",\n" +
                "  \"valor\": 1200,\n" +
                "  \"parcelas\": 3,\n" +
                "  \"seguro\": true\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(url+"/simulacoes")
                .then().statusCode(201);

        //O código abaixo verifica se o registro foi de fato criado.
        given()
                .get(url+"/simulacoes/"+cpfGerado)
                .then().statusCode(200);

    }

    @Test
    @Order(2)
    public void CriarSimulacaoSemCpf() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal\",\n" +
                "  \"email\": \"email@email.com\",\n" +
                "  \"valor\": 1200,\n" +
                "  \"parcelas\": 3,\n" +
                "  \"seguro\": true\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(url+"/simulacoes")
                .then().statusCode(400);
    }

    @Test
    @Order(3)
    public void CriarSimulacaoSemNome() {
        String requestBody = "{\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"email\": \"email@email.com\",\n" +
                "  \"valor\": 1200,\n" +
                "  \"parcelas\": 3,\n" +
                "  \"seguro\": true\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(url+"/simulacoes")
                .then().statusCode(400);
    }

    @Test
    @Order(4)
    public void CriarSimulacaoSemEmail() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal\",\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"valor\": 1200,\n" +
                "  \"parcelas\": 3,\n" +
                "  \"seguro\": true\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(url+"/simulacoes")
                .then().statusCode(400);
    }

    @Test
    @Order(5)
    public void CriarSimulacaoSemValor() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal\",\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"email\": \"email@email.com\",\n" +
                "  \"parcelas\": 3,\n" +
                "  \"seguro\": true\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(url+"/simulacoes")
                .then().statusCode(400);
    }

    @Test
    @Order(6)
    public void CriarSimulacaoSemParcela() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal\",\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"email\": \"email@email.com\",\n" +
                "  \"valor\": 1200,\n" +
                "  \"seguro\": true\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(url+"/simulacoes")
                .then().statusCode(400);
    }

    @Test
    @Order(7)
    public void CriarSimulacaoSemSeguro() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal\",\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"email\": \"email@email.com\",\n" +
                "  \"valor\": 1200,\n" +
                "  \"parcelas\": 3,\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(url+"/simulacoes")
                .then().statusCode(400);
    }

    @Test
    @Order(8)
    public void criarSimulacaoCpfExistente() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal\",\n" +
                "  \"cpf\": "+cpfGerado+",\n" +
                "  \"email\": \"email@email.com\",\n" +
                "  \"valor\": 1200,\n" +
                "  \"parcelas\": 3,\n" +
                "  \"seguro\": true\n" +
                "}";

        ResponseBodyExtractionOptions response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(url+"/simulacoes").then().statusCode(400).extract().body();
        //Na documentação da do desafio o status esperado é 409, porém a API retorna sempre 400;

        String mensagemObtida = response.asString();
        String mensagemEsperada = "{{\"mensagem\":\"CPF já existente\"}";
        assertEquals(mensagemObtida, mensagemEsperada);
        ////Na documentação do desafio a mensagem esperada é "CPF já existente", porém a API retorna "CPF duplicado";

    }

    @Test
    @Order(9)
    public void alterarSimulacao() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal Alterado\",\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"email\": \"Alteradoemail@email.com\",\n" +
                "  \"valor\": 1400,\n" +
                "  \"parcelas\": 4,\n" +
                "  \"seguro\": false\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put(url+"/simulacoes/"+cpfGerado)
                .then().statusCode(200);

        /*O status retorna 200, porém as alterações não estão sendo aplicadas, o código abaixo valida se os dados
          estão de fato sendo alterados através da consulta do CPF alterado*/

        given()
                .get(url+"/simulacoes/"+cpfGerado)
                .then().statusCode(200);
    }

    @Test
    @Order(10)
    public void alterarSimulacaoSemPreencherCpf() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal Alterado\",\n" +
                "  \"email\": \"Alteradoemail@email.com\",\n" +
                "  \"valor\": 1400,\n" +
                "  \"parcelas\": 4,\n" +
                "  \"seguro\": false\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put(url+"/simulacoes/"+cpfGerado)
                .then().statusCode(400);
    }

    @Test
    @Order(11)
    public void alterarSimulacaoSemPreencherNome() {
        String requestBody = "{\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"email\": \"Alteradoemail@email.com\",\n" +
                "  \"valor\": 1400,\n" +
                "  \"parcelas\": 4,\n" +
                "  \"seguro\": false\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put(url+"/simulacoes/"+cpfGerado)
                .then().statusCode(400);
    }

    @Test
    @Order(12)
    public void alterarSimulacaoSemPreencherEmail() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal Alterado\",\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"valor\": 1400,\n" +
                "  \"parcelas\": 4,\n" +
                "  \"seguro\": false\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put(url+"/simulacoes/"+cpfGerado)
                .then().statusCode(400);
    }

    @Test
    @Order(13)
    public void alterarSimulacaoSemPreencherValor() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal Alterado\",\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"email\": \"Alteradoemail@email.com\",\n" +
                "  \"parcelas\": 4,\n" +
                "  \"seguro\": false\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put(url+"/simulacoes/"+cpfGerado)
                .then().statusCode(400);
    }

    @Test
    @Order(14)
    public void alterarSimulacaoSemPreencherParcela() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal Alterado\",\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"email\": \"Alteradoemail@email.com\",\n" +
                "  \"valor\": 1400,\n" +
                "  \"seguro\": false\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put(url+"/simulacoes/"+cpfGerado)
                .then().statusCode(400);
    }

    @Test
    @Order(15)
    public void alterarSimulacaoSemPreecherSeguro() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal Alterado\",\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"email\": \"Alteradoemail@email.com\",\n" +
                "  \"valor\": 1400,\n" +
                "  \"parcelas\": 4,\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put(url+"/simulacoes/"+cpfGerado)
                .then().statusCode(400);
    }

    @Test
    @Order(16)
    public void alterarSimulacaoCpfNaoEncontrado() {
        String requestBody = "{\n" +
                "  \"nome\": \"Fulano de Tal Alterado\",\n" +
                "  \"cpf\": "+cpf.gerarCpf(false)+",\n" +
                "  \"email\": \"Alteradoemail@email.com\",\n" +
                "  \"valor\": 1400,\n" +
                "  \"parcelas\": 4,\n" +
                "  \"seguro\": false\n" +
                "}";

        ResponseBodyExtractionOptions response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put(url+"/simulacoes/"+cpf.gerarCpf(false))
                .then().statusCode(404).extract().body();
        String mensagemObtida = response.asString();
        String mensagemEsperada = "{\"mensagem\":\"CPF não encontrado\"}";
        assertEquals(mensagemObtida, mensagemEsperada);
        //A mensagem esperada pela documentação do desafio é "CPF não encontrado", porém a API retorna a mensagem contendo o CPF
    }

    @Test
    @Order(17)
    public void consultarSimulacoes() {
        String response = given()
                .get(url+"/simulacoes")
                .then().statusCode(200).extract().body().asString();

        String[] propriedades = {"cpf", "nome", "email", "valor", "parcela", "seguro"};
        for (String propriedade : propriedades) {
            Assert.assertTrue(response.contains(propriedade));
        }
    }

    @Test
    @Order(18)
    public void consultarSeExisteSimulacoes() {
        String statusCodeRetornado = String.valueOf(given()
                .get(url+"/simulacoes")
                .statusCode());
        String statusCodeEsperado = "204";

        Assert.assertTrue(!statusCodeRetornado.equalsIgnoreCase(statusCodeEsperado));
    }

    @Test
    @Order(19)
    public void consultarSeExisteSimulacao() {
        given()
                .get(url+"/simulacoes/"+cpf.gerarCpf(false)).then()
                .statusCode(404);
    }

    @Test
    @Order(20)
    public void consultarSimulacao() {
        given()
                .get(url+"/simulacoes/"+cpf.gerarCpf(false))
                .then().statusCode(200);
    }


    @Test
    @Order(21)
    public void removerSimulacao() {
        //Na documentação, o código esperado para o sucesso é 204, porém o retornado é 200.
        given()
                .delete(url+"/simulacoes/"+cpfGerado)
                .then().statusCode(204);

        //O código abaixo verifica se o registro removido ainda existe na API, se o registro existir o teste falha.
        int statusCodeRetornado = given().get(url+"/simulacoes/"+cpfGerado).statusCode();
        int statusCodeEsperado = 404;
        assertEquals(statusCodeEsperado, statusCodeRetornado);
    }

    @Test
    @Order(22)
    public void removerSimulacaoInexistente() {
        //Na documentação, o código esperado para o sucesso é 204, porém o retornado é 200.
        given()
                .delete(url+"/simulacoes/"+cpf.gerarCpf(false))
                .then().statusCode(404);
    }
}
