package ch.epfl.tchu;

public final class Preconditions {
    /**
     * empty constructor to avoid instances from being created
     */
    private Preconditions() {
    }

    /**
     * method used to centralize the argument check of other classes
     *
     * @param shouldBeTrue the boolean that should be true, throws an error if it is false
     * @author Clément Husler(328105)
     * @author Mathieu Faure (328086)
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * method used to centralize the argument check of other classes
     *
     * @param message      the string to be displayed in the console, to help debug
     * @param shouldBeTrue the boolean that should be true, throws an error if it is false
     * @author Clément Husler(328105)
     * @author Mathieu Faure (328086)
     */
    public static void checkArgument(boolean shouldBeTrue, String message) {
        if (!shouldBeTrue) {
            System.out.println(message);
            throw new IllegalArgumentException();
        }
    }

}
