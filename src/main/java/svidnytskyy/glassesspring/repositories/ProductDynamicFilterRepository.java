package svidnytskyy.glassesspring.repositories;

import org.hibernate.Session;
import org.hibernate.mapping.Collection;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.ArrayUtils;
import svidnytskyy.glassesspring.interfaces.ProductDynamicFilter;
import svidnytskyy.glassesspring.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ProductDynamicFilterRepository implements ProductDynamicFilter {

    @PersistenceContext
    private EntityManager em;

    public Page<Product> filterProductsByCriteria(
            String category,
            String sortBy,
            String sortDirection,
            List<Sex> sex,
            Integer minPrice,
            Integer maxPrice,
            List<LensColor> lensColor,
            List<FrameColor> frameColor,
            List<FrameMaterial> frameMaterial,
            List<Diopter> diopter,
            boolean polarization,
            Pageable pageable
    ) {
        System.out.println("MY LENS COLORS FILTER" + lensColor);
        System.out.println("MY DIOPTERS FILTER" + diopter);
        System.out.println("MY SEX FILTER" + sex);
        List<Product> products = new LinkedList<>();
        StringBuilder selectBuilder = new StringBuilder("select new map (p.id as id, p.uuid as uuid, pd.price as price, i.imageName as imageName, p.productNumber as productNumber, c.name as categoryName, c.uaName as categoryUaName) ");
        StringBuilder fromBuilder = new StringBuilder("from Product p ");
        StringBuilder joinBuilder = new StringBuilder("join p.productDetails as pd left join p.images as i with i.isMainImage = true join pd.category as c ");
        StringBuilder whereBuilder = new StringBuilder("where pd.price between :minprice and :maxprice ");
        StringBuilder groupByBuilder = new StringBuilder("group by p.id, p.uuid, p.productNumber, pd.price, i.imageName, c.name, c.uaName ");
        StringBuilder orderByBuilder = new StringBuilder("order by ");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                .append(selectBuilder)
                .append(fromBuilder);


        if (category != null ||
                !CollectionUtils.isEmpty(sex) ||
                !CollectionUtils.isEmpty(lensColor) ||
                !CollectionUtils.isEmpty(frameColor) ||
                !CollectionUtils.isEmpty(frameMaterial) ||
                !CollectionUtils.isEmpty(diopter) ||
                polarization == true
        ){

            if (category != null) {
//                joinBuilder.append("join p.category c ");
                if (whereBuilder.toString().length() > 6) {
                    whereBuilder.append("and ");
                }
                whereBuilder.append("c.name =:category ");
            }

            if (!CollectionUtils.isEmpty(sex)) {
                if (whereBuilder.toString().length() > 6) {
                    whereBuilder.append("and ");
                }
                whereBuilder.append("pd.sex in (:sex) ");
            }

            if (minPrice != null && maxPrice != null) {
//                joinBuilder.append("join p.productDetails pd ");
//                if(whereBuilder.toString().length() > 6) {
//                    whereBuilder.append("and ");
//                }
//                whereBuilder.append("pd.price between :minprice and :maxprice ");

            }

            if (!CollectionUtils.isEmpty(lensColor)) {
                if (whereBuilder.toString().length() > 6) {
                    whereBuilder.append("and ");
                }
                whereBuilder.append("p.lensColor in (:lenscolor) ");
            }

            if (!CollectionUtils.isEmpty(frameColor)) {
                if (whereBuilder.toString().length() > 6) {
                    whereBuilder.append("and ");
                }
                whereBuilder.append("p.frameColor in (:framecolor) ");
            }

            if (!CollectionUtils.isEmpty(frameMaterial)) {
                if (whereBuilder.toString().length() > 6) {
                    whereBuilder.append("and ");
                }
                whereBuilder.append("pd.frameMaterial in (:framematerial) ");
            }

            if (!CollectionUtils.isEmpty(diopter)) {
                joinBuilder.append("left join p.diopters as d ");
                if (whereBuilder.toString().length() > 6) {
                    whereBuilder.append("and ");
                }
                whereBuilder.append("d in (:diopter) ");
//                whereBuilder.append("d in (select distinct di from Diopter as di where di in (:diopter)) ");
            }
            if (polarization == true) {
                if (whereBuilder.toString().length() > 6) {
                    whereBuilder.append("and ");
                }
                whereBuilder.append("pd.polarization = true ");
            }


        }

        queryBuilder
                .append(joinBuilder)
                .append(whereBuilder);

        if (sortDirection.equalsIgnoreCase("desc")) {
            if (sortBy.equalsIgnoreCase("price")) {
                orderByBuilder.append("pd.price desc ");
            } else if (sortBy.equalsIgnoreCase("productnumber")) {
//                orderByBuilder.append("cast(p.productNumber as integer), p.productNumber desc ");
                groupByBuilder.append(", pd.modelNumber, p.lensColor, p.frameColor ");
                orderByBuilder.append("pd.modelNumber desc, p.lensColor desc, p.frameColor desc ");
            }
        } else {
            if (sortBy.equalsIgnoreCase("price")) {
                orderByBuilder.append("pd.price asc ");
            } else if (sortBy.equalsIgnoreCase("productnumber")) {
                groupByBuilder.append(", pd.modelNumber, p.lensColor, p.frameColor ");
                orderByBuilder.append("pd.modelNumber asc, p.lensColor asc, p.frameColor asc ");
            }
        }
        queryBuilder.append(groupByBuilder);
        queryBuilder.append(orderByBuilder);

        Session session = em.unwrap(org.hibernate.Session.class);
//        session.createQuery("select p from Product p join Category c where c.name in ('veloglasses')");
//        Query query = session.createQuery("select new map (p.productNumber, pd.price, i.imageName) from Product p join p.productDetails pd left join p.images i with i.isMainImage = true order by pd.price asc");
//        Query query = session.createQuery("select new map (pd.price as price, i.imageName as imageName, p.productNumber as productNumber, c.name as categoryName) from Product as p join p.productDetails as pd left join p.images as i with i.isMainImage = true join p.category as c order by pd.price asc" /*join p.productDetails pd left join p.images i with i.isMainImage = true order by pd.price asc"*/);
        Query query = session.createQuery(queryBuilder.toString());
        if (category != null) query.setParameter("category", category);
        if (!CollectionUtils.isEmpty(sex)) query.setParameter("sex", sex);
        query.setParameter("minprice", minPrice);
        query.setParameter("maxprice", maxPrice);
        if (!CollectionUtils.isEmpty(lensColor)) query.setParameter("lenscolor", lensColor);
        if (!CollectionUtils.isEmpty(frameColor)) query.setParameter("framecolor", frameColor);
        if (!CollectionUtils.isEmpty(frameMaterial)) query.setParameter("framematerial", frameMaterial);
        if (!CollectionUtils.isEmpty(diopter)) query.setParameter("diopter", diopter);
//        query.setParameter("orderby", sortBy);
        int totalItems = query.getResultList().size();
        products = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
//        queryBuilder.toString().replace("select p", "select count (*)");
//        long totalItems = (Long)(session.createQuery(queryBuilder.toString().replace("select p", "select count (*)")).iterate().next());


        return new PageImpl<>(products, pageable, totalItems);
    }
}
