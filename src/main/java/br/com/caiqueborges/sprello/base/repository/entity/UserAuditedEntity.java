package br.com.caiqueborges.sprello.base.repository.entity;

import br.com.caiqueborges.sprello.user.repository.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public abstract class UserAuditedEntity<ID> implements Persistable<ID> {

    @Column(name = "CREATION_DATE", nullable = false, updatable = false)
    @CreatedDate
    private ZonedDateTime createdDate;

    @Column(name = "MODIFIED_DATE", nullable = false)
    @LastModifiedDate
    private ZonedDateTime lastModifiedDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", nullable = false, updatable = false)
    private User createdBy;

    @Column(name = "CREATED_BY", insertable = false, updatable = false)
    private Long createdById;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODIFIED_BY", nullable = false)
    private User lastModifiedBy;

    @Column(name = "MODIFIED_BY", insertable = false, updatable = false)
    private Long lastModifiedById;

    @Column(name = "DELETED", nullable = false)
    private Boolean deleted = Boolean.FALSE;

    @Override
    public boolean isNew() {
        return Objects.isNull(getId());
    }

}
