package com.lurdharry.ecommerce.category;

import com.lurdharry.ecommerce.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Category {

    @Id
    @GeneratedValue
    private String id;

    private String name;

    private String description;

    @OneToOne(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Product> products;

}
