import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import static java.util.Arrays.asList;

/**
 * Remove command used to remove books in library by author or title.
 */
public class RemoveCmd extends LibraryCommand {

    // -------------- CONSTANTS AND FIELDS ------------------------------------

    /** Specify what kind of argument is given to remove by. */
    private RemoveType removeBy;

    /** Value to specify which book(s) must be removed. */
    private String removeValue;

    // -------------- CONSTRUCTOR(S) ------------------------------------------

    /**
     * Create a remove command.
     * Input is expected to be TITLE | AUTHOR followed by a non-blank string.
     *
     * @param argumentInput command argument input.
     * @throws IllegalArgumentException if given arguments are invalid.
     * @throws NullPointerException if the given argumentInput is null.
     */
    public RemoveCmd(String argumentInput) {
        super(CommandType.REMOVE, argumentInput);
    }

    // -------------- HELPER METHODS FOR CLASS FUNCTIONALITY METHODS ----------

    /**
     * Remove books written by author as specified in class field removeValue.
     *
     * @param data library data containing book entries.
     */
    private void removeByAuthor(LibraryData data) {
        List<BookEntry> books = data.getBookData();
        Iterator<BookEntry> bookIterator = books.iterator();
        int originalSize = books.size();
        int resultSize;

        while (bookIterator.hasNext()) {
            List<String> bookAuthors = asList(bookIterator.next().getAuthors());

            if (bookAuthors.contains(removeValue)) {
                bookIterator.remove();
            }
        }

        resultSize = books.size();

        if (originalSize == resultSize) {
            System.out.printf("0 books removed for author: %s", removeValue);
        } else {
            int bookRmvCount = originalSize - resultSize;
            System.out.printf("%d books removed for author: %s", bookRmvCount, removeValue);
        }
    }

    /**
     * Remove book with title as specified in class field removeValue.
     *
     * @param data library data containing book entries.
     */
    private void removeByTitle(LibraryData data) {
        List<BookEntry> books = data.getBookData();
        Iterator<BookEntry> bookIterator = books.iterator();
        int originalSize = books.size();
        int resultSize;

        while (bookIterator.hasNext()) {
            String title = bookIterator.next().getTitle();
            if (title.equals(removeValue)) {
                bookIterator.remove();
                break;                 // Title is unique in library. Can break out of iteration.
            }
        }

        resultSize = books.size();

        if (originalSize == resultSize) {
            System.out.printf("%s: not found.", removeValue);
        } else {
            System.out.printf("%s: removed successfully.", removeValue);
        }
    }

    // -------------- CLASS FUNCTIONALITY METHODS -----------------------------

    /**
     * Check for validity of input, i.e. remove type and remove value.
     * Parse if valid.
     *
     * @param argumentInput argument input for this command.
     * @return true if valid input, otherwise false.
     */
    @Override
    protected boolean parseArguments(String argumentInput) {
        StringTokenizer inputTokenizer = new StringTokenizer(argumentInput);
        boolean success = false;
        String potentialType = "";

        if (inputTokenizer.hasMoreTokens()) {
            potentialType = inputTokenizer.nextToken();
        }

        for (RemoveType type : RemoveType.values()) {
            if (type.name().equals(potentialType) &&        // Condition: Remove type is valid.
                    inputTokenizer.hasMoreTokens()) {       // Condition: There exists a non-blank remove value.
                removeBy = type;
                success = true;
            }
        }

        if (success) {
            StringBuilder removeValueBuilder = new StringBuilder();

            while (inputTokenizer.hasMoreTokens()) {
                removeValueBuilder.append(inputTokenizer.nextToken());
                removeValueBuilder.append(" ");
            }

            removeValue = removeValueBuilder.toString().trim();
        }

        return success;
    }

    /**
     * Execute remove command.
     *
     * @param data library data containing book entries.
     */
    @Override
    public void execute(LibraryData data) {
        Objects.requireNonNull(data, "Provided library data for RemoveCmd must not be null.");

        switch (removeBy) {
            case AUTHOR:
                removeByAuthor(data);
                break;
            case TITLE:
                removeByTitle(data);
        }
    }
}
