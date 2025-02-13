package seedu.address.model.tuitionclass.predicates;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.tuitionclass.TuitionClass;

public class ClassTimingContainsKeywordsPredicate implements Predicate<TuitionClass> {

    private final List<String> keywords;

    public ClassTimingContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(TuitionClass tuitionClass) {
        return keywords.stream()
                .allMatch(keyword -> StringUtil.containsWordIgnoreCase(tuitionClass.getClassTiming().value, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ClassTimingContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ClassTimingContainsKeywordsPredicate) other).keywords)); // state check
    }
}
