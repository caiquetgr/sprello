package br.com.caiqueborges.sprello.base.repository;

import br.com.caiqueborges.sprello.base.repository.entity.UserAuditedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface UserAuditedRepository<ENTITY extends UserAuditedEntity, ID> extends JpaRepository<ENTITY, ID> {

    @Modifying
    @Query("update #{#entityName} as e set e.deleted = true where e.id = :id")
    void deleteLogicallyById(ID id);

    @Override
    @Query("select e from #{#entityName} e where e.id = :id and e.deleted = false")
    Optional<ENTITY> findById(ID id);

    default void deleteLogicallyById(ENTITY entity) {
        deleteLogicallyById((ID) entity.getId());
    }

}
