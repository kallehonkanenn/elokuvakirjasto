package fi.elokuvat.elokuvakirjasto.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import fi.elokuvat.elokuvakirjasto.domain.AppUser;

@DataJpaTest
public class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Test
    public void findByUsernameReturnsUser() {
        userRepository.save(new AppUser("testuser", "hashedpassword", "ADMIN"));

        Optional<AppUser> user = userRepository.findByUsername("testuser");

        assertThat(user).isPresent();
        assertThat(user.get().getRole()).isEqualTo("ADMIN");
    }

    @Test
    public void findByUsernameReturnsEmptyForUnknownUser() {
        assertThat(userRepository.findByUsername("nobody")).isEmpty();
    }
}