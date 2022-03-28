import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static org.junit.Assert.assertEquals;

public class RestricaoTest {

    final String url = "http://localhost:8080/api/v1";
    final Cpf cpf = new Cpf();

    @Test
    public void verificarCpfSemRestricao(){
        get(url+"/restricoes/"+cpf.gerarCpf(false)).
                then().
                statusCode(204);
    }

    @Test
    public void verificarCpfComRestricao(){

        List<String> cpf = Arrays.asList(
                "97093236014",
                "60094146012",
                "84809766080",
                "62648716050",
                "26276298085",
                "01317496094",
                "55856777050",
                "19626829001",
                "24094592008",
                "58063164083"
        );

        for(String itemCpf : cpf) {
            Response response = get(url+"/restricoes/" + itemCpf);
            ResponseBody body = response.getBody();
            response.then().
                    statusCode(200);
            String respostaObtida = body.asString();
            String respostaEsperada = "{\"mensagem\":\"O CPF " +itemCpf+ " possui restrição\"}";
            assertEquals(respostaObtida, respostaEsperada);
            // O Assert está falhando pois a mensagem da documentação está diferente da mensagem retornada pela API.
        }
    }
}
