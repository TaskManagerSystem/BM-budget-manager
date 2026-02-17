package ua.tms.budgetmanager.data.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import ua.tms.budgetmanager.data.enumariton.RoleName;

@Entity
@Table(name = "roles")
@Data
public class Role implements GrantedAuthority {
    @Id
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @Override
    public @Nullable String getAuthority() {
        return "ROLE_" + roleName.name();
    }
}
