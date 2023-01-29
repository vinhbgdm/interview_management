package com.fa.ims.controller.rest;

import com.fa.ims.entity.Offer;
import com.fa.ims.service.OfferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/offers")
public class OfferResource {
    private final OfferService offerService;

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<?> cancelOffer(@PathVariable Long id){
        Optional<Offer> offerOptional = offerService.findById(id);
        if (offerOptional.isPresent()){
            Offer offer = offerOptional.get();
            offerService.cancel(offer);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
