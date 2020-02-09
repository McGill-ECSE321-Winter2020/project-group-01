package ca.mcgill.ecse321.petshelter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class PetShelterApplicationTests {

	@Test
	void contextLoads() {
	}

}
	public void testPersistAndLoadUser() {
		// user info
		String name = "TestUserName";
		String email = "test@email.com";
		User user = new User();
		user.setUserName(name);
		user.setEmail(email);
		user.setApiToken("10");
		user.setApplications(new HashSet<AdoptionApplication>());
		user.setGender(Gender.FEMALE);
		user.setIsEmailValidated(true);
		user.setPassword("123");
		user.setPets(new HashSet<Pet>());
		userRepository.save(user);

		//user = null;

		user = userRepository.findUserByUserName(name);
		assertNotNull(user);
		assertEquals(name, user.getUserName());
	}

	@Test
	public void testPersistAndLoadPet() {
		// user info
		String name = "TestUserName";
		String email = "test@email.com";
		User user = new User();
		user.setUserName(name);
		user.setEmail(email);
		userRepository.save(user);

		// pet info
		String petName = "TestPetName";
		Date birthDate = Date.valueOf("2015-03-31");
		String species = "Dog";
		String breed = "Labrador";
		Pet pet = new Pet();
		pet.setDateOfBirth(birthDate);
		pet.setName(petName);
		pet.setSpecies(species);
		pet.setBreed(breed);
		pet.setGender(Gender.FEMALE);
		Set pets = new HashSet<Pet>();
		user.setPets(pets);
		userRepository.save(user);
		petRepository.save(pet);


		pet = null;

		pet = petRepository.findPetByName(petName);
		assertNotNull(pet);
		assertEquals(petName, pet.getName());

	}
}
