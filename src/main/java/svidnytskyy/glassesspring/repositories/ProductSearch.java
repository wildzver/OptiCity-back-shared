package svidnytskyy.glassesspring.repositories;

import org.apache.lucene.search.*;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import svidnytskyy.glassesspring.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ProductSearch {
    @PersistenceContext
    EntityManager em;

    public Page<Product> searchAllProducts(String text,
                                           Pageable pageable,
                                           String sortBy,
                                           String sortDirection,
                                           String sexes,
                                           Integer minPrice,
                                           Integer maxPrice,
                                           String lensColors,
                                           String frameColors,
                                           String frameMaterials,
                                           String diopters,
                                           boolean polarization) throws InterruptedException {
        List<Product> results = new LinkedList<>();
        long totalResults = 0;
        if (!text.equals("")) {
            totalResults = getJpaQuery(
                    text,
                    sexes,
                    minPrice,
                    maxPrice,
                    lensColors,
                    frameColors,
                    frameMaterials,
                    diopters,
                    polarization
            ).getResultSize();

            Sort sort;
            if (sortBy.equalsIgnoreCase("price")) sortBy = "productDetails.".concat(sortBy);
            if (sortDirection.equalsIgnoreCase("desc")) {
                sort = getQueryBuilder()
                        .sort()
                        .byField(sortBy + "_Sort").desc()
                        .createSort();
            } else {
                sort = getQueryBuilder()
                        .sort()
                        .byField(sortBy + "_Sort").asc()
                        .createSort();
            }
            try {
                results = getJpaQuery(
                        text,
                        sexes,
                        minPrice,
                        maxPrice,
                        lensColors,
                        frameColors,
                        frameMaterials,
                        diopters,
                        polarization)
                        .setSort(sort)
                        .setFirstResult((int) pageable.getOffset())
                        .setMaxResults(pageable.getPageSize())
                        .getResultList();
            } catch (NullPointerException e) {
                System.out.println("SOMESING WRONG!" + e);
            }
        }
        return new PageImpl<>(results, pageable, totalResults);
    }

    public List<Product> searchNumberProducts(String text,
                                              Integer numberOfResults,
                                              String sexes,
                                              Integer minPrice,
                                              Integer maxPrice,
                                              String lensColors,
                                              String frameColors,
                                              String frameMaterials,
                                              String diopters,
                                              boolean polarization) throws InterruptedException {


        List<Product> results = new ArrayList<>();
        if (!text.equals("")) {

            try {
                results = getJpaQuery(
                        text,
                        sexes,
                        minPrice,
                        maxPrice,
                        lensColors,
                        frameColors,
                        frameMaterials,
                        diopters,
                        polarization)
                        .setMaxResults(numberOfResults)
                        .getResultList();
            } catch (NullPointerException e) {
                results = getJpaQuery(
                        text,
                        sexes,
                        minPrice,
                        maxPrice,
                        lensColors,
                        frameColors,
                        frameMaterials,
                        diopters,
                        polarization
                ).getResultList();

            }
        }
        return results;
    }

    private FullTextQuery getJpaQuery(String text,
                                      String sexes,
                                      Integer minPrice,
                                      Integer maxPrice,
                                      String lensColors,
                                      String frameColors,
                                      String frameMaterials,
                                      String diopters,
                                      boolean polarization
    ) throws InterruptedException {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        BooleanJunction bool = getQueryBuilder().bool()/*.must(getQueryBuilder().all().createQuery())*/;
        Query priceRangeQuery = getQueryBuilder()
                .range()
                .onField("productDetails.price")
                .from(minPrice).to(maxPrice)
                .createQuery();
        bool = bool.must(priceRangeQuery);

        Query searchTextQuery = getQueryBuilder()
                .keyword()
                .onFields(
                        "productDetails.lensMaterial.name",
                        "productDetails.lensMaterial.uaName",
                        "productDetails.frameMaterial.name",
                        "productDetails.frameMaterial.uaName",
                        "productDetails.origin.name",
                        "productDetails.origin.uaName",
                        "productDetails.sex.name",
                        "productDetails.sex.uaName",
                        "productDetails.category.uaName",
                        "productDetails.category.name",
                        "lensColor.name",
                        "lensColor.uaName",
                        "frameColor.name",
                        "frameColor.uaName",
                        "diopters.value",
                        "productNumber"
                )
                .matching(text)
                .createQuery();
        bool = bool.must(searchTextQuery);

        Query mainImageQuery = getQueryBuilder()
                .keyword()
                .onField("images.isMainImage")
                .matching(true)
                .createQuery();
        bool = bool.must(mainImageQuery);
        if (!sexes.equals("")) {
            Query sexesQuery = getQueryBuilder()
                    .keyword()
                    .onField("productDetails.sex.name_Filter")
                    .matching(sexes)
                    .createQuery();
            bool = bool.must(sexesQuery);
        }

        if (!lensColors.equals("")) {
            Query lensColorsQuery = getQueryBuilder()
                    .keyword()
                    .onField("lensColor.name_Filter")
                    .matching(lensColors)
                    .createQuery();
            bool = bool.must(lensColorsQuery);
        }

        if (!frameColors.equals("")) {
            Query frameColorsQuery = getQueryBuilder()
                    .keyword()
                    .onField("frameColor.name_Filter")
                    .matching(frameColors)
                    .createQuery();

            bool = bool.must(frameColorsQuery);
        }

        if (!frameMaterials.equals("")) {
            Query frameMaterialsQuery = getQueryBuilder()
                    .keyword()
                    .onField("productDetails.frameMaterial.name_Filter")
                    .matching(frameMaterials)
                    .createQuery();

            bool = bool.must(frameMaterialsQuery);
        }

        if (!diopters.equals("")) {
            Query dioptersQuery = getQueryBuilder()
                    .keyword()
                    .onField("diopters.value_Filter")
                    .matching(diopters)
                    .createQuery();

            bool = bool.must(dioptersQuery);
        }

        if (polarization == true) {
            Query polarizationQuery = getQueryBuilder()
                    .keyword()
                    .onField("productDetails.polarization_Filter")
                    .matching(true)
                    .createQuery();
            bool = bool.must(polarizationQuery);
        }

        Query luceneQuery = bool.createQuery();

        return fullTextEntityManager.createFullTextQuery(luceneQuery, Product.class);
    }

    private QueryBuilder getQueryBuilder() throws InterruptedException {

        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);

        return fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Product.class)
                .overridesForField("productDetails.category.uaName", "customanalyzer")
                .overridesForField("productDetails.category.name", "customanalyzer")
                .overridesForField("productDetails.sex.name", "customanalyzer")
                .overridesForField("productDetails.sex.uaName", "customanalyzer")
                .overridesForField("lensColor.uaName", "customanalyzer")
                .overridesForField("lensColor.name", "customanalyzer")
                .overridesForField("frameColor.uaName", "customanalyzer")
                .overridesForField("frameColor.name", "customanalyzer")
                .overridesForField("productDetails.lensMaterial.name", "customanalyzer")
                .overridesForField("productDetails.lensMaterial.uaName", "customanalyzer")
                .overridesForField("productDetails.frameMaterial.name", "customanalyzer")
                .overridesForField("productDetails.frameMaterial.uaName", "customanalyzer")
                .overridesForField("productDetails.origin.name", "customanalyzer")
                .overridesForField("productDetails.origin.uaName", "customanalyzer")
                .overridesForField("diopters.value", "customanalyzer")
                .overridesForField("productNumber", "customanalyzer")
                .overridesForField("productDetails.sex.name_Filter", "filterAnalyzer")
                .overridesForField("lensColor.name_Filter", "filterAnalyzer")
                .overridesForField("frameColor.name_Filter", "filterAnalyzer")
                .overridesForField("productDetails.frameMaterial.name_Filter", "filterAnalyzer")
                .overridesForField("productDetails.diopters.value_Filter", "filterAnalyzer")
                .get();
    }
}
