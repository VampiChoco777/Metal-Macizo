package com.itq.autorizacompragerencial.service;

import com.itq.autorizacompragerencial.dto.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PurchaseService {
    /* 
    @Autowired
    private WebClient.Builder webClientBuilder;*/

    public void processPurchase(Purchase purchase) {
        boolean authorized = Math.random() < 0.5;
        if (authorized) {
            System.out.println("La compra con id: " + purchase.getIdPurchase() + " fue autorizada.");
            // sendToOperativoOut(purchase);
        } else {
            System.out.println("La compra con id: " + purchase.getIdPurchase() + " fue rechazada.");
        }
    }

    /* 
    private void sendToGerencial(Purchase purchase) {
        webClientBuilder.build()
            .post()
            .uri("http://localhost:8087/purchase/processPurchase")
            .bodyValue(purchase)
            .retrieve()
            .bodyToMono(Void.class)
            .subscribe();
    }

    private void sendToOperativoOut(Purchase purchase) {
        webClientBuilder.build()
            .post()
            .uri("http://localhost:8088/api/handleAuthorizedPurchase")
            .bodyValue(purchase)
            .retrieve()
            .bodyToMono(Void.class)
            .subscribe();
    }*/
}