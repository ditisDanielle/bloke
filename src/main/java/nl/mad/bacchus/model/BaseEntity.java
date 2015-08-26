/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.mad.bacchus.support.PersistentLocalDateTimeType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Base template for entities.
 *
 * @author Jeroen van Schagen
 * @since Sep 30, 2014
 */
@MappedSuperclass
@TypeDefs({
    @TypeDef(defaultForType = LocalDateTime.class, name = "localDateTime", typeClass = PersistentLocalDateTimeType.class)
})
public abstract class BaseEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public boolean isNew() {
        return id == null;
    }

}
