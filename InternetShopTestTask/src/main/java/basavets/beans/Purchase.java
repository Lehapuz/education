package basavets.beans;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : Objects.hash(id, user, product, value, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Purchase purchase = (Purchase) obj;
        return Objects.equals(id, purchase.id) && Objects.equals(user, purchase.user) && Objects.equals(product,
                purchase.product) && Objects.equals(value, purchase.value) && Objects.equals(status, purchase.status);
    }

    @Override
    public String toString() {
        return "Product name - " + product.getName() + " for User - " + user.getName();
    }
}
