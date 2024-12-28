package com.voiture.ser.controllers;

import com.voiture.ser.entities.Client;
import com.voiture.ser.entities.Voiture;
import com.voiture.ser.services.VoitureService;
import com.voiture.ser.repositories.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voitures")
public class VoitureController {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private VoitureService voitureService;

    @Autowired
    private com.voiture.ser.ClientService clientService;

    // Fetch all voitures
    @GetMapping
    public ResponseEntity<Object> findAll() {
        try {
            List<Voiture> voitures = voitureService.getAllVoitures();
            return ResponseEntity.ok(voitures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching voitures: " + e.getMessage());
        }
    }

    // Fetch voiture by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            Voiture voiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture not found with ID: " + id));
            voiture.setClient(clientService.getClientById(voiture.getIdClient())); // Fetch client details
            return ResponseEntity.ok(voiture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    // Fetch voitures by client ID
    @GetMapping("/client/{id}")
    public ResponseEntity<Object> findByClientId(@PathVariable Long id) {
        try {
            Client client = clientService.getClientById(id);
            if (client != null) {
                List<Voiture> voitures = voitureService.getVoituresByClientId(id);
                voitures.forEach(v -> v.setClient(client));
                return ResponseEntity.ok(voitures);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching voitures for client ID: " + id + " - " + e.getMessage());
        }
    }

    // Save voiture with associated client
    // Save voiture with optional associated client
    @PostMapping
    public ResponseEntity<Object> save(@RequestParam(required = false) Long clientId, @RequestBody Voiture voiture) {
        try {
            if (clientId != null) {
                Client client = clientService.getClientById(clientId);
                if (client != null) {
                    voiture.setIdClient(clientId);
                    voiture.setClient(client);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Client not found with ID: " + clientId);
                }
            }
            Voiture savedVoiture = voitureService.saveVoiture(voiture);
            return ResponseEntity.ok(savedVoiture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving voiture: " + e.getMessage());
        }
    }


    // Update voiture
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Voiture updatedVoiture) {
        try {
            Voiture existingVoiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture not found with ID: " + id));

            // Update fields
            if (updatedVoiture.getMatricule() != null && !updatedVoiture.getMatricule().isEmpty()) {
                existingVoiture.setMatricule(updatedVoiture.getMatricule());
            }
            if (updatedVoiture.getMarque() != null && !updatedVoiture.getMarque().isEmpty()) {
                existingVoiture.setMarque(updatedVoiture.getMarque());
            }
            if (updatedVoiture.getModel() != null && !updatedVoiture.getModel().isEmpty()) {
                existingVoiture.setModel(updatedVoiture.getModel());
            }
            if (updatedVoiture.getIdClient() == null) {
                // Remove client association
                existingVoiture.setIdClient(null);
                existingVoiture.setClient(null);
            } else {
                // Update client association
                Client client = clientService.getClientById(updatedVoiture.getIdClient());
                if (client != null) {
                    existingVoiture.setIdClient(client.getId());
                    existingVoiture.setClient(client);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Client not found with ID: " + updatedVoiture.getIdClient());
                }
            }

            Voiture savedVoiture = voitureRepository.save(existingVoiture);
            return ResponseEntity.ok(savedVoiture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating voiture: " + e.getMessage());
        }
    }


    // Delete voiture by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        try {
            voitureRepository.deleteById(id);
            return ResponseEntity.ok("Voiture deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting voiture: " + e.getMessage());
        }
    }
}
