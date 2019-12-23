package svidnytskyy.glassesspring.repositories;

import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.search.*;
import org.apache.lucene.search.Sort;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.MustJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.sort.SortFieldContext;
import org.hibernate.search.query.dsl.sort.SortOrder;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import svidnytskyy.glassesspring.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.awt.print.Book;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


@Repository
//@Transactional
public class ProductSearch {
    @PersistenceContext
    EntityManager em;

    //    @Transactional
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
        System.out.println("<<text" + text);
        System.out.println("sexes" + sexes);
        System.out.println("minPrice" + minPrice);
        System.out.println("maxPrice" + maxPrice);
        System.out.println("lensColors" + lensColors);
        System.out.println("frameColors" + frameColors);
        System.out.println("frameMaterials" + frameMaterials);
        System.out.println("diopters>>" + diopters);
        if (!text.equals("")/* && !lensColors.equals("") && !frameColors.equals("")*/) {
            long start = System.currentTimeMillis();
//            getJpaQuery(text).setProjection(FullTextQuery.THIS, FullTextQuery.SCORE);
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

            System.out.println("TOTAL SESULTS!" + totalResults);
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

            long end = System.currentTimeMillis();
            System.out.println("Query execution time: " + (end - start));

            System.out.println("MY RESULT3!!!!" + results);


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

            long startTime = System.currentTimeMillis();
//            Query query = getQueryBuilder()
//                    .keyword()
//                    .onFields("productNumber", "category.uaName")
//                    .matching(text)
//                    .createQuery();
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime));

//
//            Session session = em.unwrap(Session.class);
//            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
//            Root root = criteriaQuery.from(Product.class);
//            Predicate predicate = criteriaBuilder.isNotNull(root.get(Product_.images));
//            criteriaQuery.where(criteriaBuilder.and(predicate));
            long start = System.currentTimeMillis();
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
//                        .setProjection("productNumber", "images.imageName", "category.uaName", "productDetails.price_Sort")
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
            long end = System.currentTimeMillis();
            System.out.println("Query execution time: " + (end - start));

            System.out.println("MY RESULT!!!!" + results);
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
//                        "productNumber_Sort",
//                        "productDetails.price",
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


//        Query luceneQuery = getQueryBuilder()
//
//                .bool()
//                .must(
//                        getQueryBuilder()
//                                .range()
//                                .onField("productDetails.price")
//                                .from(minPrice).to(maxPrice)
//                                .createQuery()
//                )
//                .must(
//                        getQueryBuilder()
//                                .keyword()
//                                .onFields("productNumber",
////                        "productNumber_Sort",
//                                        "category.uaName",
//                                        "category.name",
//                                        "lensColor.uaName",
//                                        "lensColor.name",
//                                        "productDetails.lensMaterial",
//                                        "productDetails.frameMaterial",
//                                        "productDetails.origin"
//                                )
////                .boostedTo(5)
//                                .matching(text)
//                                .createQuery()
//                )
//                .must(
//                        getQueryBuilder()
//                                .keyword()
//                                .onField("images.isMainImage")
//                                .matching(true)
//                                .createQuery()
//                )
//                .must(
//                        getQueryBuilder()
//                                .keyword()
//                                .onField("lensColor.name_Filter")
//                                .matching(lensColors)
//                                .createQuery()
//                )
//                .must(
//                        getQueryBuilder()
//                                .keyword()
//                                .onField("frameColor.name_Filter")
//                                .matching(frameColors)
//                                .createQuery()
//                )
//
//                .createQuery();

        return fullTextEntityManager.createFullTextQuery(luceneQuery, Product.class);
    }

    private QueryBuilder getQueryBuilder() throws InterruptedException {

        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);

//        fullTextEntityManager.createIndexer().startAndWait();

        return fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Product.class)
