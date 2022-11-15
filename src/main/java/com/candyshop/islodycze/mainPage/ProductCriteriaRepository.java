package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.model.Category;
import com.candyshop.islodycze.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ProductCriteriaRepository {

    @Autowired
    private CategoryService categoryService;

    private final EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    public ProductCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Product> findAllWithFilter(ProductPage productPage,
                                           ProductSearchCriteria productSearchCriteria) {
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);
        Predicate predicate = getPredicate(productSearchCriteria, productRoot, criteriaQuery);
        criteriaQuery.where(predicate);
        setOrder(productPage, criteriaQuery, productRoot);

        TypedQuery<Product> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(productPage.getPageNumber() * productPage.getPageSize());
        typedQuery.setMaxResults(productPage.getPageSize());

        Pageable pageable = getPagable(productPage);
        long productCount = getProductCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, productCount);
    }

    private Predicate getPredicate(ProductSearchCriteria productSearchCriteria,
                                   Root<Product> productRoot,
                                   CriteriaQuery<Product> criteriaQuery) {
        List<Predicate> predicate = new ArrayList<>();
        List<Predicate> categoryPredicate = new ArrayList<>();

        List<String> listCategoryNames = productSearchCriteria.getSearchedCategories();

        if (Objects.nonNull(productSearchCriteria.getSearchedPhrase())) {
            predicate.add(
                    criteriaBuilder.like(criteriaBuilder.upper(productRoot.get("productName")), "%"
                            + productSearchCriteria.getSearchedPhrase().toUpperCase() + "%")
            );
        }

        if (productSearchCriteria.getSearchedPriceLimit() != 0) {
            predicate.add(
                    criteriaBuilder.lessThanOrEqualTo(productRoot.get("price"),
                            productSearchCriteria.getSearchedPriceLimit())
            );
        }

        if (productSearchCriteria.isShowOnlyAvailable()) {
            predicate.add(
                    criteriaBuilder.notEqual(productRoot.get("amount"), 0)
            );
        }
        final Predicate otherFilters = criteriaBuilder.and(predicate.toArray(new Predicate[0]));

        if (listCategoryNames != null) {
            if (!listCategoryNames.isEmpty()) {
                for (String categoryName : listCategoryNames) {
                    Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
                    Root<Product> subqueryProduct = subquery.from(Product.class);
                    Join<Category, Product> subqueryCategory = subqueryProduct.join("categoryFk");

                    subquery.select(subqueryProduct.get("productId")).where(criteriaBuilder.equal(subqueryCategory
                            .get("categoryName"), categoryName));
                    categoryPredicate.add(criteriaBuilder.in(productRoot.get("productId")).value(subquery));
                }
                final Predicate categoryFilters = criteriaBuilder.or(categoryPredicate
                        .toArray(new Predicate[0]));
                return criteriaBuilder.and(otherFilters, categoryFilters);
            }
        }
        return otherFilters;
    }

    private void setOrder(ProductPage productPage,
                          CriteriaQuery<Product> criteriaQuery,
                          Root<Product> productRoot) {
        if (productPage.getSortingDirection().equals((Sort.Direction.ASC))) {
            criteriaQuery.orderBy(criteriaBuilder.asc(productRoot.get(productPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(productRoot.get(productPage.getSortBy())));
        }
    }

    private Pageable getPagable(ProductPage productPage) {
        Sort sort = Sort.by(productPage.getSortingDirection(), productPage.getSortBy());
        return PageRequest.of(productPage.getPageNumber(), productPage.getPageSize(), sort);
    }

    private long getProductCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
