package com.voiture.ser;

import com.voiture.ser.entities.Client;
import com.voiture.ser.entities.Voiture;
import com.voiture.ser.repositories.VoitureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SerApplication.class, args);
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}



	/*@Bean
	CommandLineRunner initialiserBaseH2(VoitureRepository voitureRepository, ClientService clientService) {
		return args -> {
			Client c1 = clientService.getClientById(2L);
			Client c2 = clientService.getClientById(1L);

			System.out.println("******************************");
			System.out.println("Id est : " + c2.getId());
			System.out.println("Nom est : " + c2.getNom());
			System.out.println("Age est : " + c2.getAge());
			System.out.println("******************************");
			System.out.println("Id est : " + c1.getId());
			System.out.println("Nom est : " + c1.getNom());
			System.out.println("Age est : " + c1.getAge());
			System.out.println("******************************");

			voitureRepository.save(new Voiture(null, "hyanday", "27 34543", "Tucson", 1L, c1));
			voitureRepository.save(new Voiture(null, "Renault", "65453456", "Clio", 1L, c2));
			voitureRepository.save(new Voiture(null, "ford", "554444", "fiesta", 2L, c1));

		};
	}*/



}


