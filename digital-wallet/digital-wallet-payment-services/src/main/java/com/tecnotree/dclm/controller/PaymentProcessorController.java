package com.tecnotree.dclm.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.tecnotree.dclm.config.ApplicationProperties;
import com.tecnotree.dclm.model.AccessTokenDetails;
import com.tecnotree.dclm.model.AccessTokenHistory;
import com.tecnotree.dclm.model.PaymentConfirmationDetails;
import com.tecnotree.dclm.model.PaymentIntents;
import com.tecnotree.dclm.repository.AccessTokenHistoryRepository;
import com.tecnotree.dclm.service.AccessTokenHistoryService;
import com.tecnotree.dclm.util.BaseUtil;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/v1")
public class PaymentProcessorController 
{
	private static final Logger LOG = LogManager.getLogger(PaymentProcessorController.class);

	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private AccessTokenHistoryService accessTokenHistoryService;
	
	@Autowired
	ApplicationProperties appProperties;
	
	@Autowired
	private AccessTokenHistoryRepository accessTokenHistoryRepository;	
	
	@PostMapping("/transctiontoken")
	public String sendTokenNotification(@RequestBody String  entity,@RequestHeader("apikey") String apikey, @RequestHeader("apipasscode") String apipasscode,
			@RequestHeader(name = "Accept-Language", required = false) String language, HttpServletRequest request , HttpServletResponse response )
			throws  Exception {
		
		String token = null;
		
		JSONObject json = new JSONObject(entity.toString());//NOSONAR
		
		HttpHeaders headers = new HttpHeaders();
		Query query5 = new Query();
		query5.addCriteria(new Criteria().andOperator(Criteria.where("apikey").is(apikey),
				Criteria.where("apipassocode").is(apipasscode),Criteria.where("orgId").is(json.getString("orgId"))));	
		     
		

		AccessTokenDetails tokendetails = mongoTemplate.findOne(query5, AccessTokenDetails.class);
		System.out.println("tokendetails--------:" +tokendetails);
		LOG.info("tokendetails--------: ", tokendetails);
		
		  if (tokendetails == null) { 
			  response.setStatus(401); 
		  return "Invalid Api Key or passcode"; 
		  }
		   token = UUID.randomUUID().toString();		
		//token = "asewq54877hfgthPYVJJngjnjrejy78673272hjgyfewyr6r26r37turweyt67325RSESXGD";
		//nedd to insert token in to history table
		//nedd to insert orgid,amount,status-inprogress PaymentConfirmation
		
		PaymentConfirmationDetails payment =new PaymentConfirmationDetails();
		payment.setAmount(json.getString("amount"));
		payment.setOrgId(json.getString("orgId"));
		payment.setStatus("inprogress");
		PaymentConfirmationDetails pay = mongoTemplate.save(payment);
		
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:s");
		String date5 = simpleDate.format(new Date());
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");  
	    Date date = new Date();   
		
		 Date date1=simpleDate.parse(date5);  
		
		AccessTokenHistory accessTokenHistory = new AccessTokenHistory();
		accessTokenHistory.setAccesstoken(token);
		//accessTokenHistory.setAmount(tokendetails.getAmount());
		accessTokenHistory.setAccesstokenId(pay.getId());
		accessTokenHistory.setOrgId(json.getString("orgId"));
		accessTokenHistory.setCreatedby("Sys");
		accessTokenHistory.setCreatedDate(date);
		accessTokenHistory.setModifiedDate(date);
		accessTokenHistory.setModifiedby("Sys");
		
		AccessTokenHistory savedUser = accessTokenHistoryRepository.save(accessTokenHistory);		
		
		//response lo token,transid,amount,orgId.
		JSONObject result = new JSONObject();
		
		result.put("token", savedUser.getAccesstoken());
		result.put("transid", pay.getId());
		result.put("amount", json.getString("amount"));
		result.put("orgId", tokendetails.getOrgId());
		return result.toString();
		         
	}
	
	
	
	//token,organaizatiid modified date grater than 10 mins 401 invalid token access token history table
	//lessthan 10 mins update modified date.
	
