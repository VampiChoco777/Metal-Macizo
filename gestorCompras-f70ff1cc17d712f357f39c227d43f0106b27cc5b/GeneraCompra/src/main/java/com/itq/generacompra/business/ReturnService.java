package com.itq.generacompra.business;

import com.itq.generacompra.dto.Return;
import com.itq.generacompra.dto.UpdateReturn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ReturnService {
    private Map<Integer, Return> returns = new HashMap<>();
    private AtomicInteger idCounter = new AtomicInteger();

    @Async
    public CompletableFuture<Return> createReturn(Return devolucion) {
        return CompletableFuture.supplyAsync(() -> {
            int idReturn = idCounter.incrementAndGet();
            devolucion.setIdReturn(idReturn);
            returns.put(idReturn, devolucion);
            return devolucion;
        });
    }

    @Async
    public CompletableFuture<Return> getReturnById(int idReturn) {
        return CompletableFuture.supplyAsync(() -> returns.get(idReturn));
    }

    @Async
    public CompletableFuture<Return> updateReturn(UpdateReturn updateReturn) {
        return CompletableFuture.supplyAsync(() -> {
            Return devolucion = returns.get(updateReturn.getIdReturn());
            devolucion.setStatus(updateReturn.getStatus());
            return devolucion;
        });
    }

    @Async
    public CompletableFuture<List<Return>> getAllReturns() {
        return CompletableFuture.supplyAsync(() -> returns.values().stream().collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<List<Return>> getReturnsByReturnDate(String returnDate) {
        return CompletableFuture.supplyAsync(() -> returns.values().stream().filter(r -> r.getReturnDate().equals(returnDate)).collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<Void> deleteReturn(int idReturn) {
        return CompletableFuture.runAsync(() -> returns.remove(idReturn));
    }
}