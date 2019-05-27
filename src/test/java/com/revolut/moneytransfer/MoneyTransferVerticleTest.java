package com.revolut.moneytransfer;

import com.jayway.restassured.RestAssured;

import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class MoneyTransferVerticleTest {

	@BeforeClass
	public static void configureRestAssured() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 9090;
	}

	@AfterClass
	public static void unconfigureRestAssured() {
		RestAssured.reset();
	}

	@Test
	public void testGetAllAccounts() {
		final int accountId = get("/api/accounts").then().assertThat().statusCode(200).extract().jsonPath()
				.getInt("find { it.balance==500.0000 }.accountId");
		get("/api/account/" + accountId).then().assertThat().statusCode(200).body("accountId", equalTo(accountId))
				.body("clientId", equalTo(4)).body("currencyCode", equalTo("USD"));
	}

	@Test
	public void testGetAccountPass() {
		get("/api/account/2000").then().assertThat().statusCode(200).body("accountId", equalTo(2000))
				.body("clientId", equalTo(2)).body("currencyCode", equalTo("USD"));
	}

	@Test
	public void testGetAccountFail() {
		get("/api/account/999").then().assertThat().statusCode(404);
	}

	@Test
	public void testCreateAccountPass() {
		given().body("{\"clientId\": \"3\",\"balance\": \"50000\",\"currencyCode\": \"GBP\",\"user\": \"Raja\"}").when()
				.post("api/createAccount").then().assertThat().statusCode(201);
	}

	@Test
	public void testCreateAccountFail() {
		given().body("{\"clientId\": \"4\",\"balance\": \"50000\",\"currencyCode\": \"djskjdsk\",\"user\": \"Raja\"}")
				.when().post("api/createAccount").then().assertThat().statusCode(400);
	}

	@Test
	public void testDepositAccountPass() {
		given().body("{\"accountId\": \"1000\",\"deltaAmount\": \"50\",\"currencyCode\": \"USD\",\"user\": \"Raja\"}")
				.when().post("api/deposit").then().assertThat().statusCode(200);
	}

	@Test
	public void testDepositAccountFail() {
		given().body("{\"accountId\": \"1000\",\"deltaAmount\": \"50\",\"currencyCode\": \"GBP\",\"user\": \"Raja\"}")
				.when().post("api/deposit").then().assertThat().statusCode(400);
	}

	@Test
	public void testWithdrawAccountPass() {
		given().body("{\"accountId\": \"1000\",\"deltaAmount\": \"50\",\"currencyCode\": \"USD\",\"user\": \"Raja\"}")
				.when().post("api/withdraw").then().assertThat().statusCode(200);
	}

	@Test
	public void testWithdrawAccountFail() {
		given().body("{\"accountId\": \"1000\",\"deltaAmount\": \"0\",\"currencyCode\": \"GBP\",\"user\": \"Raja\"}")
				.when().post("api/withdraw").then().assertThat().statusCode(400);
	}

	@Test
	public void testDeleteAccountPass() {
		delete("/api/account/3000").then().assertThat().statusCode(204);
		get("/api/account/3000").then().assertThat().statusCode(404);
	}

	@Test
	public void testDeleteAccountFail() {
		delete("/api/account/999").then().assertThat().statusCode(404);
	}

	@Test
	public void testTransferFundPass() {
		given().body(
				"{\"sourceAccountId\": \"1000\",\"destinationAccountId\": \"2000\",\"amount\": \"10\",\"currencyCode\": \"USD\",\"comment\": \"test transfer\",   \"user\": \"Raja\"}")
				.when().post("api/transferFund").then().assertThat().statusCode(200);
	}

	@Test
	public void testTransferFundFail() {
		given().body(
				"{\"sourceAccountId\": \"1000\",\"destinationAccountId\": \"2000\", \"amount\": \"0\", \"currencyCode\": \"UkfjSD\",\"comment\": \"test transfer\",   \"user\": \"Raja\"}")
				.when().post("api/transferFund").then().assertThat().statusCode(400);
	}
	
	@Test
	public void testTransferFundCurrencyCodeFail() {
		given().body(
				"{\"sourceAccountId\": \"1000\",\"destinationAccountId\": \"2000\",\"amount\": \"10\",\"currencyCode\": \"IND\",\"comment\": \"test transfer\",   \"user\": \"Raja\"}")
				.when().post("api/transferFund").then().assertThat().statusCode(400);
	}
	
	@Test
	public void testGetAllTransactions() {
		Long transactionId = (long) get("/api/transactions").then().assertThat().statusCode(200).extract().jsonPath()
				.getInt("find { it.amount==10.0000 }.transactionId");
		assertNotNull(transactionId);;
	}

}
