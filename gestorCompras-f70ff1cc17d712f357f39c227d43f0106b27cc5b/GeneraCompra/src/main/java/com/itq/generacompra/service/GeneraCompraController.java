package com.itq.generacompra.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.itq.generacompra.business.PurchaseService;
import com.itq.generacompra.business.ReturnService;
import com.itq.generacompra.dto.Purchase;
import com.itq.generacompra.dto.UpdatePurchase;
import com.itq.generacompra.dto.Return;
import com.itq.generacompra.dto.UpdateReturn;
import com.itq.generacompra.dto.ResponseCode;

@RestController
@RequestMapping("/metalforge")
public class GeneraCompraController {

    private ReturnService returnService;
    private PurchaseService purchaseService;
    private WebClient.Builder webClientBuilder;
    
    @Autowired
    public GeneraCompraController(ReturnService returnService, PurchaseService purchaseService, WebClient.Builder webClientBuilder) {
        this.returnService = returnService;
        this.purchaseService = purchaseService;
        this.webClientBuilder = webClientBuilder;
    }

    @Async
    @PostMapping(value = "/purchase", consumes = "application/json", produces = "application/json")
    public CompletableFuture<ResponseEntity<?>> createPurchase(@RequestBody Purchase purchase) {
        return CompletableFuture.supplyAsync(() -> {
            Purchase createdPurchase = purchaseService.createPurchase(purchase).join();
            System.out.println("Compra creada con ID: " + createdPurchase.getIdPurchase());
            // Llamada asíncrona a otro servicio
            webClientBuilder.build()
                .post()
                .uri("http://localhost:8086/purchase/processPurchase")
                .bodyValue(createdPurchase)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
            return new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);
            
        });
    }

    @Async
    @GetMapping("/purchase")
    public CompletableFuture<ResponseEntity<?>> getPurchases(@RequestParam(required = false) Integer idPurchase, @RequestParam(required = false) String orderDate) {
        return CompletableFuture.supplyAsync(() -> {
            if (idPurchase != null) {
                Purchase purchase = purchaseService.getPurchaseById(idPurchase).join();
                if (purchase == null) {
                    ResponseCode responseCode = new ResponseCode();
                    responseCode.setCode("404");
                    responseCode.setMensaje("La compra no fue encontrada.");
                    return new ResponseEntity<>(responseCode, HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(purchase, HttpStatus.OK);
            } else if (orderDate != null) {
                List<Purchase> purchases = purchaseService.getPurchasesByOrderDate(orderDate).join();
                return new ResponseEntity<>(purchases, HttpStatus.OK);
            } else {
                List<Purchase> purchases = purchaseService.getAllPurchases().join();
                return new ResponseEntity<>(purchases, HttpStatus.OK);
            }
        });
    }

    @Async
    @PutMapping("/purchase")
    public CompletableFuture<ResponseEntity<?>> updatePurchase(@RequestBody UpdatePurchase updatePurchase) {
        return CompletableFuture.supplyAsync(() -> {
            Purchase purchase = purchaseService.updatePurchase(updatePurchase).join();
            if (purchase == null) {
                ResponseCode responseCode = new ResponseCode();
                responseCode.setCode("404");
                responseCode.setMensaje("La compra no fue encontrada.");
                return new ResponseEntity<>(responseCode, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(purchase, HttpStatus.OK);
        });
    }

    @Async
    @DeleteMapping("/purchase")
    public CompletableFuture<ResponseEntity<?>> deletePurchase(@RequestParam int idPurchase) {
        return CompletableFuture.supplyAsync(() -> {
            Purchase purchase = purchaseService.getPurchaseById(idPurchase).join();
            ResponseCode responseCode = new ResponseCode();
            if (purchase == null) {
                responseCode.setCode("404");
                responseCode.setMensaje("La compra no fue encontrada.");
                return new ResponseEntity<>(responseCode, HttpStatus.NOT_FOUND);
            }
            purchaseService.deletePurchase(idPurchase).join();
            responseCode.setCode("200");
            responseCode.setMensaje("La compra fue eliminada correctamente.");
            return new ResponseEntity<>(responseCode, HttpStatus.OK);
        });
    }

    @Async
    @PostMapping(value = "/return", consumes = "application/json", produces = "application/json")
    public CompletableFuture<ResponseEntity<?>> createReturn(@RequestBody Return devolucion) {
        return CompletableFuture.supplyAsync(() -> {
            Return createdReturn = returnService.createReturn(devolucion).join();
            // Llamada asíncrona a otro servicio
            webClientBuilder.build()
                .post()
                .uri("http://otro-servicio/api/endpoint")
                .bodyValue(createdReturn)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
            return new ResponseEntity<>(createdReturn, HttpStatus.CREATED);
        });
    }

    @Async
    @GetMapping("/return")
    public CompletableFuture<ResponseEntity<?>> getReturns(@RequestParam(required = false) Integer idReturn, @RequestParam(required = false) String returnDate) {
        return CompletableFuture.supplyAsync(() -> {
            if (idReturn != null) {
                Return devolucion = returnService.getReturnById(idReturn).join();
                if (devolucion == null) {
                    ResponseCode responseCode = new ResponseCode();
                    responseCode.setCode("404");
                    responseCode.setMensaje("La devolución no fue encontrada.");
                    return new ResponseEntity<>(responseCode, HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(devolucion, HttpStatus.OK);
            } else if (returnDate != null) {
                List<Return> Returns = returnService.getReturnsByReturnDate(returnDate).join();
                return new ResponseEntity<>(Returns, HttpStatus.OK);
            } else {
                List<Return> Returns = returnService.getAllReturns().join();
                return new ResponseEntity<>(Returns, HttpStatus.OK);
            }
        });
    }

    @Async
    @PutMapping("/return")
    public CompletableFuture<ResponseEntity<?>> updateReturn(@RequestBody UpdateReturn updateReturn) {
        return CompletableFuture.supplyAsync(() -> {
            Return devolucion = returnService.updateReturn(updateReturn).join();
            if (devolucion == null) {
                ResponseCode responseCode = new ResponseCode();
                responseCode.setCode("404");
                responseCode.setMensaje("La devolución no fue encontrada.");
                return new ResponseEntity<>(responseCode, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(devolucion, HttpStatus.OK);
        });
    }

    @Async
    @DeleteMapping("/return")
    public CompletableFuture<ResponseEntity<?>> deleteReturn(@RequestParam Integer idReturn) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseCode responseCode = new ResponseCode();
            Return devolucion = returnService.getReturnById(idReturn).join();
            if (devolucion == null) {
                responseCode.setCode("404");
                responseCode.setMensaje("La devolución no fue encontrada.");
                return new ResponseEntity<>(responseCode, HttpStatus.NOT_FOUND);
            }
            returnService.deleteReturn(idReturn).join();
            responseCode.setCode("200");
            responseCode.setMensaje("La devolución fue eliminada correctamente.");
            return new ResponseEntity<>(responseCode, HttpStatus.OK);
        });
    }
}