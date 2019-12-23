select distinct product0_.id as id1_7_0_,
    productdet3_.id as id1_8_1_,
    product0_.created_at as created_2_7_0_,
    product0_.updated_at as updated_3_7_0_,
    product0_.category_id as category5_7_0_,
    product0_.frame_color_id as frame_co6_7_0_,
    product0_.lens_color_id as lens_col7_7_0_,
    product0_.product_details_id as product_8_7_0_,
    product0_.product_number as product_4_7_0_,
    productdet3_.bracket_length as bracket_2_8_1_,
    productdet3_.frame_material as frame_ma3_8_1_,
    productdet3_.lens_height as lens_hei4_8_1_,
    productdet3_.lens_material as lens_mat5_8_1_,
    productdet3_.lens_width as lens_wid6_8_1_,
    productdet3_.model_number as model_nu7_8_1_,
    productdet3_.origin as origin8_8_1_,
    productdet3_.price as price9_8_1_,
    productdet3_.total_width as total_w10_8_1_
from product product0_
inner join categories category1_ on product0_.category_id=category1_.id
left outer join images images2_ on product0_.id=images2_.product_id
inner join product_details productdet3_ on product0_.product_details_id=productdet3_.id
where productdet3_.price between 0 and 100000 order by product0_.product_number asc limit ?
