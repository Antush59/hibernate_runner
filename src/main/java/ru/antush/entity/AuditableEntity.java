package ru.antush.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import ru.antush.listner.AuditDatesListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditDatesListener.class)
//@Audited
public abstract class AuditableEntity <T extends Serializable> implements BaseEntity<T>{

    private Instant createdAt;
    private String createdBy;

    private Instant updateAt;
    private String updateBy;
}
