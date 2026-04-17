package com.example.stocksimulator.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.stocksimulator.model.Transaction;
import com.example.stocksimulator.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
class TransactionControllerMappingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void buyEndpointIsMappedAndReturnsResponse() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setType("BUY");
        transaction.setTraderId(2L);
        transaction.setStockId(1L);
        transaction.setQuantity(10);
        transaction.setPrice(150.0);

        when(transactionService.buyStock(2L, 1L, 10)).thenReturn(transaction);

        mockMvc.perform(post("/transactions/buy")
                        .param("traderId", "2")
                        .param("stockId", "1")
                        .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.traderId").value(2))
                .andExpect(jsonPath("$.stockId").value(1))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.type").value("BUY"));
    }
}