//                .overridesForField("productNumber_Sort", "custom_normalizer")
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
//    public List<Product> searchProductNameByKeywordQuery(String text) throws InterruptedException {
//
//        Query keywordQuery = getQueryBuilder()
//                .keyword()
//                .onField("productNumber")
//                .matching(text)
//                .createQuery();
//
//        List<Product> results = getJpaQuery(keywordQuery).getResultList();
//
//        return results;
//    }
//
//    public List<Product> searchProductNameByFuzzyQuery(String text) throws InterruptedException {
//
//        Query fuzzyQuery = getQueryBuilder()
//                .keyword()
//                .fuzzy()
//                .withEditDistanceUpTo(2)
//                .withPrefixLength(0)
//                .onField("productNumber")
//                .matching(text)
//                .createQuery();
//
//        List<Product> results = getJpaQuery(fuzzyQuery).getResultList();
//
//        return results;
//    }
//
//
//    public List<Product> searchProductDescriptionByPhraseQuery(String text) throws InterruptedException {
//
//        Query phraseQuery = getQueryBuilder()
//                .phrase()
//                .withSlop(1)
//                .onField("description")
//                .sentence(text)
//                .createQuery();
//
//        List<Product> results = getJpaQuery(phraseQuery).getResultList();
//
//        return results;
//    }
//
//    public List<Product> searchProductNameAndDescriptionBySimpleQueryStringQuery(String text) throws InterruptedException {
//
//        Query simpleQueryStringQuery = getQueryBuilder()
//                .simpleQueryString()
//                .onFields("productName", "description")
//                .matching(text)
//                .createQuery();
//
//        List<Product> results = getJpaQuery(simpleQueryStringQuery).getResultList();
//
//        return results;
//    }
//
//    public List<Product> searchProductNameByRangeQuery(int low, int high) throws InterruptedException {
//
//        Query rangeQuery = getQueryBuilder()
//                .range()
//                .onField("memory")
//                .from(low)
//                .to(high)
//                .createQuery();
//
//        List<Product> results = getJpaQuery(rangeQuery).getResultList();
//
//        return results;
//    }
//
//    public List<Object[]> searchProductNameByMoreLikeThisQuery(Product entity) throws InterruptedException {
//
//        Query moreLikeThisQuery = getQueryBuilder()
//                .moreLikeThis()
//                .comparingField("productName")
//                .toEntity(entity)
//                .createQuery();
//
//        List<Object[]> results = getJpaQuery(moreLikeThisQuery).setProjection(ProjectionConstants.THIS, ProjectionConstants.SCORE)
//                .getResultList();
//
//        return results;
//    }
//
//    public List<Product> searchProductNameAndDescriptionByKeywordQuery(String text) throws InterruptedException {
//
//        Query keywordQuery = getQueryBuilder()
//                .keyword()
//                .onFields("productName", "description")
//                .matching(text)
//                .createQuery();
//
//        List<Product> results = getJpaQuery(keywordQuery).getResultList();
//
//        return results;
//    }
//
//    public List<Object[]> searchProductNameAndDescriptionByMoreLikeThisQuery(Product entity) throws InterruptedException {
//
//        Query moreLikeThisQuery = getQueryBuilder()
//                .moreLikeThis()
//                .comparingField("productName")
//                .toEntity(entity)
//                .createQuery();
//
//        List<Object[]> results = getJpaQuery(moreLikeThisQuery).setProjection(ProjectionConstants.THIS, ProjectionConstants.SCORE)
//                .getResultList();
//
//        return results;
//    }
//
//    public List<Product> searchProductNameAndDescriptionByCombinedQuery(String manufactorer, int memoryLow, int memoryTop, String extraFeature, String exclude) throws InterruptedException {
//
//        Query combinedQuery = getQueryBuilder()
//                .bool()
//                .must(getQueryBuilder().keyword()
//                        .onField("productName")
//                        .matching(manufactorer)
//                        .createQuery())
//                .must(getQueryBuilder()
//                        .range()
//                        .onField("memory")
//                        .from(memoryLow)
//                        .to(memoryTop)
//                        .createQuery())
//                .should(getQueryBuilder()
//                        .phrase()
//                        .onField("description")
//                        .sentence(extraFeature)
//                        .createQuery())
//                .must(getQueryBuilder()
//                        .keyword()
//                        .onField("productName")
//                        .matching(exclude)
//                        .createQuery())
//                .not()
//                .createQuery();
//
//        List<Product> results = getJpaQuery(combinedQuery).getResultList();
//
//        return results;
//    }

}
