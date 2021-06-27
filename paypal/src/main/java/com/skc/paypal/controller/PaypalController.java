package com.skc.paypal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.skc.paypal.bean.Order;
import com.skc.paypal.enums.TransactionEnum;
import com.skc.paypal.service.PaypalService;

@Controller
public class PaypalController {

	@Autowired
	PaypalService paypalService;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/payment")
	public String payment(@ModelAttribute("order") Order order) {
		try {
			Payment payment = paypalService.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), "http://localhost:9090/" + TransactionEnum.CANCEL_URL.getValue(),
					"http://localhost:9090/" + TransactionEnum.SUCCESS_URL.getValue());
			for(Links link:payment.getLinks()) {
				if(link.getRel().equals("approval_url")) {
					return "redirect:"+link.getHref();
				}
			}
			
		} catch (PayPalRESTException e) {
		
			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	 @GetMapping(value = "PAY/CANCEL")
	    public String cancelPay() {
	        return "cancel";
	    }

	    @GetMapping(value = "PAY/SUCCESS")
	    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
	        try {
	            Payment payment = paypalService.executePayment(paymentId, payerId);
	            System.out.println(payment.toJSON());
	            if (payment.getState().equals("approved")) {
	                return "success";
	            }
	        } catch (PayPalRESTException e) {
	         System.out.println(e.getMessage());
	        }
	        return "redirect:/";
	    }

}
