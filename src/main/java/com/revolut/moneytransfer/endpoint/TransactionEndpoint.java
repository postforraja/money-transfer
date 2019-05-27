package com.revolut.moneytransfer.endpoint;

import java.math.BigDecimal;
import com.revolut.moneytransfer.model.Transaction;
import com.revolut.moneytransfer.service.ServiceFactory;
import com.revolut.moneytransfer.util.ConstantUtil;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class TransactionEndpoint {
	private final ServiceFactory serviceFactory = ServiceFactory.getServiceFactory();

	public void transferFund(RoutingContext routingContext) {
		Long zero = 0L;

		try {
			Transaction transaction = Json.decodeValue(routingContext.getBodyAsString(), Transaction.class);

			if (zero.equals(transaction.getSourceAccountId()) || zero.equals(transaction.getDestinationAccountId())
					|| transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0 || null == transaction.getCurrencyCode()
					|| "".equals(transaction.getCurrencyCode().trim())) {
				routingContext.response().setStatusCode(400).end();
			}

			if (2 == serviceFactory.getTransactionService().transferFund(transaction)) {
				routingContext.response().setStatusCode(200).end();
			} else {
				routingContext.response().setStatusCode(400).end();
			}

		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end();
		}
	}
	
	public void getAllTransactions(RoutingContext routingContext) {
		try {
			routingContext.response().putHeader(ConstantUtil.CONTENT_TYPE, ConstantUtil.CONTENT_TYPE_VALUE)
					.end(Json.encodePrettily(serviceFactory.getTransactionService().getAllTransactions()));
		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end();
		}
	}
}
