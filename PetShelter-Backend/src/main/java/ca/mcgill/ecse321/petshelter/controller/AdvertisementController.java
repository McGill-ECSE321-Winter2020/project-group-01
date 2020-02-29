package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.AdvertisementService;
import ca.mcgill.ecse321.petshelter.service.PetService;
import ca.mcgill.ecse321.petshelter.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.sql.Date;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController {
    
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PetService petService;
    
    @Autowired
    AdvertisementService advertisementService;
    
    @Autowired
    UserService userService;
    
    /**
     * Converts an advertisement to an advertisement DTO.
     *
     * @param ad The advertisement to convert.
     * @return An advertisement DTO.
     */

   AdvertisementDTO advertisementToDto(Advertisement ad) {
        AdvertisementDTO adDto = new AdvertisementDTO();
        Set<ApplicationDTO> applications = new HashSet<ApplicationDTO>();
        ad.getAdoptionApplication().forEach(u -> applications.add(applicationToDto(u)));
        adDto.adId = ad.getId();
        adDto.setApplicationDTO(applications);
        adDto.setDescription(ad.getDescription());
        adDto.setTitle(ad.getTitle());
        adDto.setDescription(ad.getDescription());
        adDto.setFulfilled(ad.isIsFulfilled());
        List<Long> petIds = new ArrayList<Long>();
        Set<Pet> allPets = petService.getAllPets();
        for (Pet pet : allPets) {
            if(pet.getAdvertisement().equals(ad)) {
                petIds.add(pet.getId());
            }
        }
        adDto.setPetIds(petIds);
        return adDto;
    }
    
   @PostConstruct
   public void initIt() throws Exception {
     User user = new User();
     UserDTO userDto = new UserDTO("master@email.com", "mastermaster", "M@ster123", UserType.USER, null);
     userService.createUser(userDto);
     //pet
     Pet pet = new Pet();
     PetDTO petDto = new PetDTO();
     petDto.setAdvertisement(null);
     petDto.setBreed("labrador");
     petDto.setDateOfBirth(new Date(119, 11, 20));
     petDto.setDescription("desc");
     petDto.setGender(Gender.FEMALE);
     petDto.setName("puppy");
     petDto.setPicture(null);
     petDto.setSpecies("dog");
     petDto.SetUserName("mastermaster");
     pet = petService.createPet(petDto);
     
   }   
    /**
     * Gets the desired advertisement.
     *
     * @param id Advertisement ID.
     * @return The advertisement DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdvertisement(@PathVariable(required = true) Long id) {
        Advertisement ad = advertisementService.getAdvertisementById(id);
        if (ad != null) {
            return new ResponseEntity<>(advertisementToDto(ad), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get all advertisements with a specific title.
     *
     * @param title The title for which you want to find the advertisements.
     * @return The list of all advertisements with this title.
     */
    @GetMapping("/advertisements/{title}")
    public ResponseEntity<?> getAdvertisementByTitle(@PathVariable String title) {
        List<Advertisement> ads = advertisementService.getAdvertisementByTitle(title);
        if (ads.size() >= 1) {
        List<AdvertisementDTO> adDtos = new ArrayList<AdvertisementDTO>();
        for(Advertisement ad : ads) {
            adDtos.add(advertisementToDto(ad));
        }
        return new ResponseEntity<>(adDtos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Gets all existing advertisements.
     *
     * @return List of all existing advertisements.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllAdvertisements() {
        List<Advertisement> ads = new ArrayList<>();
        List<AdvertisementDTO> adsDto = new ArrayList<>();
        Iterable<Advertisement> adsIterable = advertisementService.getAllAdvertisements();
        adsIterable.forEach(ads::add);
        for (Advertisement ad : ads) {
            adsDto.add(advertisementToDto(ad));
        }
        return new ResponseEntity<>(adsDto, HttpStatus.OK);
    }
    
    //public ResponseEntity<?> getAdvertisementByPet(@PathVariable long petId) {
        
    
    /**
     * Create new advertisement.
     *
     * @param title The title of the advertisement to create.
     * @param token The session token of the user.
     * @return The created advertisement.
     */
    @PostMapping("/{petId}/newAd")
    public ResponseEntity<?> createAdvertisement(@PathVariable(required = true) long petId, @RequestBody String title, String description, @RequestHeader String token) {
        System.out.println(title + "  " + token);
        User user = userRepository.findUserByApiToken(token);
        Pet pet = petService.getPet(petId);
        boolean isOwner = user.getPets().contains(pet);
 
        if (user != null && isOwner && title != null && !title.trim().equals("") && description != null && !description.trim().equals("")) {
            List<Long> petIds = new ArrayList<Long>();
            petIds.add(petId);
            AdvertisementDTO adDto = createAdDto (title, false, petIds, new HashSet<ApplicationDTO>(),description);
            Advertisement ad = advertisementService.createAdvertisement(adDto);
            if(ad != null) {
                return new ResponseEntity<>(adDto, HttpStatus.CREATED);  
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Update the title of an advertisement.
     *
     * @param adId The id of a given ad.
     * @param title   The new title of the ad.
     * @param token   The session token of the user.
     * @return The modified advertisement.
     */
    @PutMapping("/{adId}/title")
    public ResponseEntity<?> updateAdTitle(@PathVariable long adId, @RequestBody String title,
                                         @RequestHeader String token) {
        User user = userRepository.findUserByApiToken(token);
        Advertisement ad = advertisementService.getAdvertisementById(adId);
        boolean isOwner = hasRightsForAd(user, ad);
        if (user != null
                && ad != null // Verify the ad already exists.
                && isOwner // Check if the issuing user is the author.
                && title != null // Check if the new title is valid.
                && !title.trim().equals("")) {
            AdvertisementDTO adDto = advertisementToDto(ad);
            adDto.setTitle(title);
            ad = advertisementService.editAdvertisement(adDto);
            return new ResponseEntity<AdvertisementDTO>(adDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
    * Update the description of an advertisement.
    *
    * @param adId The id of a given ad.
    * @param desc   The new description of the ad.
    * @param token   The session token of the user.
    * @return The modified advertisement.
    */
   @PutMapping("/{adId}/description")
   public ResponseEntity<?> updateAdDescription(@PathVariable long adId, @RequestBody String description,
                                        @RequestHeader String token) {
       User user = userRepository.findUserByApiToken(token);
       Advertisement ad = advertisementService.getAdvertisementById(adId);
       boolean isOwner = hasRightsForAd(user, ad);
       if (user != null
               && ad != null // Verify the ad already exists.
               && isOwner) // Check if the issuing user is the author.
               {
           AdvertisementDTO adDto = advertisementToDto(ad);
           adDto.setDescription(description);
           ad = advertisementService.editAdvertisement(adDto);
           return new ResponseEntity<AdvertisementDTO>(adDto, HttpStatus.OK);
       } else {
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
   }
    
    /**
     * Fulfill an advertisement.
     *
     * @param adID  The id of a given ad.
     * @param token    The session token of a user.
     * @param fulfilled The status f the advertisement.
     * @return The modified advertisement.
     */
    @PutMapping("/{adId}/fulfill")
    public ResponseEntity<?> fulfillAd(@PathVariable long adId, @RequestHeader String token,
                                          @RequestBody Boolean fulfilled) {
        User user = userRepository.findUserByApiToken(token);
        Advertisement ad = advertisementService.getAdvertisementById(adId);
        boolean isOwner = hasRightsForAd(user, ad);
        if (user != null && isOwner) {
            AdvertisementDTO adDto = advertisementToDto(ad);
            adDto.setFulfilled(fulfilled);
            ad = advertisementService.editAdvertisement(adDto);
            return new ResponseEntity<AdvertisementDTO>(adDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Updates the pets of an advertisement.
     */
    @PutMapping("/{adId}/pets")
    public ResponseEntity<?> updateAdPets(@PathVariable long adId, long[] petIds, @RequestHeader String token) {
        User user = userRepository.findUserByApiToken(token);
        Advertisement ad = advertisementService.getAdvertisementById(adId);
        boolean isOwnerOfAd = hasRightsForAd(user, ad);
        boolean isOwnerOfPets = true;
        Set<Pet> pets = petService.getPetsByUser(user.getUserName());
        List<Long> newIds = new ArrayList<Long>();

        for(Long id: petIds) {
            Pet pet = petService.getPet(id);
            if(!(pets.contains(pet))) {
                isOwnerOfPets = false;
            }
            newIds.add(id);
        }
        
        if (user != null && isOwnerOfAd && isOwnerOfPets) {
            AdvertisementDTO adDto = advertisementToDto(ad);
            adDto.setPetIds(newIds);
            ad = advertisementService.editAdvertisement(adDto);
            return new ResponseEntity<AdvertisementDTO>(adDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    
    /**
     * Delete an advertisement from the database. By design, only an admin or the owner may delete an advertisement.
     *
     * @param adId ID of the advertisement to delete.
     * @param token   Session token of the user.
     * @return The deleted ad.
     */
    @DeleteMapping("/{adId}")
    public ResponseEntity<?> deleteAd(@PathVariable long adId, @RequestHeader String token) {
        User user = userRepository.findUserByApiToken(token);
        Advertisement ad = advertisementService.getAdvertisementById(adId);
        boolean isOwner = hasRightsForAd(user, ad);
        if (user != null && isOwner && ad != null) {
            advertisementService.deleteAdvertisement(ad);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    public static ApplicationDTO applicationToDto(AdoptionApplication application) {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setDescription(application.getDescription());
        applicationDTO.setUsername(application.getUser().getUserName());
        applicationDTO.setAdvertisementTitle(application.getAdvertisement().getTitle());
        applicationDTO.setIsAccepted(application.isIsAccepted());
        applicationDTO.appId = application.getId();
        
        return applicationDTO;
    }
    
    private AdvertisementDTO createAdDto (String title, boolean isfulfilled, List<Long> aD_PET_IDS, Set<ApplicationDTO> adoptionApplication, String description) {
        AdvertisementDTO dto = new AdvertisementDTO(title, isfulfilled, aD_PET_IDS, adoptionApplication, description);
        return dto;
    }
   
    public boolean hasRightsForAd(User user, Advertisement ad) {
        boolean hasRights = false;
        List<Pet> pets = petService.getPetsByAdvertisement(ad.getId());
        if(user.getUserType().equals(UserType.USER)) {
            for (Pet pet : pets) {
                if(user.getPets().contains(pet)) {
                    hasRights = true;
                }
            }
        } else {
            hasRights = true;
        }
        return hasRights;
    }
}
