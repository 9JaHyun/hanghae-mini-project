package com.miniproject.common.auditing;

import com.miniproject.config.security.domain.UserDetailsImpl;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
              .getAuthentication().getPrincipal();

        if (userDetails == null) {
            return null;
        }
        return Optional.of(userDetails.getUser().getId());
    }
}
