package svidnytskyy.glassesspring.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import svidnytskyy.glassesspring.models.*;

import java.util.List;

public interface ProductDynamicFilter {
    Page<Product> filterProductsByCriteria(
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
    );
}
