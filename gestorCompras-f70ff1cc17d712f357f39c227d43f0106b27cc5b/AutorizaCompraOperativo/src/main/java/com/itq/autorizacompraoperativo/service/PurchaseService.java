package com.itq.autorizacompraoperativo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itq.autorizacompraoperativo.dto.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PurchaseService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    public void processPurchase(Purchase purchase) {
        boolean authorized;
        try {
            if (purchase.getTotalPrice() >= 100000 || purchase.isUrgent()) {
                sendToGerencial(purchase);
            } else {
                authorized = Math.random() < 0.5;
                if (authorized) {
                    System.out.println("La compra con id: " + purchase.getIdPurchase() + " fue autorizada.");
                    // sendToOperativoOut(purchase);
                } else {
                    System.out.println("La compra con id: " + purchase.getIdPurchase() + " fue rechazada.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToGerencial(Purchase purchase) {
        webClientBuilder.build()
            .post()
            .uri("http://localhost:8087/purchase/processPurchase")
            .bodyValue(purchase)
            .retrieve()
            .bodyToMono(Void.class)
            .doOnError(e -> System.err.println("Error al enviar a Gerencial: " + e.getMessage()))
            .subscribe();
    }



    private void sendToOperativoOut(Purchase purchase) {
        webClientBuilder.build()
            .post()
            .uri("http://operativo-out-service/api/endpoint")
            .bodyValue(purchase)
            .retrieve()
            .bodyToMono(Void.class)
            .subscribe();
    }
}