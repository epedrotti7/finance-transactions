package com.example.finance_transactions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.finance_transactions.model.Transaction;
import com.example.finance_transactions.service.TransactionService;

@SpringBootTest
@AutoConfigureMockMvc
class FinanceTransactionsApplicationTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private WebApplicationContext webApplicationContext;

        @MockBean
        private TransactionService transactionService;

        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        }

        @Test
        void testCreateTransaction() throws Exception {
                Transaction transaction = new Transaction();
                transaction.setId(UUID.randomUUID().toString());
                transaction.setMerchantCode(UUID.randomUUID());
                transaction.setAmount(100.0);
                transaction.setDescription("Mercearia do Eliton");
                transaction.setPaymentMethod("debit");
                transaction.setCardNumber("0000");
                transaction.setCardHolderName("ELITON TESTE");
                transaction.setCardExpirationDate("01/29");
                transaction.setCardCvv("123");
                transaction.setCreatedAt(LocalDateTime.now());

                Mockito.when(transactionService.createTransaction(Mockito.any(Transaction.class)))
                                .thenReturn(transaction);

                String jsonRequest = "{\"merchantCode\":\"123e4567-e89b-12d3-a456-426614174000\",\"amount\":100.00,\"description\":\"Mercearia do Eliton\",\"paymentMethod\":\"debit\",\"cardNumber\":\"0000 0000 0000 0000\",\"cardHolderName\":\"ELITON TESTE\",\"cardExpirationDate\":\"01/29\",\"cardCvv\":\"123\"}";

                mockMvc.perform(post("/api/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                                .andExpect(status().isOk())
                                .andExpect(content().json("{\"id\":\"" + transaction.getId() + "\",\"merchantCode\":\""
                                                + transaction.getMerchantCode()
                                                + "\",\"amount\":100.0,\"description\":\"Mercearia do Eliton\",\"paymentMethod\":\"debit\",\"cardNumber\":\"0000\",\"cardHolderName\":\"ELITON TESTE\",\"cardExpirationDate\":\"01/29\",\"cardCvv\":\"123\",\"createdAt\":\""
                                                + transaction.getCreatedAt().format(formatter) + "\"}"));
        }

        @Test
        void testGetTransactionById() throws Exception {
                Transaction transaction = new Transaction();
                transaction.setId("5f9b45fc-c324-4123-a4c2-507385760d44");
                transaction.setMerchantCode(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
                transaction.setAmount(100.0);
                transaction.setDescription("Mercearia do Eliton");
                transaction.setPaymentMethod("debit");
                transaction.setCardNumber("0000");
                transaction.setCardHolderName("ELITON TESTE");
                transaction.setCardExpirationDate("01/29");
                transaction.setCardCvv("123");
                transaction.setCreatedAt(LocalDateTime.now());

                Mockito.when(transactionService.getTransactionById(transaction.getId()))
                                .thenReturn(Optional.of(transaction));

                mockMvc.perform(get("/api/transactions/{id}", transaction.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().json("{\"id\":\"" + transaction.getId() + "\",\"merchantCode\":\""
                                                + transaction.getMerchantCode()
                                                + "\",\"amount\":100.0,\"description\":\"Mercearia do Eliton\",\"paymentMethod\":\"debit\",\"cardNumber\":\"0000\",\"cardHolderName\":\"ELITON TESTE\",\"cardExpirationDate\":\"01/29\",\"cardCvv\":\"123\",\"createdAt\":\""
                                                + transaction.getCreatedAt().format(formatter) + "\"}"));
        }

        @Test
        void testGetAllTransactions() throws Exception {
                Transaction transaction = new Transaction();
                transaction.setId("5f9b45fc-c324-4123-a4c2-507385760d44");
                transaction.setMerchantCode(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
                transaction.setAmount(100.0);
                transaction.setDescription("Mercearia do Eliton");
                transaction.setPaymentMethod("debit");
                transaction.setCardNumber("0000");
                transaction.setCardHolderName("ELITON TESTE");
                transaction.setCardExpirationDate("01/29");
                transaction.setCardCvv("123");
                transaction.setCreatedAt(LocalDateTime.now());

                Mockito.when(transactionService.getAllTransactions("Mercearia do Eliton", null, "ELITON TESTE"))
                                .thenReturn(Collections.singletonList(transaction));

                mockMvc.perform(get("/api/transactions")
                                .param("description", "Mercearia do Eliton")
                                .param("cardHolderName", "ELITON TESTE")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().json("[{\"id\":\"" + transaction.getId() + "\",\"merchantCode\":\""
                                                + transaction.getMerchantCode()
                                                + "\",\"amount\":100.0,\"description\":\"Mercearia do Eliton\",\"paymentMethod\":\"debit\",\"cardNumber\":\"0000\",\"cardHolderName\":\"ELITON TESTE\",\"cardExpirationDate\":\"01/29\",\"cardCvv\":\"123\",\"createdAt\":\""
                                                + transaction.getCreatedAt().format(formatter) + "\"}]"));
        }
}