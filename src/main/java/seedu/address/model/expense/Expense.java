package seedu.address.model.expense;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents an Expense in the finance tracker.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Expense {
    private final Name name;
    private final Amount amount;
    private final Category category;
    private final LocalDate date;
    private final Location location;

    /**
     * Every field must be present and not null.
     */
    public Expense(Name name, Amount amount, Category category, LocalDate date, Location location) {
        requireAllNonNull(name, amount, category, date, location);
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.location = location;
    }

    public Name getName() {
        return name;
    }

    public Amount getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amount, category, date, location);
    }

    /**
     * Returns true if both expenses have the same data fields.
     * This defines a stronger notion of equality between two expenses.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Expense)) {
            return false;
        }

        Expense otherExpense = (Expense) other;
        return name.equals(otherExpense.name)
                && amount == otherExpense.amount
                && category.equals(otherExpense.category)
                && date.equals(otherExpense.date)
                && location.equals(otherExpense.location);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("amount", amount)
                .add("category", category)
                .add("date", date)
                .add("location", location)
                .toString();
    }
}
