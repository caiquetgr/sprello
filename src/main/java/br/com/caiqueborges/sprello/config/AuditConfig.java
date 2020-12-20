package br.com.caiqueborges.sprello.config;

import br.com.caiqueborges.sprello.user.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import static br.com.caiqueborges.sprello.config.ClockConfig.ZONE_UTC;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "clockDateTimeProvider")
public class AuditConfig {

    @Bean
    AuditorAware<User> userAuditorAware() {
        return new UserAuditorAware();
    }

    @Bean
    DateTimeProvider clockDateTimeProvider(@Autowired Clock clock) {
        return new ClockDateTimeProvider(clock);
    }

    public static class UserAuditorAware implements AuditorAware<User> {

        @Override
        public Optional<User> getCurrentAuditor() {

            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            return Optional.ofNullable(authentication)
                    .filter(Authentication::isAuthenticated)
                    .map(Authentication::getPrincipal)
                    .map(Object::toString)
                    .map(Long::valueOf)
                    .map(userId -> User.builder().id(userId).build());

        }

    }

    public static class ClockDateTimeProvider implements DateTimeProvider {

        private final Clock clock;

        public ClockDateTimeProvider(Clock clock) {
            this.clock = clock;
        }

        @Override
        public Optional<TemporalAccessor> getNow() {
            return Optional.of(ZonedDateTime.ofInstant(clock.instant(), ZONE_UTC));
        }

    }

}
