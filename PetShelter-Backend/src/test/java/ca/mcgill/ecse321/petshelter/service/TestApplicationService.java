package ca.mcgill.ecse321.petshelter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.dto.ForumDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Application;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.ApplicationRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.ApplicationException;

@ExtendWith(MockitoExtension.class)
public class TestApplicationService {

	private static final long USER_ID = 13;
	private static final String USER_NAME = "TestPerson";
	private static final String USER_EMAIL = "TestPerson@email.com";
	private static final String USER_PASSWORD = "myP1+abc";

	private static final long APPLICATION_ID = 42;
	private static final String APPLICATION_DESCRIPTION = "SomeDescription123@";
	private static final boolean APPLICATION_IS_ACCEPTED = false;

	private static final long ADVERTISEMENT_ID = 69;
	private static final boolean ADVERTISEMENT_IS_FULFILLED = false;
	private static final String ADVERTISEMENT_DESCRIPTION = "Thebestdescription3ver!";
	private static final String ADVERTISEMENT_TITLE = "T1tle!";

	private static final long APPLICATION_ID_2 = 420;
	private static final String APPLICATION_DESCRIPTION_2 = "SomasdeDwqefescriptgwergion123@";
	private static final boolean APPLICATION_IS_ACCEPTED_2 = true;

	private static final long ADVERTISEMENT_ID_2 = 690;
	private static final boolean ADVERTISEMENT_IS_FULFILLED_2 = true;
	private static final String ADVERTISEMENT_DESCRIPTION_2 = "Thebrefestdescrggthiption3ver!";
	private static final String ADVERTISEMENT_TITLE_2 = "T1tweqrle!";

	@Mock
	private ApplicationRepository applicationRepository;

	@Mock
	private AdvertisementRepository advertisementRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ApplicationService applicationService;

