package com.itq.generacompra.business;

import com.itq.generacompra.dto.Purchase;
import com.itq.generacompra.dto.UpdatePurchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {
    private Map<Integer, Purchase> purchases = new HashMap<>();
    private AtomicInteger idCounter = new AtomicInteger();

    @Async
    public CompletableFuture<Purchase> createPurchase(Purchase purchase) {
        return CompletableFuture.supplyAsync(() -> {
            int idPurchase = idCounter.incrementAndGet();
            purchase.setIdPurchase(idPurchase);
            purchases.put(idPurchase, purchase);
            return purchase;
        });
    }

    @Async
    public CompletableFuture<Purchase> getPurchaseById(int idPurchase) {
        return CompletableFuture.supplyAsync(() -> purchases.get(idPurchase));
    }

    @Async
    public CompletableFuture<List<Purchase>> getAllPurchases() {
        return CompletableFuture.supplyAsync(() -> purchases.values().stream().collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<List<Purchase>> getPurchasesByOrderDate(String orderDate) {
        return CompletableFuture.supplyAsync(() -> purchases.values().stream().filter(p -> p.getOrderDate().equals(orderDate)).collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<Purchase> updatePurchase(UpdatePurchase updatepurchase) {
        return CompletableFuture.supplyAsync(() -> {
            Purchase purchase = purchases.get(updatepurchase.getIdPurchase());
            purchase.setDeliveryDate(updatepurchase.getDeliveryDate());
            purchase.setStatus(updatepurchase.getStatus());
            purchase.setProblem(updatepurchase.getProblem());
            return purchase;
        });
    }

    @Async
    public CompletableFuture<Void> deletePurchase(int idPurchase) {
        return CompletableFuture.runAsync(() -> purchases.remove(idPurchase));
    }
}