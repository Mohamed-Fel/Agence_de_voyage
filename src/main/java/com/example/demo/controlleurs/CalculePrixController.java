package com.example.demo.controlleurs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.ReservationPriceRequest;
import com.example.demo.entities.ReservationPriceResponse;
import com.example.demo.services.CalculePrixService;
@RestController
@RequestMapping("/Prix")
public class CalculePrixController {
	@Autowired
    private CalculePrixService priceService;

    @PostMapping("/calculprix")
    public ReservationPriceResponse calculerPrix(@RequestBody ReservationPriceRequest request) {
        return priceService.calculerPrixTotal(request);
    }

}
