package unicash.logic.parser;

import static java.util.Objects.requireNonNull;
import static unicash.logic.UniCashMessages.MESSAGE_INVALID_COMMAND_FORMAT;
import static unicash.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static unicash.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static unicash.logic.parser.CliSyntax.PREFIX_DATETIME;
import static unicash.logic.parser.CliSyntax.PREFIX_LOCATION;
import static unicash.logic.parser.CliSyntax.PREFIX_NAME;
import static unicash.logic.parser.CliSyntax.PREFIX_TYPE;

import java.util.List;

import unicash.commons.util.ToStringBuilder;
import unicash.logic.commands.FilterCommand;
import unicash.logic.parser.exceptions.ParseException;
import unicash.model.category.Category;
import unicash.model.commons.Amount;
import unicash.model.transaction.DateTime;
import unicash.model.transaction.Location;
import unicash.model.transaction.Name;
import unicash.model.transaction.Type;
import unicash.model.transaction.predicates.TransactionContainsAllKeywordsPredicate;


/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private TransactionContainsAllKeywordsPredicate filterPredicate;

    /**
     * Creates a {@code FilterCommandParser} object with a default
     * {@code TransactionContainsAllKeywordsPredicate} predicate.
     */
    FilterCommandParser() {
        this.filterPredicate = new TransactionContainsAllKeywordsPredicate();
    }

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TYPE, PREFIX_AMOUNT, PREFIX_DATETIME,
                        PREFIX_CATEGORY, PREFIX_LOCATION);

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty() || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        // TODO: Propose an future implementation with more than 2 types of transactions in DG
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TYPE);


        /* Parses the name keywords provided and adds them to the all keywords predicate */
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            List<String> keywordsList = argMultimap.getAllValues(PREFIX_NAME);

            for (String keyword : keywordsList) {
                Name transactionName = ParserUtil.parseTransactionName(keyword);

                filterPredicate.addNameKeyword(transactionName.toString());
            }

        }

        /* Parses the amount keywords provided and adds them to the all keywords predicate */
        if (argMultimap.getValue(PREFIX_AMOUNT).isPresent()) {
            List<String> keywordsList = argMultimap.getAllValues(PREFIX_AMOUNT);

            for (String keyword : keywordsList) {
                Amount transactionAmount = ParserUtil.parseAmount(keyword);

                filterPredicate.addAmountKeyword(
                        Amount.amountToDecimalString(transactionAmount));
            }

        }

        /* Parses the categories provided and adds them to the all keywords predicate */
        if (argMultimap.getValue(PREFIX_CATEGORY).isPresent()) {
            List<String> keywordsList = argMultimap.getAllValues(PREFIX_CATEGORY);

            for (String keyword : keywordsList) {
                Category transactionCategory = ParserUtil.parseCategory(keyword);

                filterPredicate.addCategoryKeyword(transactionCategory.toString());
            }

        }

        /* Parses the location keywords provided and adds them to the all keywords predicate */
        if (argMultimap.getValue(PREFIX_LOCATION).isPresent()) {
            List<String> keywordsList = argMultimap.getAllValues(PREFIX_LOCATION);

            for (String keyword : keywordsList) {
                Location transactionLocation = ParserUtil.parseLocation(keyword);

                filterPredicate.addLocationKeyword(transactionLocation.toString());
            }

        }

        /* Parses the dateTime keywords provided and adds them to the all keywords predicate */
        if (argMultimap.getValue(PREFIX_DATETIME).isPresent()) {
            List<String> keywordsList = argMultimap.getAllValues(PREFIX_DATETIME);

            for (String keyword : keywordsList) {
                DateTime transactionDateTime = ParserUtil.parseDateTime(keyword);

                filterPredicate.addDateTimeKeyword(transactionDateTime.toString());
            }

        }

        /* Parses the type keyword provided and adds it to the all keywords predicate */
        if (argMultimap.getValue(PREFIX_TYPE).isPresent()) {
            Type transactionType = ParserUtil.parseType(
                    argMultimap.getValue(PREFIX_TYPE).get());
            filterPredicate.addTypeKeyword(transactionType.toString());
        }


        return new FilterCommand(filterPredicate);

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCommandParser)) {
            return false;
        }

        FilterCommandParser otherCommand = (FilterCommandParser) other;
        return filterPredicate.equals(otherCommand.filterPredicate);

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("filterPredicate", filterPredicate)
                .toString();
    }

}

