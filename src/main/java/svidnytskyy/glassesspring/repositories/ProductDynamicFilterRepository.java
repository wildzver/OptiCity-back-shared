package svidnytskyy.glassesspring.repositories;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import svidnytskyy.glassesspring.interfaces.ProductDynamicFilter;
import svidnytskyy.glassesspring.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        List<Product> products = new LinkedList<>();
        StringBuilder selectBuilder = new StringBuilder("select new map (p.id as id, p.uuid as uuid, pd.price as price, i.imageName as imageName, p.productNumber as productNumber, c.name as categoryName, c.uaName as categoryUaName) ");
        StringBuilder fromBuilder = new StringBuilder("from Product p ");
        StringBuilder joinBuilder = new StringBuilder("join p.productDetails as pd left join p.images as i with i.isMainImage = true join pd.category as c ");
        StringBuilder whereBuilder = new StringBuilder("where pd.price between :minprice and :maxprice and i.isMainImage != null ");
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
        Query query = session.createQuery(queryBuilder.toString());
        if (category != null) query.setParameter("category", category);
        if (!CollectionUtils.isEmpty(sex)) query.setParameter("sex", sex);
        query.setParameter("minprice", minPrice);
        query.setParameter("maxprice", maxPrice);
        if (!CollectionUtils.isEmpty(lensColor)) query.setParameter("lenscolor", lensColor);
        if (!CollectionUtils.isEmpty(frameColor)) query.setParameter("framecolor", frameColor);
        if (!CollectionUtils.isEmpty(frameMaterial)) query.setParameter("framematerial", frameMaterial);
        if (!CollectionUtils.isEmpty(diopter)) query.setParameter("diopter", diopter);
        int totalItems = query.getResultList().size();
        products = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(products, pageable, totalItems);
    }
}
