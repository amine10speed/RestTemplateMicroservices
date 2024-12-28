package com.voiture.ser;

import com.voiture.ser.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientService {

    @Autowired
    private RestTemplate restTemplate;

    public Client getClientById(Long id) {
        String url = "http://localhost:8888/SERVICE-CLIENT/clients/" + id;
        return restTemplate.getForObject(url, Client.class);
    }
}
