package com.service.EventMicroservice.exception;

public class HTTPRequestException extends RuntimeException{
        public HTTPRequestException(String message) {
            super(message);
        }

}
