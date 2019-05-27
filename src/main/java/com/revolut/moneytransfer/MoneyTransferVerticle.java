package com.revolut.moneytransfer;

import com.revolut.moneytransfer.dao.DAOFactory;
import com.revolut.moneytransfer.endpoint.AccountEndpoint;
import com.revolut.moneytransfer.endpoint.TransactionEndpoint;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class MoneyTransferVerticle extends AbstractVerticle {
	private final AccountEndpoint accountEndpoint = new AccountEndpoint();
	private final TransactionEndpoint transactionEndpoint = new TransactionEndpoint();

	public static void main(final String[] args) {
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
		daoFactory.populateTestData();
		Launcher.executeCommand("run", MoneyTransferVerticle.class.getName());
	}

	@Override
	public void start(Future<Void> future) {

		Router router = Router.router(vertx);

		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/html").end(
					"<h1>Money Transfer</h1><table border=\"1\"> <thead> <tr> <th>HTTP METHOD</th> <th>PATH</th> <th>USAGE</th> </tr> </thead> <tbody> <tr> <td>GET</td> <td>/api/accounts</td> <td>Get All Accounts</td> </tr> <tr> <td>GET</td> <td>/api/account/{accountId}</td> <td>Get Account by account Id</td> </tr> <tr> <td>POST</td> <td>/api/createAccount</td> <td>Create Account</td> </tr> <tr> <td>POST</td> <td>/api/deposit</td> <td>Desposit amount into Account</td> </tr> <tr> <td>POST</td> <td>/api/withdraw</td> <td>Withdraw amount from Account</td> </tr> <tr> <td>DELETE</td> <td>/api/account/{accountId}</td> <td>Delete Account by account Id</td> </tr> <tr> </tr> <tr> <td>POST</td> <td>/api/transferFund</td> <td>Transfer fund between accounts</td> </tr> <tr> <td>GET</td> <td>/api/transactions</td> <td>Get all transactions</td> </tr> </tbody> </table>");
		});

		router.route("/assets/*").handler(StaticHandler.create("assets"));

		router.route().handler(BodyHandler.create());

		router.get("/api/accounts").handler(routingContext -> accountEndpoint.getAllAccounts(routingContext));
		router.get("/api/account/:accountId").handler(routingContext -> accountEndpoint.getAccount(routingContext));
		router.post("/api/createAccount").handler(routingContext -> accountEndpoint.createAccount(routingContext));
		router.post("/api/deposit").handler(routingContext -> accountEndpoint.depositAccount(routingContext));
		router.post("/api/withdraw").handler(routingContext -> accountEndpoint.withdrawAccount(routingContext));
		router.delete("/api/account/:accountId")
				.handler(routingContext -> accountEndpoint.deleteAccount(routingContext));

		router.post("/api/transferFund").handler(routingContext -> transactionEndpoint.transferFund(routingContext));
		router.get("/api/transactions")
				.handler(routingContext -> transactionEndpoint.getAllTransactions(routingContext));

		vertx.createHttpServer().requestHandler(router::accept).listen(9090, result -> {
			if (result.succeeded()) {
				future.complete();
			} else {
				future.fail(result.cause());
			}
		});
	}

}
