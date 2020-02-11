package ca.mcgill.ecse321.petshelter.config;

import java.util.Locale;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@ConfigurationProperties
@EnableScheduling
//@EnableSwagger2
/**
 * 
 * @author louis
 *
 */
public class BeanConfiguration implements WebMvcConfigurer{

	// retrieves properties from application.properties
	@Autowired
	private ApplicationProperties configProp;
	
	// Specify Bcrypt as the PW encoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	//defined to retrieve properties from app.properties
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	//bean needed for swagger
//    @Bean
//    public Docket api() { 
//        return new Docket(DocumentationType.SWAGGER_2)  
//          .select()                                  
//          .apis(RequestHandlerSelectors.any())              
//          .paths(PathSelectors.any())                          
//          .build();                                           
//    }

	// Email sending config
	@Bean
	@PostConstruct
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(configProp.getConfigValue("spring.mail.host"));
		String port = configProp.getConfigValue("spring.mail.port");
		mailSender.setPort(Integer.parseInt(port));
		mailSender.setUsername(configProp.getConfigValue("spring.mail.username"));
		mailSender.setPassword(configProp.getConfigValue("spring.mail.password"));

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.debug", true);

		return mailSender;
	}
	

	// setting the locale of the app; english by default
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.CANADA);
		return slr;
	}

	// setting a listener to language change
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	

	// listener to locale changes
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	//allows for @Scheduled tasks
	@Bean
	public AsyncTaskExecutor asyncTaskExecutor() {
		return new SimpleAsyncTaskExecutor("async");
	}
	
	//allows for @Scheduled tasks
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(-1);
		configurer.setTaskExecutor(asyncTaskExecutor());
	}
}
