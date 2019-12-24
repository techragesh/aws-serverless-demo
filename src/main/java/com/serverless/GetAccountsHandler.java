package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.data.Accounts;
import com.serverless.data.DynamoDBAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAccountsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(GetAccountsHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
			LOG.info("received: " + input);
			List<Accounts> tx;
			try {
				Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
				String accountId = pathParameters.get("accountId");
				LOG.info("Getting transactions for " + accountId);
				tx = DynamoDBAdapter.getInstance().getAccounts(accountId);

				LOG.info("Headers: " + input.get("headers"));

			} catch (Exception e) {
				LOG.error(e, e);
				Response responseBody = new Response("Failure getting transactions", input);
				return ApiGatewayResponse.builder()
						.setStatusCode(500)
						.setObjectBody(responseBody)
						.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
						.build();
			}
			return ApiGatewayResponse.builder()
					.setStatusCode(200)
					.setObjectBody(tx)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
					.build();
		}


}
