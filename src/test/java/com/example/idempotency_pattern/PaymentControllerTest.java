package com.example.idempotency_pattern;

import com.example.idempotency_pattern.controller.PaymentController;
import com.example.idempotency_pattern.entity.PaymentRequest;
import com.example.idempotency_pattern.service.IdempotencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
public class PaymentControllerTest {

    @Autowired
    private PaymentController paymentController;

    @MockitoBean
    private IdempotencyService idempotencyService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void testProcessPayment() throws Exception {

        String idempotencyKey = "12345";
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(100));
        paymentRequest.setCurrency("USD");
        paymentRequest.setCardNumber("4111111111111111");

        //when(idempotencyService.checkAndProcessTransaction(idempotencyKey, paymentRequest.toString())).thenReturn(true);
        when(idempotencyService.checkAndProcessTransaction(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        mockMvc.perform(post("/payment")
                        .header("Idempotency-Key", idempotencyKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 100.0, \"currency\": \"USD\", \"cardNumber\": \"4111111111111111\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment processed successfully"));
    }

    @Test
    void testDuplicatePayment() throws Exception {
        String idempotencyKey = "12345";
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(100));
        paymentRequest.setCurrency("USD");
        paymentRequest.setCardNumber("4111111111111111");

        when(idempotencyService.checkAndProcessTransaction(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        mockMvc.perform(post("/payment")
                        .header("Idempotency-Key", idempotencyKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 100.0, \"currency\": \"USD\", \"cardNumber\": \"4111111111111111\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Duplicate request, Payment already processed with the given Idempotency Key."));
    }

}
