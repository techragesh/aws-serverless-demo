package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.data.Accounts;
import com.serverless.data.DynamoDBAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class PostAccountsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(PostAccountsHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
			LOG.info("received: " + input);
			//lets get our path parameter for account_id
			try{
				ObjectMapper mapper = new ObjectMapper();
				Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
				String accountId = pathParameters.get("accountId");
				Accounts tx = new Accounts();
				tx.setAccountId(accountId);
				JsonNode body = mapper.readTree((String) input.get("body"));
				String accountName = body.get("accountName").asText();
				tx.setAccountName(accountName);
				DynamoDBAdapter.getInstance().postAccount(tx);
			} catch(Exception e){
				LOG.error(e,e);
				Response responseBody = new Response("Failure putting transaction", input);
				return ApiGatewayResponse.builder()
						.setStatusCode(500)
						.setObjectBody(responseBody)
						.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
						.build();
			}
			Response responseBody = new Response("Transaction added successfully!", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(200)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
					.build();
		}
}
