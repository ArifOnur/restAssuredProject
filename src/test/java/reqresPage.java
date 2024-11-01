import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class reqresPage {

    @BeforeClass
    public void setup() {
        RestAssured.useRelaxedHTTPSValidation();  // SSL doğrulamasını devre dışı bırak
    }

    @Test
    public void listUser(){

        when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .time(lessThan(3000L));

    }

    @Test
    public void singleUser(){

        RestAssured.baseURI = "https://reqres.in";
        RequestSpecification httpRequest = given();
        Response response = httpRequest.get("/api/users/2");
        ResponseBody body = response.getBody();
        String bodyAsString = body.asString();
        System.out.println(bodyAsString);
        Assert.assertTrue(bodyAsString.contains("janet.weaver@reqres.in"), "String did not found");

    }

    @Test
    public void singleUserNotFound(){
        when()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .statusCode(404);
                //.body(equalTo("{}"));

    }

    @Test
    public void create(){

        String postData = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(postData)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"));
    }

    @Test
    public void update(){

        String updateData ="{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"));

    }

    @Test
    public void delete(){

        Response response = RestAssured.delete("https://reqres.in/api/users/2");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,204);
        System.out.println(statusCode);


    }





}