	@BeforeEach
	public void setMockOutput() {
		MockitoAnnotations.initMocks(this);

		lenient().when(userRepository.findUserByUserName(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(USER_NAME)) {
				User user = new User();
				user.setUserName(USER_NAME);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASSWORD);
				user.setApplications(new HashSet<>());
				return user;
			} else {
				return null;
			}
		});

		lenient().when(advertisementRepository.findAdvertisementById(anyLong()))
				.thenAnswer((InvocationOnMock invocation) -> {
					if (invocation.getArgument(0).equals(ADVERTISEMENT_ID)) {
						Advertisement advertisement = new Advertisement();
						advertisement.setId(ADVERTISEMENT_ID);
						Set<Application> applications = new HashSet<>();
						advertisement.setApplication(applications);
						advertisement.setIsFulfilled(ADVERTISEMENT_IS_FULFILLED);
						advertisement.setDescription(ADVERTISEMENT_DESCRIPTION);
						advertisement.setTitle(ADVERTISEMENT_TITLE);
						return advertisement;
					} else {
						return null;
					}
				});

		lenient().when(applicationRepository.findById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(APPLICATION_ID)) {
				User user = new User();
				user.setId(USER_ID);
				user.setUserName(USER_NAME);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASSWORD);

				Set<Application> applications = new HashSet<>();
				Advertisement advertisement = new Advertisement();
				advertisement.setId(ADVERTISEMENT_ID);
				advertisement.setApplication(applications);
				advertisement.setIsFulfilled(ADVERTISEMENT_IS_FULFILLED);
				advertisement.setDescription(ADVERTISEMENT_DESCRIPTION);
				advertisement.setTitle(ADVERTISEMENT_TITLE);

				Application application = new Application();
				application.setId(APPLICATION_ID);
				application.setDescription(APPLICATION_DESCRIPTION);
				application.setIsAccepted(APPLICATION_IS_ACCEPTED);
				application.setUser(user);

				return application;
			} else {
				return Optional.empty();
			}
		});

		lenient().when(applicationRepository.findApplicationsByUser(any(User.class)))
				.thenAnswer((InvocationOnMock invocation) -> {
					if (((User) invocation.getArgument(0)).getId() == (USER_ID)) {
						// Create user.
						User user = new User();
						user.setId(USER_ID);
						user.setUserName(USER_NAME);
						user.setEmail(USER_EMAIL);
						user.setPassword(USER_PASSWORD);

						Set<Application> applications = new HashSet<>();
						Advertisement advertisement = new Advertisement();
						advertisement.setId(ADVERTISEMENT_ID);
						advertisement.setApplication(applications);
						advertisement.setIsFulfilled(ADVERTISEMENT_IS_FULFILLED);
						advertisement.setDescription(ADVERTISEMENT_DESCRIPTION);
						advertisement.setTitle(ADVERTISEMENT_TITLE);

						Application application = new Application();
						application.setId(APPLICATION_ID);
						application.setDescription(APPLICATION_DESCRIPTION);
						application.setIsAccepted(APPLICATION_IS_ACCEPTED);
						application.setUser(user);

						Set<Application> applications2 = new HashSet<>();
						Advertisement advertisement2 = new Advertisement();
						advertisement2.setId(ADVERTISEMENT_ID_2);
						advertisement2.setApplication(applications2);
						advertisement2.setIsFulfilled(ADVERTISEMENT_IS_FULFILLED_2);
						advertisement2.setDescription(ADVERTISEMENT_DESCRIPTION_2);
						advertisement2.setTitle(ADVERTISEMENT_TITLE_2);

						Application application2 = new Application();
						application.setId(APPLICATION_ID_2);
						application.setDescription(APPLICATION_DESCRIPTION_2);
						application.setIsAccepted(APPLICATION_IS_ACCEPTED_2);
						application.setUser(user);

						List<Application> apps = new ArrayList<>();
						apps.add(application);
						apps.add(application2);

						return apps;
					} else {
						return new ArrayList<ForumDTO>();
					}
				});

		lenient().when(applicationRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
			// Create user.
			User user = new User();
			user.setId(USER_ID);
			user.setUserName(USER_NAME);
			user.setEmail(USER_EMAIL);
			user.setPassword(USER_PASSWORD);

			Set<Application> applications = new HashSet<>();
			Advertisement advertisement = new Advertisement();
			advertisement.setId(ADVERTISEMENT_ID);
			advertisement.setApplication(applications);
			advertisement.setIsFulfilled(ADVERTISEMENT_IS_FULFILLED);
			advertisement.setDescription(ADVERTISEMENT_DESCRIPTION);
			advertisement.setTitle(ADVERTISEMENT_TITLE);

			Application application = new Application();
			application.setId(APPLICATION_ID);
			application.setDescription(APPLICATION_DESCRIPTION);
			application.setIsAccepted(APPLICATION_IS_ACCEPTED);
			application.setUser(user);

			Set<Application> applications2 = new HashSet<>();
			Advertisement advertisement2 = new Advertisement();
			advertisement2.setId(ADVERTISEMENT_ID_2);
			advertisement2.setApplication(applications2);
			advertisement2.setIsFulfilled(ADVERTISEMENT_IS_FULFILLED_2);
			advertisement2.setDescription(ADVERTISEMENT_DESCRIPTION_2);
			advertisement2.setTitle(ADVERTISEMENT_TITLE_2);

			Application application2 = new Application();
			application2.setId(APPLICATION_ID_2);
			application2.setDescription(APPLICATION_DESCRIPTION_2);
			application2.setIsAccepted(APPLICATION_IS_ACCEPTED_2);
			application2.setUser(user);

			List<Application> apps = new ArrayList<>();
			apps.add(application);
			apps.add(application2);

			return apps;
		});

		// Set a reflexive return answer.
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
		lenient().when(advertisementRepository.save(any(Advertisement.class))).thenAnswer(returnParameterAsAnswer);
		lenient().when(applicationRepository.save(any(Application.class))).thenAnswer(returnParameterAsAnswer);

	}

	@Test
	public void testCreateApplication() {
		ApplicationDTO applicationDTO = new ApplicationDTO();

		applicationDTO.setAdId(ADVERTISEMENT_ID);
		applicationDTO.setAdvertisementTitle(ADVERTISEMENT_TITLE);
		applicationDTO.setAppId(APPLICATION_ID);
		applicationDTO.setDescription(APPLICATION_DESCRIPTION);
		applicationDTO.setIsAccepted(APPLICATION_IS_ACCEPTED);
		applicationDTO.setUsername(USER_NAME);
		ApplicationDTO app2 = null;
		try {
			app2 = applicationService.createApplication(applicationDTO);
		} catch (ApplicationException e) {
		}
		assertEquals(app2.getUsername(), USER_NAME);
	}

	@Test
	public void testCreateApplicationNoDescription() {
		ApplicationDTO applicationDTO = new ApplicationDTO();

		applicationDTO.setAdId(ADVERTISEMENT_ID);
		applicationDTO.setAdvertisementTitle(ADVERTISEMENT_TITLE);
		applicationDTO.setAppId(APPLICATION_ID);
		applicationDTO.setDescription(null);
		applicationDTO.setIsAccepted(APPLICATION_IS_ACCEPTED);
		applicationDTO.setUsername(USER_NAME);

		try {
			applicationService.createApplication(applicationDTO);
			fail();
		} catch (ApplicationException e) {
			assertEquals(e.getMessage(), "Description can't be null!");
		}
	}

	@Test
	public void testCreateApplicationNoUsername() {
		ApplicationDTO applicationDTO = new ApplicationDTO();

		applicationDTO.setAdId(ADVERTISEMENT_ID);
		applicationDTO.setAdvertisementTitle(ADVERTISEMENT_TITLE);
		applicationDTO.setAppId(APPLICATION_ID);
		applicationDTO.setDescription(APPLICATION_DESCRIPTION);
		applicationDTO.setIsAccepted(APPLICATION_IS_ACCEPTED);
		applicationDTO.setUsername(null);

		try {
			applicationService.createApplication(applicationDTO);
		} catch (ApplicationException e) {
			assertEquals(e.getMessage(), "Username can't be null!");
		}
	}

	@Test
	public void testCreateApplicationNoAdvertisementTitle() {
		// assertEquals(0, applicationService.getAllApplications().size());
		ApplicationDTO applicationDTO = new ApplicationDTO();

		applicationDTO.setAdId(ADVERTISEMENT_ID);
		applicationDTO.setAdvertisementTitle(null);
		applicationDTO.setAppId(APPLICATION_ID);
		applicationDTO.setDescription(APPLICATION_DESCRIPTION);
		applicationDTO.setIsAccepted(APPLICATION_IS_ACCEPTED);
		applicationDTO.setUsername(USER_NAME);

		try {
			applicationService.createApplication(applicationDTO);
		} catch (ApplicationException e) {
			assertEquals(e.getMessage(), "Advertisement Title can't be null!");
		}
	}

	@Test
	public void testDeleteApplication() {
		try {
			//applicationService.deleteApplication(APPLICATION_ID);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConvertToDto() {
		try {
			// Create an application
			User user = new User();
			user.setId(USER_ID);
			user.setUserName(USER_NAME);
			user.setEmail(USER_EMAIL);
			user.setPassword(USER_PASSWORD);

			Set<Application> applications = new HashSet<>();
			Advertisement advertisement = new Advertisement();
			advertisement.setId(ADVERTISEMENT_ID);
			advertisement.setApplication(applications);
			advertisement.setIsFulfilled(ADVERTISEMENT_IS_FULFILLED);
			advertisement.setDescription(ADVERTISEMENT_DESCRIPTION);
			advertisement.setTitle(ADVERTISEMENT_TITLE);

			Application application = new Application();
			application.setAdvertisement(advertisement);
			application.setId(APPLICATION_ID);
			application.setDescription(APPLICATION_DESCRIPTION);
			application.setIsAccepted(APPLICATION_IS_ACCEPTED);
			application.setUser(user);

			// Convert
			ApplicationDTO applicationDTO = applicationService.convertToDto(application);

			// Test values
			assert (applicationDTO.getAdId() == ADVERTISEMENT_ID);
		} catch (ApplicationException e) {
			fail();
		}
	}

	@Test
	public void testUpdateApplication() {
		try {
			// Create application
			User user = new User();
			user.setId(USER_ID);
			user.setUserName(USER_NAME);
			user.setEmail(USER_EMAIL);
			user.setPassword(USER_PASSWORD);

			Set<Application> applications = new HashSet<>();
			Advertisement advertisement = new Advertisement();
			advertisement.setId(ADVERTISEMENT_ID);
			advertisement.setApplication(applications);
			advertisement.setIsFulfilled(ADVERTISEMENT_IS_FULFILLED);
			advertisement.setDescription(ADVERTISEMENT_DESCRIPTION);
			advertisement.setTitle(ADVERTISEMENT_TITLE);

			Application application = new Application();
			application.setId(APPLICATION_ID);
			application.setDescription(APPLICATION_DESCRIPTION);
			application.setIsAccepted(APPLICATION_IS_ACCEPTED);
			application.setUser(user);

			// Update application
			//applicationService.updateApplication(APPLICATION_ID, APPLICATION_DESCRIPTION_2);

			// Check description

		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAcceptApplication() {
		try {
			// Create application
			User user = new User();
			user.setId(USER_ID);
			user.setUserName(USER_NAME);
			user.setEmail(USER_EMAIL);
			user.setPassword(USER_PASSWORD);

			Set<Application> applications = new HashSet<>();
			Advertisement advertisement = new Advertisement();
			advertisement.setId(ADVERTISEMENT_ID);
			advertisement.setApplication(applications);
			advertisement.setIsFulfilled(ADVERTISEMENT_IS_FULFILLED);
			advertisement.setDescription(ADVERTISEMENT_DESCRIPTION);
			advertisement.setTitle(ADVERTISEMENT_TITLE);

			Application application = new Application();
			application.setId(APPLICATION_ID);
			application.setDescription(APPLICATION_DESCRIPTION);
			application.setIsAccepted(false);
			application.setUser(user);

			// Accept application
			// applicationService.acceptApplication(APPLICATION_ID);

			// Check isAccepted
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUnacceptApplication() {
		try {
			// Create application
			User user = new User();
			user.setId(USER_ID);
			user.setUserName(USER_NAME);
			user.setEmail(USER_EMAIL);
			user.setPassword(USER_PASSWORD);

			Set<Application> applications = new HashSet<>();
			Advertisement advertisement = new Advertisement();
			advertisement.setId(ADVERTISEMENT_ID);
			advertisement.setApplication(applications);
			advertisement.setIsFulfilled(ADVERTISEMENT_IS_FULFILLED);
			advertisement.setDescription(ADVERTISEMENT_DESCRIPTION);
			advertisement.setTitle(ADVERTISEMENT_TITLE);

			Application application = new Application();
			application.setId(APPLICATION_ID);
			application.setDescription(APPLICATION_DESCRIPTION);
			application.setIsAccepted(true);
			application.setUser(user);

			// Unaccept application
			//applicationService.unacceptApplication(APPLICATION_ID);

			// Check isAccepted

		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
}
