package com.ecommerce.customer.controller;

import com.ecommerce.customer.config.VNPayService;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final CustomerService customerService;
    private final OrderService orderService;

    private final ShoppingCartService cartService;

    private final CountryService countryService;

    private final CityService cityService;

    private final VNPayService vnPayService;

    @GetMapping("/check-out")
    public String checkOut(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            CustomerDto customer = customerService.getCustomer(principal.getName());
            if (customer.getAddress() == null || customer.getCity() == null || customer.getPhoneNumber() == null) {
                model.addAttribute("information", "Bạn cần cập nhật đầy đủ thông tin trước khi thanh toán");
                List<Country> countryList = countryService.findAll();
                List<City> cities = cityService.findAll();
                model.addAttribute("customer", customer);
                model.addAttribute("cities", cities);
                model.addAttribute("countries", countryList);
                model.addAttribute("title", "Thông tin tài khoản");
                model.addAttribute("page", "Thông tin tài khoản");
                return "customer-information";
            } else {
                ShoppingCart cart = customerService.findByUsername(principal.getName()).getCart();
                model.addAttribute("customer", customer);
                model.addAttribute("title", "Đặt hàng-");
                model.addAttribute("page", "Đặt hàng-");
                model.addAttribute("shoppingCart", cart);
                model.addAttribute("grandTotal", cart.getTotalItems());
                return "checkout";
            }
        }
    }

    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            List<Order> orderList = customer.getOrders();
            model.addAttribute("orders", orderList);
            model.addAttribute("title", "Đơn hàng");
            model.addAttribute("page", "Đơn hàng");
            return "order";
        }
    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, RedirectAttributes attributes) {
        orderService.cancelOrder(id);
        attributes.addFlashAttribute("success", "Hủy đơn thành công !");
        return "redirect:/orders";
    }


    @RequestMapping(value = "/add-order", method = {RequestMethod.POST})
    public String createOrder(Principal principal,
                              Model model,
                              HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();
            Order order = orderService.save(cart);
            session.removeAttribute("totalItems");
            model.addAttribute("order", order);
            model.addAttribute("title", "Thông tin đơn hàng");
            model.addAttribute("page", "Thông tin đơn hàng");
            model.addAttribute("success", "Thêm đơn hàng thành công !");
            return "order-detail";
        }
    }
    @GetMapping("/vnpay")
//    @RequestMapping(value = "/vnpay", method = {RequestMethod.GET})
    public String VnPay(Principal principal,Model model){
        CustomerDto customer = customerService.getCustomer(principal.getName());
        ShoppingCart cart = customerService.findByUsername(principal.getName()).getCart();
        model.addAttribute("customer", customer);
        model.addAttribute("shoppingCart", cart);
        model.addAttribute("grandTotal", cart.getTotalItems());
        return "vnpay";
    }
    @PostMapping("/submitOrder")
//    @RequestMapping(value = "/submitOrder", method = {RequestMethod.POST})
    public String submidOrder(@RequestParam("amount") double orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        int Total = (int)orderTotal;
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(Total, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }
    @GetMapping("/vnpay-payment")
//    @RequestMapping(value = "/vnpay-payment", method = {RequestMethod.GET})
    public String GetMapping(HttpServletRequest request, Model model, Principal principal, HttpSession session){
        int paymentStatus =vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        ///
        CustomerDto customer = customerService.getCustomer(principal.getName());
        ShoppingCart cart = customerService.findByUsername(principal.getName()).getCart();
        model.addAttribute("customer", customer);
        Order order = orderService.save(cart);
        session.removeAttribute("totalItems");
        model.addAttribute("order", order);
        model.addAttribute("shoppingCart", cart);
        ///
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}
