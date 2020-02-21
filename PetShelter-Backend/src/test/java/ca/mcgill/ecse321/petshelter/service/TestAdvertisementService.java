package ca.mcgill.ecse321.petshelter.service;

import java.sql.Date;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAdvertisementService {

	@Autowired
	AdvertisementService advertisementService;
	
	@Autowired
	private AdvertisementRepository advertisementRepository;
	
	@Autowired
	private PetRepository petRepository;
	
	@Before
	public void clearDatabase() {
		advertisementRepository.deleteAll();
		petRepository.deleteAll();
	}
	
	public void createPet() {
	    
	}
	public void createAdvertisement() {

	}
}
