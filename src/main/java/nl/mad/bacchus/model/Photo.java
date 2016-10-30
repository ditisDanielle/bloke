package nl.mad.bacchus.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Photo extends BaseEntity {


    @NotNull
    @OneToOne
    private Product product;
    @NotEmpty
    @Column(length = 10000 * 1024)
    private byte[] content;
    @NotEmpty
    private String contentType;

    private Photo() {
        super();
    }

    public Photo(byte[] content, Product product, String contentType) {
        this();
        this.content = content;
        this.product = product;
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public Product getProduct() {
        return product;
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
