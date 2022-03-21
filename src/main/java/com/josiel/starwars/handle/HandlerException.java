package com.josiel.starwars.handle;

import com.josiel.starwars.exception.ItemOfferAmountOutOfBoundsException;
import com.josiel.starwars.exception.ItemOfferNotFoundException;
import com.josiel.starwars.exception.RebelNotFoundException;
import com.josiel.starwars.model.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler({RebelNotFoundException.class})
    public ResponseEntity<Object> hadlerTaskNotFound(RebelNotFoundException rebelNotFoundException){
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(rebelNotFoundException.getMessage());
    }

    @ExceptionHandler({ItemOfferNotFoundException.class})
    public ResponseEntity<Object> hadlerTaskNotFound(ItemOfferNotFoundException itemOfferNotFoundException){
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(itemOfferNotFoundException.getMessage());
    }

    @ExceptionHandler({ItemOfferAmountOutOfBoundsException.class})
    public ResponseEntity<Object> hadlerTaskNotFound(ItemOfferAmountOutOfBoundsException itemOfferAmountOutOfBoundsException){
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(itemOfferAmountOutOfBoundsException.getMessage());
    }
}
