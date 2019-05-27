package com.revolut.moneytransfer.endpoint;

import java.math.BigDecimal;

import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.service.ServiceFactory;
import com.revolut.moneytransfer.util.ConstantUtil;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class AccountEndpoint {
	private final ServiceFactory serviceFactory = ServiceFactory.getServiceFactory();

	public void getAllAccounts(RoutingContext routingContext) {
		try {
			routingContext.response().putHeader(ConstantUtil.CONTENT_TYPE, ConstantUtil.CONTENT_TYPE_VALUE)
					.end(Json.encodePrettily(serviceFactory.getAccountService().getAllAccounts()));
		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end();
		}
	}

	public void getAccount(RoutingContext routingContext) {
		try {
			String accountIdStr = routingContext.request().getParam("accountId");

			if (null == accountIdStr || "".equals(accountIdStr.trim())) {
				routingContext.response().setStatusCode(400).end();
			} else {
				Account account = serviceFactory.getAccountService().getAccount(Long.parseLong(accountIdStr));

				if (null == account) {
					routingContext.response().setStatusCode(404).end();
				} else {
					routingContext.response().putHeader(ConstantUtil.CONTENT_TYPE, ConstantUtil.CONTENT_TYPE_VALUE)
							.end(Json.encodePrettily(account));
				}
			}
		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end();
		}
	}

	public void createAccount(RoutingContext routingContext) {
		try {
			Account account = Json.decodeValue(routingContext.getBodyAsString(), Account.class);
			Long id = serviceFactory.getAccountService().createAccount(account);

			if (null == id) {
				routingContext.response().setStatusCode(400).end();
			} else {
				account.setAccountId(id);
			}

			routingContext.response().setStatusCode(201).putHeader(ConstantUtil.CONTENT_TYPE, ConstantUtil.CONTENT_TYPE_VALUE)
					.end(Json.encodePrettily(account));
		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end();
		}
	}

	public void depositAccount(RoutingContext routingContext) {
		try {
			Account account = Json.decodeValue(routingContext.getBodyAsString(), Account.class);

			if (account.getDeltaAmount().compareTo(BigDecimal.ZERO) <= 0) {
				routingContext.response().setStatusCode(400).end();
			} else if (serviceFactory.getAccountService().updateAccount(account) == 0) {
				routingContext.response().setStatusCode(404).end();
			} else {
				routingContext.response().putHeader(ConstantUtil.CONTENT_TYPE, ConstantUtil.CONTENT_TYPE_VALUE).end(
						Json.encodePrettily(serviceFactory.getAccountService().getAccount(account.getAccountId())));
			}
		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end();
		}
	}

	public void withdrawAccount(RoutingContext routingContext) {
		try {
			Account account = Json.decodeValue(routingContext.getBodyAsString(), Account.class);

			if (account.getDeltaAmount().compareTo(BigDecimal.ZERO) <= 0) {
				routingContext.response().setStatusCode(400).end();
			}

			account.setDeltaAmount(account.getDeltaAmount().negate());

			if (serviceFactory.getAccountService().updateAccount(account) == 0) {
				routingContext.response().setStatusCode(404).end();
			} else {
				routingContext.response().putHeader(ConstantUtil.CONTENT_TYPE, ConstantUtil.CONTENT_TYPE_VALUE).end(
						Json.encodePrettily(serviceFactory.getAccountService().getAccount(account.getAccountId())));
			}
		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end();
		}
	}

	public void deleteAccount(RoutingContext routingContext) {
		try {
			String accountIdStr = routingContext.request().getParam("accountId");

			if (null == accountIdStr) {
				routingContext.response().setStatusCode(400).end();
			} else if (serviceFactory.getAccountService().deleteAccount(Long.parseLong(accountIdStr)) == 0) {
				routingContext.response().setStatusCode(404).end();
			} else {
				routingContext.response().setStatusCode(204).end();
			}
		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end();
		}
	}

}
