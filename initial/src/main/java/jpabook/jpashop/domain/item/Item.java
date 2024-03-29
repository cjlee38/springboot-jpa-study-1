package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /* 비즈니스 로직(재고증감) 추가 */

    /**
     * stock 증가
     */
    public void addStock(int quantitiy) {
        this.stockQuantity += quantitiy;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStcok = this.stockQuantity - quantity;
        if (restStcok < 0) {
            throw new NotEnoughStockException("need more stock");
        }

        this.stockQuantity = restStcok;
    }


}
