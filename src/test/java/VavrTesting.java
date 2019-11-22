import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
public class VavrTesting {

    private Logger logger = Logger.getGlobal();

    @Test
    public void option() {
        Long nullValue = null;
        int value = 0;

        // creating Optional:
        Optional<Integer> optionalInteger = Optional.of(value);// Optional[value]
        Optional.of(null); // is going to fail with NPE
        //Optional.ofNullable(); // Optional.empty

        // creating Option:
        Option.of(null); // None, will not explode with NPE
        Option.of(value);// Some[value]
        Option.some(null);// Some[null], there is no way to do this for Optional
        Option.none(); // None

        if (optionalInteger.isPresent()) {
            System.out.println(optionalInteger.get());
        } else {
            System.out.println("NOPE");
        }

        optionalInteger.ifPresentOrElse(integer -> System.out.println(optionalInteger.get()),
                                        () -> System.out.println("NOPE"));

        optionalInteger.get(); //nope
        optionalInteger.orElseGet(() -> 20);

        List.of(Optional.of(22), Optional.of(44))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List.of(Option.of(22), Option.of(44))
                .flatMap(integers -> integers);

    }

    @Test
    void tryornot() {

        try {
            new URI("");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }

        Try.of(() -> new URI(""))
                .onFailure(System.out::println) //hierarchy
                .onFailure(NullPointerException.class, Throwable::printStackTrace)
                .onSuccess(URI::getPath);

        Try.of(() -> new URI(""))
                .onFailure(Exception.class, Throwable::printStackTrace)
                .onFailure(RuntimeException.class, Throwable::printStackTrace);

        Try.of(() -> new URI(""))
                .onFailure(this::handleEx)
                .recoverWith(NullPointerException.class, e -> Try.of(() -> new URI("NNNNNN")));
    }

    private void handleEx(Throwable ex) {
        logger.log(Level.FINE, "", ex);
    }

    @Test
    void lazyyy() {
        Supplier<Integer> supplier = () -> {
            System.out.println("Comupting supplier");
            return 42;
        };

        supplier.get();
        supplier.get();

        Lazy<Integer> lazy = Lazy.of(() -> {
            System.out.println("Comupting lazy");
            return 42;
        });

        lazy.get();
        lazy.get();
    }

    @Test
    void persistenCollections() {

        List<String> of = List.of("12","13").foldRight(List.of("14"), (singleElement, strings) -> strings.prepend(singleElement));

        System.out.println(of);
    }

    @Test
    void Tuple() {
        Tuple2<String, Integer> firstAndEight = Tuple.of("first ", 8);

        String applyFunc = firstAndEight.apply((s, integer) -> s + integer);

        System.out.println(applyFunc);
    }

}
