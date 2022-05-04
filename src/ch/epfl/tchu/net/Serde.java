package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * the Serde interface represents an object used to serialize and deserialize values of a given type
 *
 * @param <E> the given type parameter
 */
public interface Serde<E> {

    /**
     * abstract method redefined for different types later
     *
     * @param obj the object to serialize
     * @return a string representing the serialized value of the given type
     */
    String serialize(E obj);

    /**
     * abstract method redefined for different types later
     *
     * @param string the String to deserialize
     * @return an object  of the given type E representing the deserialized value of the given String
     */
    E deserialize(String string);

    /**
     * @param ser the serialization function used
     * @param des the deserialization function used
     * @param <T> the type parameter
     * @return the corresponding Serde
     */
    static <T> Serde<T> of(Function<T, String> ser, Function<String, T> des) {
        return new Serde<>() {
            @Override
            public String serialize(T obj) {
                return ser.apply(obj);
            }

            @Override
            public T deserialize(String string) {
                return des.apply(string);
            }
        };
    }

    /**
     * generic method used to (de)serialize an object of a given type T
     *
     * @param list the list of all the values of an enumerated type
     * @param <T>  the type parameter
     * @return the corresponding Serde
     */
    static <T> Serde<T> oneOf(List<T> list) {
        return Serde.of((T obj) -> String.valueOf(list.indexOf(obj)), (String string) -> list.get(Integer.parseInt(string)));
    }

    /**
     * generic method used to (de)serialize a List containing objects of a given type T
     *
     * @param serde     the Serde used to (de)serialize the list in the first time
     * @param separator the separation character
     * @param <T>       the type parameter
     * @return a Serde capable of (de)serializing values lists (de)serialized by the Serde taken in parameter
     */
    static <T> Serde<List<T>> listOf(Serde<T> serde, CharSequence separator) {
        return new Serde<>() {
            @Override
            public String serialize(List<T> list) {
                List<String> strings = new ArrayList<>();
                list.forEach(i -> strings.add(serde.serialize(i)));
                return String.join(separator, strings);
            }

            @Override
            public List<T> deserialize(String string) {
                List<T> list = new ArrayList<T>();
                if (!string.isEmpty()) {
                    String[] strings = string.split(Pattern.quote((String) separator), -1);
                    for (String s : strings) {
                        list.add(serde.deserialize(s));
                    }
                }
                return list;
            }
        };
    }

    /**
     * generic method used to (de)serialize a SortedBag containing objects of a given type T
     *
     * @param serde     the Serde used to (de)serialize the bag in the first time
     * @param separator the separation character
     * @param <T>       the type parameter
     * @return a Serde capable of (de)serializing values bags (de)serialized by the Serde taken in parameter
     */
    static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, CharSequence separator) {
        return Serde.of((SortedBag<T> b) -> Serde.listOf(serde, separator).serialize(b.toList()), (String s) -> SortedBag.of(listOf(serde, separator).deserialize(s)));
    }
}