	@PostMapping("/validation")
	public String tokenvalidation(@RequestBody PaymentConfirmationDetails entity,@RequestHeader("accesstoken") String accesstoken,
			@RequestHeader(name = "Accept-Language", required = false) String language, HttpServletRequest request , HttpServletResponse response )
			throws  Exception {
		
		
		if(accesstoken==null) {//NOSONAR
			response.setStatus(500);
			 return "Token Is  Mandatory";
		}
		
		if(entity.getAmount().equals("")) {
			response.setStatus(500);
			 return "Amount Is  Mandatory";
		}
		
		if(entity.getOrgId().equals("")) {
			response.setStatus(500);
			 return "OrganisationId Is  Mandatory";
		}
		if(entity.getId().equals("")) {
			response.setStatus(500);
			 return "TransactionId Is  Mandatory";
		}
		
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("orgId").is(entity.getOrgId()),Criteria.where("_id").is(entity.getId()),Criteria.where("amount").is(entity.getAmount())));
		
		PaymentConfirmationDetails paid =mongoTemplate.findOne(query, PaymentConfirmationDetails.class);
		LOG.info("paid!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:  {} ", paid);
		
		if(paid==null) {
			response.setStatus(500);
			 return "please check the Payment Info";
		}
		
		Query query5 = new Query();
		query5.addCriteria(new Criteria().andOperator(Criteria.where("orgId").is(paid.getOrgId()),
				Criteria.where("accesstoken").is(accesstoken),Criteria.where("accesstokenId").is(paid.getId())));
		AccessTokenHistory tokendetails = mongoTemplate.findOne(query5, AccessTokenHistory.class);
		LOG.info("tokendetails------------------------------------:  {} ", tokendetails);
		
		if(tokendetails==null) {
			response.setStatus(500);  
			 return "please check the Payment Info & Token";
		}
		Date previous = tokendetails.getModifiedDate();
		
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:s");
		String date5 = simpleDate.format(new Date());
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");  
	    Date now = new Date(); 		 
		 
		 long diff = now.getTime() - previous.getTime();
		 
		 LOG.info("diff&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:  {} ", diff);
		 System.out.println("diff&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:" +diff);
		
		 if(diff >= 10 * 60 * 1000) {
			 response.setStatus(401);
			 return "token has been expired";
		 }
                 
		 AccessTokenHistory access = mongoTemplate.findById(tokendetails.getId(), AccessTokenHistory.class);
		 LOG.info("access------------------------------------:  {} ", access);
		 access.setModifiedDate(now);
		 mongoTemplate.save(access);
		 LOG.info("lessthan 10 mins:  {} ");
		 PaymentConfirmationDetails payment =mongoTemplate.findById(paid.getId(), PaymentConfirmationDetails.class);
		 payment.setVendorname(entity.getVendorname());
		 payment.setMobilno(entity.getMobilno());
		 payment.setEmailId(entity.getEmailId());
		 payment.setDescription(entity.getDescription());
		 mongoTemplate.save(payment);
		 
			JSONObject result = new JSONObject();
			
			
			
			result.put("token", accesstoken);
			result.put("transid", tokendetails.getAccesstokenId());
			result.put("amount", paid.getAmount());
			result.put("orgId", tokendetails.getOrgId());			
			
				return result.toString();
		
	}   
	
	@GetMapping(value = "/payment_methods/{id}")
	public String paymentmethods(@PathVariable String id, HttpServletResponse response,
			@RequestHeader(name = "Accept-Language", required = false) String language) throws StripeException, JsonProcessingException {
		 
		Stripe.apiKey = appProperties.getSTRIPE_SECRET_KEY();
		Query query1 = new Query(Criteria.where("orgId").is(id));
		PaymentConfirmationDetails cust = mongoTemplate.findOne(query1, PaymentConfirmationDetails.class);
		System.out.println("customer" + cust);
		LOG.info("customer: ", cust);
		Map<String, Object> params = new HashMap<>();
		params.put("customer", cust.getOrgId());
		params.put("type", "card");
		PaymentMethodCollection paymentMethods = PaymentMethod.list(params);
		String serialized = paymentMethods.toJson();		
		return serialized;

	}
	
	
	@PostMapping(value = "/paymentIntents/{id}")
	public String paymentIntents(@RequestParam Double amount, @RequestParam String paymentType,@RequestHeader("accesstoken") String accesstoken,
			 @PathVariable String id, HttpServletResponse response,	@RequestHeader(name = "Accept-Language", required = false) String language) throws StripeException, JSONException {

		if(accesstoken.equals("")) {
			response.setStatus(500);
			 return "Token Is  Mandatory";
		}
		Query query = new Query(Criteria.where("accesstoken").is(accesstoken));
		/*
		 * Query query5 = new Query(); query5.addCriteria(new
		 * Criteria().andOperator(Criteria.where("orgId").is(entity.getOrgId()),Criteria
		 * .where("accesstoken").is(accesstoken)));
		 */
		AccessTokenHistory tokendetails = mongoTemplate.findOne(query, AccessTokenHistory.class);
		if(tokendetails==null) {
			response.setStatus(500);
			 return "Invalid token";
		}
		Date previous = tokendetails.getModifiedDate();		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");  
	    Date now = new Date(); 		 
		 
		 long diff = now.getTime() - previous.getTime();
		 
		 LOG.info("diff&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:  {} ", diff);
		 System.out.println("diff&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:" +diff);
		 if(diff >= 10 * 60 * 1000) {
			 response.setStatus(401);
			 return "token has been expired";
		 }
                 
		 AccessTokenHistory access = mongoTemplate.findById(tokendetails.getId(), AccessTokenHistory.class);		 
		 access.setModifiedDate(now);
		 mongoTemplate.save(access);
		 LOG.info("lessthan 10 mins:  {} ");
		
		
		String clientSecret = "";
		Stripe.apiKey = appProperties.getSTRIPE_SECRET_KEY();
		String method = "card";
		Query query1 = new Query(Criteria.where("orgId").is(id));
		PaymentConfirmationDetails cust = mongoTemplate.findOne(query1, PaymentConfirmationDetails.class);

		try {
			if (!BaseUtil.isObjNull(cust)) {
				if (BaseUtil.isObjNull(cust.getOrgId())) {
					/******************************
					 * Stripes create Customer API start
					 *********************************/
					Map<String, Object> customerParam = new HashMap<>();
					customerParam.put("description", "My First Test Customer (created for API docs)");
					Customer customer = Customer.create(customerParam);
					/******************************
					 * Stripes create Customer API end
					 *********************************/
					JSONObject stripeCustId = new JSONObject(customer.toJson());
					String stripeCustIdVal = stripeCustId.getString("id");
					PaymentConfirmationDetails payment =mongoTemplate.findById(cust.getId(), PaymentConfirmationDetails.class);
					payment.setOrgId(stripeCustIdVal);
					 mongoTemplate.save(payment);
					//Optional<PaymentConfirmationDetails> updatedAcc = customerRegistrationService.updateOne(cust.getId(), cust);
				}
			}
			List<Object> paymentMethodTypes = new ArrayList<>();
			paymentMethodTypes.add(method);
			Map<String, Object> params = new HashMap<>();
			params.put("amount", amount.intValue());
			params.put("currency", appProperties.getCURRENCY());
			params.put("payment_method_types", paymentMethodTypes);
			// params.put("id", cust.getId());
			
			params.put("customer", cust.getOrgId());

			PaymentIntent paymentIntent = PaymentIntent.create(params);
			if (!BaseUtil.isObjNull(paymentIntent)) {
				PaymentIntents paymentIntents = new PaymentIntents();
				paymentIntents.setPaymentId(paymentIntent.getId());
				paymentIntents.setWalletId(id);
				paymentIntents.setCustomerId(cust.getId());
				paymentIntents.setAmount(amount);
				paymentIntents.setCurrency(appProperties.getCURRENCY());
				paymentIntents.setMethodType(method);
				paymentIntents.setClientSecret(paymentIntent.getClientSecret());
				paymentIntents.setStatus(paymentIntent.getStatus());
				paymentIntents.setPaymentType(paymentType);				
				PaymentIntents newEntityWallet = mongoTemplate.save(paymentIntents);
				/*
				 * if (newEntityWallet!="") { LOG.info("Created  {} ", newEntityWallet.geti); }
				 */
			}
		 clientSecret = paymentIntent.toJson();		
			//return serialized;
			//clientSecret = paymentIntent.getClientSecret();
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return clientSecret;

	}
	
	
	@PostMapping(value = "/paymentIntentsmethod/{id}")
	public String paymentIntentsmethod(@RequestParam Double amount, @RequestParam String paymentType,@RequestHeader("accesstoken") String accesstoken,
			@RequestParam String setupfutureusage, @PathVariable String id,@RequestParam  String paymentId, HttpServletResponse response,
			@RequestHeader(name = "Accept-Language", required = false) String language) throws StripeException, JSONException {

		if(accesstoken.equals("")) {
			response.setStatus(500);
			 return "Token Is  Mandatory";
		}
		Query query = new Query(Criteria.where("accesstoken").is(accesstoken));
	/*
	 * Query query5 = new Query(); query5.addCriteria(new
	 * Criteria().andOperator(Criteria.where("orgId").is(entity.getOrgId()),Criteria
	 * .where("accesstoken").is(accesstoken)));
	 */
		AccessTokenHistory tokendetails = mongoTemplate.findOne(query, AccessTokenHistory.class);
		if(tokendetails==null) {
			response.setStatus(500);
			 return "Invalid token";
		}
		Date previous = tokendetails.getModifiedDate();		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");  
	    Date now = new Date(); 		 
		 
		 long diff = now.getTime() - previous.getTime();
		 
		 //System.out.println("diff&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:" +diff);
		 
		 LOG.info("diff&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:  {} ", diff);
		 System.out.println("diff&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:" +diff);
		 if(diff >= 10 * 60 * 1000) {
			 response.setStatus(401);
			 return "token has been expired";
		 }
                 
		 AccessTokenHistory access = mongoTemplate.findById(tokendetails.getId(), AccessTokenHistory.class);		 
		 access.setModifiedDate(now);
		 mongoTemplate.save(access);
		 LOG.info("lessthan 10 mins:  {} ");
		
		String clientSecret = "";
		Stripe.apiKey = appProperties.getSTRIPE_SECRET_KEY();
		String method = "card";
		Query query1 = new Query(Criteria.where("orgId").is(id));
		PaymentConfirmationDetails cust = mongoTemplate.findOne(query1, PaymentConfirmationDetails.class);

		try {
			if (!BaseUtil.isObjNull(cust)) {
				if (BaseUtil.isObjNull(cust.getOrgId())) {
					/******************************
					 * Stripes create Customer API start
					 *********************************/
					Map<String, Object> customerParam = new HashMap<>();
					customerParam.put("description", "My First Test Customer (created for API docs)");
					Customer customer = Customer.create(customerParam);
					/******************************
					 * Stripes create Customer API end
					 *********************************/
					JSONObject stripeCustId = new JSONObject(customer.toJson());
					String stripeCustIdVal = stripeCustId.getString("id");
					PaymentConfirmationDetails payment =mongoTemplate.findById(cust.getId(), PaymentConfirmationDetails.class);
					payment.setOrgId(stripeCustIdVal);
					 mongoTemplate.save(payment);
					//Optional<CustomerRegistration> updatedAcc = customerRegistrationService.updateOne(cust.getId(),	cust);
				}
			}
			List<Object> paymentMethodTypes = new ArrayList<>();
			paymentMethodTypes.add(method);
			Map<String, Object> params = new HashMap<>();
			params.put("amount", amount.intValue());
			params.put("currency", appProperties.getCURRENCY());
			params.put("payment_method_types", paymentMethodTypes);
			// params.put("id", cust.getId());
			if (setupfutureusage.equals("off_session")) {
				params.put("setup_future_usage", setupfutureusage);
			}
			params.put("customer", cust.getOrgId());

			PaymentIntent paymentIntent = PaymentIntent.create(params);
			if (!BaseUtil.isObjNull(paymentIntent)) {
				PaymentIntents paymentIntents = new PaymentIntents();
				PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentId);
				System.out.println("paymentMethod" +paymentMethod);
				
				paymentIntents.setPaymentId(paymentIntent.getId());
				paymentIntents.setWalletId(id);
				paymentIntents.setCustomerId(cust.getId());
				paymentIntents.setAmount(amount);
				paymentIntents.setCurrency(appProperties.getCURRENCY());
				paymentIntents.setMethodType(method);
				paymentIntents.setClientSecret(paymentIntent.getClientSecret());
				paymentIntents.setStatus(paymentIntent.getStatus());
				paymentIntents.setPaymentType(paymentType);
				paymentIntents.setSetup_future_usage(setupfutureusage);
				PaymentIntents newEntityWallet = mongoTemplate.save(paymentIntents);
				//Optional<PaymentIntents> newEntityWallet = paymentIntentsService.create(paymentIntents);
				/*
				 * if (newEntityWallet.isPresent()) { LOG.info("Created  {} ",
				 * newEntityWallet.get().getId()); }
				 */
			}

			clientSecret = paymentIntent.getClientSecret();
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return clientSecret;    

	}
	 
	@PatchMapping(value = "/updatepaymentintents/{id}")
	public String updatepaymentintents(@PathVariable String id, @RequestParam String setupfutureusage,@RequestHeader("accesstoken") String accesstoken,  HttpServletResponse response,
			@RequestHeader(name = "Accept-Language", required = false) String language) throws StripeException, JsonProcessingException {
		if(accesstoken.equals("")) {
			response.setStatus(500);
			 return "Token Is  Mandatory";
		}
		Query query = new Query(Criteria.where("accesstoken").is(accesstoken));
		/*
		 * Query query5 = new Query(); query5.addCriteria(new
		 * Criteria().andOperator(Criteria.where("orgId").is(entity.getOrgId()),Criteria
		 * .where("accesstoken").is(accesstoken)));
		 */
		AccessTokenHistory tokendetails = mongoTemplate.findOne(query, AccessTokenHistory.class);
		if(tokendetails==null) {
			response.setStatus(500);
			 return "Invalid token";
		}
		Date previous = tokendetails.getModifiedDate();		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");  
	    Date now = new Date(); 		 
		 
		 long diff = now.getTime() - previous.getTime();
		 
		 //System.out.println("diff&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:" +diff);
		 
		 LOG.info("diff&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:  {} ", diff);
		 System.out.println("diff&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:" +diff);
		 if(diff >= 10 * 60 * 1000) {
			 response.setStatus(401);
			 return "token has been expired";
		 }
                 
		 AccessTokenHistory access = mongoTemplate.findById(tokendetails.getId(), AccessTokenHistory.class);		 
		 access.setModifiedDate(now);
		 mongoTemplate.save(access);
		 LOG.info("lessthan 10 mins:  {} ");	
		
		
		
		Stripe.apiKey = appProperties.getSTRIPE_SECRET_KEY();		
		PaymentIntent paymentIntent =
				  PaymentIntent.retrieve(id);
		 LOG.info("&&&&&&&&&&&&&&&&&&&paymentIntent:  {} ", paymentIntent);

		/*
		 * Map<String, Object> metadata = new HashMap<>(); metadata.put("order_id",
		 * "6735");
		 */
				Map<String, Object> params = new HashMap<>();
				//params.put("metadata", metadata);
				params.put("setup_future_usage", setupfutureusage);				
				LOG.info("params:  {} ", params);
				LOG.info("+++++++++++++++++++++++++++++++++++++++++:  {} ");

				PaymentIntent updatedPaymentIntent =
				  paymentIntent.update(params);
				String serialized = updatedPaymentIntent.toJson();
				System.out.println("serialized:" +serialized);
				LOG.info("serialized%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%:  {} ", serialized);
				return serialized;
	}

}
