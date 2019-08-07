package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ToString
public enum Colors {
    WHITE(0, "Білий"),
    BLACK(1, "Чорний"),
    BLUE(2, "Синій"),
    RED(3, "Червоний"),
    YELLOW(4, "Жовтий"),
    SILVER(5, "Срібний"),
    GOLD(6, "Золотистий"),
    BROWN(7, "Коричневий"),
    CHAMELEON(8, "Хамелеон"),
    TRANSPARENT(10, "Прозорий"),
    BLACK_RED(13, "Чорно-червоний"),
    BLACK_YELLOW(14, "Чорно-жовтий");

    private int id;
    private String description;

    Colors(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    //    private static final Map<Integer, String> map = new HashMap<>();
//    static {
//        for (Colors color: Colors.values()){
//            map.put(color.id, color.description);
//
//        }
//    }

    public static Colors getById(int id) {
//        System.out.println(Stream.of(values() )
//                .filter((color) -> color.id.equals(id)).findAny().orElse(null));
//        return Stream.of(values() )
//                .filter((color) -> color.id.equals(id)).findAny().orElse(null);
//        System.out.println(map.get(id) + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        return map.get(id);
//        Colors[] colors = Colors.values();
        System.out.println(values());
        for (Colors color: values()) {
            if(color.id == id) {
                System.out.println(color.id);
                System.out.println(color.description);
                return color;
            }
        }
        throw new IllegalArgumentException();
    }

}
