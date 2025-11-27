package ru.Golov_Denis.NauJava;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.Golov_Denis.NauJava.entity.Role;
import ru.Golov_Denis.NauJava.entity.UserEntity;
import ru.Golov_Denis.NauJava.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
public class LoginSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private UserEntity createdUser;

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode("selenium_pass")).thenReturn("selenium_pass");
        when(passwordEncoder.matches("selenium_pass", "selenium_pass")).thenReturn(true);

        var existing = userRepository.findByUsername("selenium_user");
        if (existing.isPresent()) {
            createdUser = existing.get();
        } else {
            var u = new UserEntity();
            u.setUsername("selenium_user");
            u.setEmail("selenium@example.com");
            u.setPassword(passwordEncoder.encode("selenium_pass"));
            u.setRole(Role.USER);
            createdUser = userRepository.save(u);
        }

        var driverLocal = new ChromeDriver();
        driverLocal.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driverLocal.manage().window().setSize(new Dimension(1200, 800));
        driver = driverLocal;
    }


    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    void loginAndLogoutFlow() {
        var base = "http://localhost:" + port;

        driver.get(base + "/login");

        var usernameInput = driver.findElement(By.name("username"));
        var passwordInput = driver.findElement(By.name("password"));

        usernameInput.clear();
        usernameInput.sendKeys("selenium_user");
        passwordInput.clear();
        passwordInput.sendKeys("selenium_pass");

        try {
            var submit = driver.findElement(By.cssSelector("button[type=submit]"));
            submit.click();
        } catch (NoSuchElementException ex) {
            var submit = driver.findElement(By.cssSelector("input[type=submit]"));
            submit.click();
        }

        var logoutLink = driver.findElements(By.xpath("//a[contains(@href, '/logout')]"));
        Assertions.assertTrue(!driver.getCurrentUrl().contains("/login") || !logoutLink.isEmpty());
    }

}
