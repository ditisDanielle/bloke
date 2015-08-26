package nl.mad.bacchus.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Photo extends BaseEntity {

    @NotNull
    @OneToOne
    private Wine wine;
    @NotEmpty
    @Column(length = 10000 * 1024)
    private byte[] content;
    @NotEmpty
    private String contentType;

    private Photo() {
        super();
    }

    public Photo(byte[] content, Wine wine, String contentType) {
        this();
        this.content = content;
        this.wine = wine;
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public Wine getWine() {
        return wine;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
