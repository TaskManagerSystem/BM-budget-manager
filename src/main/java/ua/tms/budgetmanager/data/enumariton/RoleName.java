package ua.tms.budgetmanager.data.enumariton;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleName {
    USER(1L),
    MANAGER(2L),
    ADMIN(3L);

    private final long id;
}
