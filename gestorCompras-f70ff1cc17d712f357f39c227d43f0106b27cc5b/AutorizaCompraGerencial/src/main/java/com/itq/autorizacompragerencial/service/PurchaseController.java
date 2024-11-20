package com.itq.autorizacompragerencial.service;

import com.itq.autorizacompragerencial.dto.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/processPurchase")
    public ResponseEntity<Void> processPurchase(@RequestBody Purchase purchase) {
        purchaseService.processPurchase(purchase);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
