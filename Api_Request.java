package petstore.com;

import org.testng.annotations.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Api_Request {
	
	//public String baseURL = "https://petstore.swagger.io/v2/pet/1";
	JsonObject requestParam;
	JsonObject category;
	JsonObject tags;
	JsonArray categorylist;
	JsonArray taglist;
	
	public Api_Request()
	{
		//forming Request Body
		requestParam = new JsonObject();
		requestParam.addProperty("id", "1");
		category = new JsonObject();
		category.addProperty("id", "1");
		category.addProperty("name", "string");
		categorylist = new JsonArray();
		categorylist.add(category);
		requestParam.add("category", categorylist);
		
		requestParam.addProperty("name", "tom");
		requestParam.addProperty("photoUrls", "string");
		
		tags = new JsonObject();
		tags.addProperty("id", "1");
		tags.addProperty("name", "string");
		taglist = new JsonArray();
		taglist.add(tags);
		requestParam.add("tags", taglist);
		
		requestParam.addProperty("status", "Avaialble");
		RestAssured.baseURI= baseURI;
	}
	
	public String baseURI= "https://petstore.swagger.io/v2/pet/";
	
	
	@Test
	public void getResponseCode(){
			
		Response res = given().contentType(ContentType.JSON).log().all().get("/1");
		//printing whole response
		res.prettyPrint();
		
		//make sure the fields are returned from the get object
		
		res.jsonPath().get("id").equals(requestParam.get("id"));
		res.jsonPath().get("name").equals(requestParam.get("name"));
		res.jsonPath().get("status").equals(requestParam.get("status"));
		res.jsonPath().get("photoUrls").equals(requestParam.get("photoUrls"));
		res.body().jsonPath().get("category").equals(categorylist);
		res.body().jsonPath().get("tags").equals(taglist);
				
	}
	
	@Test
	public void create_a_verify_new_pet(){
		
		
		//calling the Post End point
			
		Response res=given().contentType(ContentType.JSON).log().all().body(requestParam).post();
		
		//status code validation
		res.then().assertThat().statusCode(200).extract().response();
				
		//fields validation
		requestParam.get("id").equals(res.jsonPath().get("id"));
		requestParam.get("name").equals(res.jsonPath().get("name"));
		requestParam.get("status").equals(res.jsonPath().get("status"));
		requestParam.get("photoUrls").equals(res.jsonPath().get("photoUrls"));
		
		//validating name and id of cateogry
		res.body().jsonPath().get("category").equals(categorylist);
		
		//validating tagname and id of tags
		res.body().jsonPath().get("tags").equals(taglist);
		
	}
	
	@Test
	public void update_pet_record(){
		
		requestParam.remove("name");
		requestParam.addProperty("name","modified value");
		Response res=given().contentType(ContentType.JSON).log().all().body(requestParam).put("1").
				 then().assertThat().statusCode(200).extract().response();
		res.jsonPath().get("name").equals("modified value");
	}
	
	@Test
	public void delete_pet_record(){
		//deleting
		given().queryParam("key", "arg1").delete("1").then().assertThat().statusCode(200);
		
		//demonstragte its deleted
		given().contentType(ContentType.JSON).log().all().get("/1").then().assertThat().statusCode(404);
		
	}
 
	
}
