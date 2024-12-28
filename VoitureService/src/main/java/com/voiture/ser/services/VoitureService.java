package com.voiture.ser.services;

import com.voiture.ser.ClientService;
import com.voiture.ser.entities.Client;
import com.voiture.ser.entities.Voiture;
import com.voiture.ser.repositories.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoitureService {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private ClientService clientService;

    /**
     * Save a voiture with its associated client.
     */
    public Voiture saveVoiture(Voiture voiture) {
        if (voiture.getIdClient() != null) {
            Client client = clientService.getClientById(voiture.getIdClient());
            if (client == null) {
                throw new IllegalArgumentException("Client with ID " + voiture.getIdClient() + " not found.");
            }
            voiture.setClient(client);
        }
        return voitureRepository.save(voiture);
    }

    /**
     * Retrieve all voitures and populate their client details.
     */
    public List<Voiture> getAllVoitures() {
        return voitureRepository.findAll().stream()
                .map(voiture -> {
                    if (voiture.getIdClient() != null) {
                        Client client = clientService.getClientById(voiture.getIdClient());
                        voiture.setClient(client);
                    }
                    return voiture;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all voitures for a specific client by their ID.
     */
    public List<Voiture> getVoituresByClientId(Long clientId) {
        List<Voiture> voitures = voitureRepository.findByIdClient(clientId);
        Client client = clientService.getClientById(clientId);
        if (client != null) {
            voitures.forEach(voiture -> voiture.setClient(client));
        }
        return voitures;
    }
}
