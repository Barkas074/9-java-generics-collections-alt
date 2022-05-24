import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Operator<TCollection extends Collection<EValue>, EValue> {
    private final TCollection collection;

    private Operator(TCollection collection) {
        this.collection = collection;
    }

    public static <TCollection extends Collection<EValue>, EValue> Operator<TCollection, EValue> modify(TCollection collection) {
        return new Operator<>(collection);
    }

    public Operator<TCollection, EValue> add(EValue EValue) {
        collection.add(EValue);
        return this;
    }

    public Operator<TCollection, EValue> add(Collection<EValue> collection) {
        this.collection.addAll(collection);
        return this;
    }

    public Operator<TCollection, EValue> remove(Predicate<? super EValue> predicate) {
        collection.removeIf(predicate);
        return this;
    }

    public Operator<TCollection, EValue> sort(Comparator<? super EValue> comparator) {
        ArrayList<EValue> list = new ArrayList<>(collection);
        list.sort(comparator);
        collection.clear();
        collection.addAll(list);
        return this;
    }

    public Operator<TCollection, EValue> each(Consumer<? super EValue> consumer) {
        collection.forEach(consumer);
        return this;
    }

    public <TNewCollection extends Collection<EValue>> Operator<TNewCollection, EValue> copyTo(Supplier<TNewCollection> supplier) {
        return new Operator<>(
                collection.stream()
                        .collect(Collectors.toCollection(supplier))
        );
    }

    public <TNewCollection extends Collection<Result>, Result> Operator<TNewCollection, Result> convertTo(Supplier<TNewCollection> supplier, Function<EValue, Result> function) {
        return new Operator<>(
                collection.stream()
                        .map(function)
                        .collect(Collectors.toCollection(supplier))
        );
    }

    public TCollection get() {
        return collection;
    }
}
